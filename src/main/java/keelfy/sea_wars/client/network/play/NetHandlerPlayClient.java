package keelfy.sea_wars.client.network.play;

import java.util.Iterator;

import org.apache.log4j.Logger;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gameplay.ClientPlayer;
import keelfy.sea_wars.client.gameplay.WorldClient;
import keelfy.sea_wars.client.gui.DisconnectedGUI;
import keelfy.sea_wars.client.gui.IngameGUI;
import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketGameStage;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;
import keelfy.sea_wars.common.network.packet.play.server.SPacketLeaveGame;

/**
 * @author keelfy
 */
public class NetHandlerPlayClient implements INetHandlerPlayClient {

	private static final Logger logger = Logger.getLogger(NetHandlerPlayClient.class.getSimpleName());

	private final NetworkManager netManager;
	private SeaWars sw;

	public NetHandlerPlayClient(SeaWars sw, NetworkManager networkManager) {
		this.sw = sw;
		this.netManager = networkManager;
	}

	@Override
	public void onNetworkTick() {}

	@Override
	public void handleJoinGame(SPacketJoinGame packet) {
		WorldClient world = sw.getPlayer().getWorld();
		world.getPlayers().add(new ClientPlayer(packet.getName(), packet.getSide(), world));
		world.createField(packet.getSide());
	}

	@Override
	public void handleLeaveGame(SPacketLeaveGame packet) {
		if (sw.getPlayer() != null) {
			WorldClient world = sw.getPlayer().getWorld();
			Iterator<ClientPlayer> it = world.getPlayers().iterator();
			while (it.hasNext()) {
				if (it.next().getName().equals(packet.getName()))
					it.remove();
			}
		}
	}

	@Override
	public void handleDisconnect(SPacketDisconnect packet) {
		this.netManager.closeChannel(packet.getReason());
		this.sw.openGUI(new DisconnectedGUI(sw, packet.getReason()));
		this.sw.setPlayer(null);
	}

	@Override
	public void handleAlive(SPacketAlive packet) {
		this.sendPacket(new CPacketAlive(packet.getSendTime()));
	}

	@Override
	public void handleGameStage(SPacketGameStage packet) {
		sw.getPlayer().getWorld().setGameStage(packet.getGameStage());
		sw.openGUI(new IngameGUI(sw));
	}

	@Override
	public void onDisconnect(String reason) {
		this.sw.setPlayer(null);
		this.sw.openGUI(new DisconnectedGUI(sw, reason));
	}

	public void sendPacket(Packet packet) {
		this.netManager.scheduleOutboundPacket(packet);
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		// throw new IllegalStateException("Unexpected protocol change!");
	}

	public NetworkManager getNetworkManager() {
		return this.netManager;
	}
}
