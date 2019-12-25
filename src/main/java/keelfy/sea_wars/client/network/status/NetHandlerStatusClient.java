package keelfy.sea_wars.client.network.status;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.status.server.SPacketInfo;
import keelfy.sea_wars.common.network.packet.status.server.SPacketPong;

/**
 * @author keelfy
 */
public class NetHandlerStatusClient {

	private NetworkManager networkManager;

	public NetHandlerStatusClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void handleServerInfo(SPacketInfo packet) {}

	public void handlePong(SPacketPong packet) {
		long sendTime = packet.getSendTime();
		long currentTime = System.currentTimeMillis();
		long pingToServer = currentTime - sendTime;
		System.out.println(pingToServer);
		networkManager.closeChannel("Finished");
	}

	public void onDisconnect(String reason) {}

	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		if (stateTo != EnumConnectionState.STATUS) {
			throw new UnsupportedOperationException("Unexpected change in protocol to " + stateTo);
		}
	}

	public void onNetworkTick() {}

}
