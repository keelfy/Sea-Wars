package keelfy.sea_wars.server.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.util.MessageDeserializer;
import keelfy.sea_wars.common.network.util.MessagePrepender;
import keelfy.sea_wars.common.network.util.MessageSerializer;
import keelfy.sea_wars.common.network.util.MessageSplitter;
import keelfy.sea_wars.server.SeaWarsServer;
import keelfy.sea_wars.server.network.handshake.NetHandlerHandshakeTCP;

/**
 * @author keelfy
 */
public class NetworkSystem {

	private static final Logger logger = Logger.getLogger(NetworkSystem.class.getCanonicalName());

	private static final NioEventLoopGroup loops = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("netty-%d").setDaemon(true).build());
	private final SeaWarsServer server;
	public volatile boolean isAlive;
	private final List<ChannelFuture> endpoints = Collections.<ChannelFuture>synchronizedList(new ArrayList<ChannelFuture>());
	private final List<NetworkManager> networkManagers = Collections.<NetworkManager>synchronizedList(new ArrayList<NetworkManager>());

	public NetworkSystem(SeaWarsServer server) {
		this.server = server;
		this.isAlive = true;
	}

	public void addLanEndpoint(InetAddress address, int port) throws IOException {
		synchronized (this.endpoints) {
			this.endpoints.add((new ServerBootstrap()).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) {
					try {
						channel.config().setOption(ChannelOption.IP_TOS, 24);
						channel.config().setOption(ChannelOption.TCP_NODELAY, false);
					} catch (ChannelException e) {
						;
					}

					channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new MessageSplitter()).addLast("decoder", new MessageDeserializer())
							.addLast("prepender", new MessagePrepender()).addLast("encoder", new MessageSerializer());
					NetworkManager manager = new NetworkManager(false);
					networkManagers.add(manager);
					channel.pipeline().addLast("packet_handler", manager);
					manager.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.server, manager));
				}
			}).group(loops).localAddress(address, port).bind().syncUninterruptibly());
		}
	}

	public void terminateEndpoints() {
		this.isAlive = false;
		Iterator<ChannelFuture> it = this.endpoints.iterator();

		while (it.hasNext()) {
			ChannelFuture channel = it.next();
			channel.channel().close().syncUninterruptibly();
		}
	}

	public void networkTick() {
		synchronized (this.networkManagers) {
			Iterator<NetworkManager> iterator = this.networkManagers.iterator();

			while (iterator.hasNext()) {
				final NetworkManager manager = iterator.next();

				if (!manager.isChannelOpen()) {
					iterator.remove();

					if (manager.getTerminationReason() != null) {
						manager.getNetHandler().onDisconnect(manager.getTerminationReason());
					} else if (manager.getNetHandler() != null) {
						manager.getNetHandler().onDisconnect("Disconnected - channel closed");
					}
				} else {
					try {
						manager.processReceivedPackets();
					} catch (Exception e) {
						logger.warn("Failed to handle packet for " + manager.getSocketAddress(), e);
						final String reason = "Internal server error";
						manager.scheduleOutboundPacket(new SPacketDisconnect(reason), (future) -> manager.closeChannel(reason));
						manager.disableAutoRead();
					}
				}
			}
		}
	}

	public SeaWarsServer getServer() {
		return server;
	}
}