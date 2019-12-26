package keelfy.sea_wars.client.network.login;

import org.apache.log4j.Logger;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gameplay.ClientPlayer;
import keelfy.sea_wars.client.gameplay.WorldClient;
import keelfy.sea_wars.client.gui.DisconnectedGUI;
import keelfy.sea_wars.client.gui.IngameGUI;
import keelfy.sea_wars.client.network.play.NetHandlerPlayClient;
import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.login.SPacketLogged;
import keelfy.sea_wars.common.network.packet.login.SPacketLogout;
import keelfy.sea_wars.common.world.World;

/**
 * @author keelfy
 */
public class NetHandlerLoginClient implements INetHandlerLoginClient {

	private static final Logger logger = Logger.getLogger(NetHandlerPlayClient.class.getSimpleName());
	private final SeaWars sw;
	private final NetworkManager networkManager;

	public NetHandlerLoginClient(SeaWars sw, NetworkManager networkManager) {
		this.sw = sw;
		this.networkManager = networkManager;
	}

	@Override
	public void handleLoginSuccess(SPacketLogged packet) {
		sw.openGUI(new IngameGUI(sw));

		this.networkManager.setConnectionState(EnumConnectionState.PLAY);
		NetHandlerPlayClient nhpc = new NetHandlerPlayClient(this.sw, this.networkManager);
		this.networkManager.setNetHandler(nhpc);
		this.sw.setNetHandler(nhpc);

		World world = new WorldClient();
		world.createField(packet.getSide());
		this.sw.setPlayer(new ClientPlayer(sw.getUsername(), packet.getSide(), world));
	}

	@Override
	public void onDisconnect(String reason) {
		sw.openGUI(new DisconnectedGUI(sw, reason));
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		logger.debug("Switching protocol from " + stateFrom + " to " + stateTo);
	}

	@Override
	public void onNetworkTick() {}

	@Override
	public void handleDisconnect(SPacketLogout packet) {
		this.networkManager.closeChannel(packet.getReason());
	}
}