package keelfy.sea_wars.server.network.play;

import java.util.Iterator;

import org.apache.log4j.Logger;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.server.SeaWarsServer;
import keelfy.sea_wars.server.ServerPlayer;

/**
 * @author keelfy
 */
public class NetHandlerPlayServer implements INetHandlerPlayServer {

	private static final Logger logger = Logger.getLogger(NetHandlerPlayServer.class.getCanonicalName());

	private final NetworkManager networkManager;
	private final SeaWarsServer server;
	private ServerPlayer player;

	private int networkTickCount;
	private int aliveSendTimeInt;
	private long aliveSendTime;
	private long lastAliveCheck;

	public NetHandlerPlayServer(SeaWarsServer server, NetworkManager networkManager, ServerPlayer player) {
		this.server = server;
		this.networkManager = networkManager;
		networkManager.setNetHandler(this);
		this.player = player;
		this.player.setNetHandler(this);
	}

	@Override
	public void onNetworkTick() {
		++this.networkTickCount;

		if (this.networkTickCount - this.lastAliveCheck > 40L) {
			this.lastAliveCheck = this.networkTickCount;
			this.aliveSendTime = getTime();
			this.aliveSendTimeInt = (int) this.aliveSendTime;
			this.sendPacket(new SPacketAlive(this.aliveSendTimeInt));
		}

		int idleTimeout = 2 * 60 * 1000; // 2 minutes
		if (this.player.getLastActionTime() > 0L && System.currentTimeMillis() - this.player.getLastActionTime() > idleTimeout) {
			this.kickPlayerFromServer("You have been idle for too long!");
		}
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void kickPlayerFromServer(String reason) {
		this.networkManager.scheduleOutboundPacket(new SPacketDisconnect(reason), (future) -> networkManager.closeChannel(reason));
		this.networkManager.disableAutoRead();
	}

	@Override
	public void onDisconnect(String reason) {
		logger.info(this.player.getName() + " lost connection: " + reason);

		Iterator<ServerPlayer> it = this.server.getPlayers().iterator();
		while (it.hasNext()) {
			if (player.getName().equals(it.next().getName())) {
				it.remove();
			}
		}
	}

	@Override
	public void processAlive(CPacketAlive packet) {
		if (packet.sendTime() == this.aliveSendTimeInt) {
			int difference = (int) (getTime() - this.aliveSendTime);
			this.player.setPing((this.player.getPing() * 3 + difference) / 4);
		}
	}

	public void sendPacket(final Packet packet) {
		this.networkManager.scheduleOutboundPacket(packet);
	}

	private long getTime() {
		return System.nanoTime() / 1000000L;
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		if (stateTo != EnumConnectionState.PLAY) {
			throw new IllegalStateException("Unexpected change in protocol!");
		}
	}
}