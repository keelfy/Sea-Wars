package keelfy.sea_wars.common.network;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.google.common.collect.BiMap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.util.MessageDeserializer;
import keelfy.sea_wars.common.network.util.MessagePrepender;
import keelfy.sea_wars.common.network.util.MessageSerializer;
import keelfy.sea_wars.common.network.util.MessageSplitter;

/**
 * @author keelfy
 */
public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

	private static final Logger logger = Logger.getLogger(NetworkManager.class.getSimpleName());

	public static final NioEventLoopGroup LOOPS = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("netty-client-%d").setDaemon(true).build());

	private boolean clientSide;

	public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = new AttributeKey<EnumConnectionState>("protocol");
	public static final AttributeKey<BiMap<Integer, Class<? extends Packet>>> attrKeyReceivable = new AttributeKey<BiMap<Integer, Class<? extends Packet>>>("receivable_packets");
	public static final AttributeKey<BiMap<Integer, Class<? extends Packet>>> attrKeySendable = new AttributeKey<BiMap<Integer, Class<? extends Packet>>>("sendable_packets");

	private final Queue<Packet> receivedPacketsQueue = Queues.<Packet>newConcurrentLinkedQueue();
	private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.<InboundHandlerTuplePacketListener>newConcurrentLinkedQueue();

	private INetHandler netHandler;

	private Channel channel;
	private SocketAddress socketAddress;
	private EnumConnectionState connectionState;
	private String terminationReason;

	public NetworkManager(boolean clientSide) {
		this.clientSide = clientSide;
	}

	// Only for client side
	public static NetworkManager provideLanClient(InetAddress address, int port) {
		final NetworkManager manager = new NetworkManager(true);
		new Bootstrap().group(LOOPS).handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel channel) {
				try {
					channel.config().setOption(ChannelOption.IP_TOS, 24);
					channel.config().setOption(ChannelOption.TCP_NODELAY, false);
				} catch (ChannelException e) {
					;
				}

				channel.pipeline().addLast("timeout", new ReadTimeoutHandler(20)).addLast("splitter", new MessageSplitter()).addLast("decoder", new MessageDeserializer())
						.addLast("prepender", new MessagePrepender()).addLast("encoder", new MessageSerializer()).addLast("packet_handler", manager);
			}
		}).channel(NioSocketChannel.class).connect(address, port).syncUninterruptibly();
		return manager;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

		this.channel = ctx.channel();
		this.socketAddress = ctx.channel().remoteAddress();
		setConnectionState(EnumConnectionState.HANDSHAKING);
	}

	public void setConnectionState(EnumConnectionState state) {
		this.connectionState = this.channel.attr(attrKeyConnectionState).getAndSet(state);
		this.channel.attr(attrKeyReceivable).set(state.getPacketsFromClient(clientSide));
		this.channel.attr(attrKeySendable).set(state.getPacketsFromServer(clientSide));
		this.channel.config().setAutoRead(true);
		logger.debug("Enabled auto read");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		this.closeChannel("End of stream");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if (channel.isOpen()) {
			if (packet.hasPriority()) {
				packet.processPacket(this.netHandler);
			} else {
				this.receivedPacketsQueue.add(packet);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
		String reason = t instanceof TimeoutException ? "Timeout" : ("Generic: " + t.toString());
		t.printStackTrace();
		this.closeChannel(reason);
	}

	public void scheduleOutboundPacket(Packet packetToSend, GenericFutureListener... listeners) {
		if (this.channel != null && this.channel.isOpen()) {
			this.flushOutboundQueue();
			this.dispatchPacket(packetToSend, listeners);
		} else {
			this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetToSend, listeners));
		}
	}

	private void dispatchPacket(final Packet packet, final GenericFutureListener... listeners) {
		final EnumConnectionState state = EnumConnectionState.getState(packet);
		final EnumConnectionState state1 = this.channel.attr(attrKeyConnectionState).get();

		if (state1 != state) {
			logger.debug("Disabled auto read");
			this.channel.config().setAutoRead(false);
		}

		if (this.channel.eventLoop().inEventLoop()) {
			if (state != state1) {
				this.setConnectionState(state);
			}

			this.channel.writeAndFlush(packet).addListeners(listeners).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		} else {
			this.channel.eventLoop().execute(() -> {
				if (state != state1) {
					setConnectionState(state);
				}
				channel.writeAndFlush(packet).addListeners(listeners).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			});
		}
	}

	private void flushOutboundQueue() {
		if (this.channel != null && this.channel.isOpen()) {
			while (!this.outboundPacketsQueue.isEmpty()) {
				NetworkManager.InboundHandlerTuplePacketListener inboundListener = this.outboundPacketsQueue.poll();
				this.dispatchPacket(inboundListener.packet, inboundListener.lisneters);
			}
		}
	}

	public void processReceivedPackets() {
		this.flushOutboundQueue();
		EnumConnectionState stateTo = this.channel.attr(attrKeyConnectionState).get();

		if (this.connectionState != stateTo) {
			if (this.connectionState != null) {
				this.netHandler.onConnectionStateTransition(this.connectionState, stateTo);
			}

			this.connectionState = stateTo;
		}

		if (this.netHandler != null) {
			for (int i = 1000; !this.receivedPacketsQueue.isEmpty() && i >= 0; --i) {
				Packet packet = this.receivedPacketsQueue.poll();
				packet.processPacket(this.netHandler);
			}

			this.netHandler.onNetworkTick();
		}

		this.channel.flush();
	}

	public void closeChannel(String reason) {
		if (this.channel.isOpen()) {
			this.channel.close();
			this.terminationReason = reason;
		}
	}

	public void setNetHandler(INetHandler netHandler) {
		logger.debug(String.format("Set listener of %s to %s", this.toString(), netHandler.toString()));
		this.netHandler = netHandler;
	}

	public void disableAutoRead() {
		this.channel.config().setAutoRead(false);
	}

	public Channel channel() {
		return channel;
	}

	public INetHandler getNetHandler() {
		return netHandler;
	}

	public boolean isChannelOpen() {
		return this.channel != null && this.channel.isOpen();
	}

	public boolean isClientSide() {
		return clientSide;
	}

	public String getTerminationReason() {
		return terminationReason;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	static class InboundHandlerTuplePacketListener {
		private final Packet packet;
		private final GenericFutureListener[] lisneters;

		public InboundHandlerTuplePacketListener(Packet packet, GenericFutureListener... listeners) {
			this.packet = packet;
			this.lisneters = listeners;
		}
	}
}
