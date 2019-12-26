package keelfy.sea_wars.server.network.login;

import org.apache.log4j.Logger;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.login.CPacketLoginStart;
import keelfy.sea_wars.common.network.packet.login.SPacketLogged;
import keelfy.sea_wars.common.network.packet.login.SPacketLogout;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;
import keelfy.sea_wars.common.world.WorldSide;
import keelfy.sea_wars.server.SeaWarsServer;
import keelfy.sea_wars.server.ServerPlayer;
import keelfy.sea_wars.server.network.play.NetHandlerPlayServer;

/**
 * @author keelfy
 */
public class NetHandlerLoginServer implements INetHandlerLoginServer {

	private static final Logger logger = Logger.getLogger(NetHandlerLoginServer.class.getSimpleName());

	private final SeaWarsServer server;
	public final NetworkManager networkManager;
	private LoginState loginState;
	private int loginTimer;
	private String name;

	public NetHandlerLoginServer(SeaWarsServer server, NetworkManager networkManager) {
		this.loginState = LoginState.HELLO;
		this.server = server;
		this.networkManager = networkManager;
		this.loginTimer = 0;
	}

	@Override
	public void onNetworkTick() {
		if (this.loginState == LoginState.READY_TO_ACCEPT) {
			this.verifyPlayerConnection();
		}

		if (this.loginTimer++ >= 600) {
			this.terminate("Took too long to log in");
		}
	}

	public void terminate(String reason) {
		try {
			logger.info("Disconnecting " + this.getPlayerConnectionInfo() + ": " + reason);
			this.networkManager.scheduleOutboundPacket(new SPacketLogout(reason));
			this.networkManager.closeChannel(reason);
		} catch (Exception e) {
			logger.error("Error whilst disconnecting player", e);
		}
	}

	public void verifyPlayerConnection() {
		if (server.getWorld().getFreeSide() == null) {
			terminate("Server hasn't free sides on field");
			return;
		}

		for (ServerPlayer player : server.getPlayers()) {
			if (player.getName().equals(name)) {
				terminate("Player with your username already playing!");
				return;
			}
		}

		this.loginState = LoginState.ACCEPTED;
		WorldSide freeSide = server.getWorld().getFreeSide();
		server.getWorld().createField(freeSide);
		this.networkManager.scheduleOutboundPacket(new SPacketLogged(freeSide));
		ServerPlayer player = ServerPlayer.create(server, freeSide, name);
		this.networkManager.setNetHandler(new NetHandlerPlayServer(server, networkManager, player));
		for (ServerPlayer target : server.getPlayers()) {
			target.getNetHandler().sendPacket(new SPacketJoinGame(name, freeSide));
		}
		server.getPlayers().add(player);
	}

	@Override
	public void onDisconnect(String reason) {
		logger.info(this.getPlayerConnectionInfo() + " lost connection: " + reason);
	}

	public String getPlayerConnectionInfo() {
		return this.name != null ? this.name + " (" + this.networkManager.getSocketAddress().toString() + ")" : String.valueOf(this.networkManager.getSocketAddress());
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {}

	@Override
	public void processLoginStart(CPacketLoginStart packet) {
		this.name = packet.getName();
		this.loginState = LoginState.READY_TO_ACCEPT;
	}

	public String getName() {
		return name;
	}

	static enum LoginState {
		HELLO, READY_TO_ACCEPT, ACCEPTED;
	}
}