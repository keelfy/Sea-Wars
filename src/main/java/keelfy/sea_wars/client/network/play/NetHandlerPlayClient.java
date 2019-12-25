package keelfy.sea_wars.client.network.play;

import org.apache.log4j.Logger;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.player.ClientPlayer;
import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;
import keelfy.sea_wars.common.world.World;

/**
 * @author keelfy
 */
public class NetHandlerPlayClient implements INetHandlerPlayClient {

	private static final Logger logger = Logger.getLogger(NetHandlerPlayClient.class.getCanonicalName());

	private final NetworkManager netManager;
	private SeaWars sw;
	private World clientWorld;

	public NetHandlerPlayClient(SeaWars sw, NetworkManager networkManager) {
		this.sw = sw;
		this.netManager = networkManager;
	}

	@Override
	public void onNetworkTick() {}

	@Override
	public void handleJoinGame(SPacketJoinGame packet) {
		this.clientWorld = new World();
		this.sw.setPlayer(new ClientPlayer(sw.getUsername(), packet.getSide(), this.clientWorld));
	}

	@Override
	public void handleDisconnect(SPacketDisconnect packet) {
		this.netManager.closeChannel(packet.getReason());
	}

	@Override
	public void handleAlive(SPacketAlive packet) {
		this.sendPacket(new CPacketAlive(packet.getSendTime()));
	}

	@Override
	public void onDisconnect(String reason) {
		this.sw.setPlayer(null);
		this.sw.openGUI(null);
	}

	public void sendPacket(Packet packet) {
		this.netManager.scheduleOutboundPacket(packet);
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		throw new IllegalStateException("Unexpected protocol change!");
	}

	public NetworkManager getNetworkManager() {
		return this.netManager;
	}
}
