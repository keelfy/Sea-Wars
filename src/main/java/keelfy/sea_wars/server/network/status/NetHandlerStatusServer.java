package keelfy.sea_wars.server.network.status;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.status.client.CPacketPing;
import keelfy.sea_wars.common.network.packet.status.client.CPacketQuery;
import keelfy.sea_wars.common.network.packet.status.server.SPacketPong;
import keelfy.sea_wars.server.SeaWarsServer;

/**
 * @author keelfy
 */
public class NetHandlerStatusServer implements INetHandlerStatusServer {

	private final SeaWarsServer server;
	private final NetworkManager networkManager;

	public NetHandlerStatusServer(SeaWarsServer server, NetworkManager networkManager) {
		this.server = server;
		this.networkManager = networkManager;
	}

	@Override
	public void onDisconnect(String reason) {}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		if (stateTo != EnumConnectionState.STATUS) {
			throw new UnsupportedOperationException("Unexpected change in protocol to " + stateTo);
		}
	}

	@Override
	public void onNetworkTick() {}

	@Override
	public void processServerQuery(CPacketQuery packet) {
		// this.networkManager.scheduleOutboundPacket(new
		// SPacketInfo(this.server.getInfo()));
	}

	@Override
	public void processPing(CPacketPing packet) {
		this.networkManager.scheduleOutboundPacket(new SPacketPong(packet.getSendTime()));
	}
}