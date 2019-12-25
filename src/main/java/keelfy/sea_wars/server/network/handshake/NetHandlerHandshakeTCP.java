package keelfy.sea_wars.server.network.handshake;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.handshake.CPacketHandshake;
import keelfy.sea_wars.server.SeaWarsServer;
import keelfy.sea_wars.server.network.login.NetHandlerLoginServer;
import keelfy.sea_wars.server.network.status.NetHandlerStatusServer;

/**
 * @author keelfy
 */
public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {

	private final SeaWarsServer server;
	private final NetworkManager networkManager;

	public NetHandlerHandshakeTCP(SeaWarsServer server, NetworkManager networkManager) {
		this.server = server;
		this.networkManager = networkManager;
	}

	@Override
	public void processHandshake(CPacketHandshake packet) {
		switch (packet.getState()) {
			case LOGIN :
				this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
				this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
				break;
			case STATUS :
				this.networkManager.setConnectionState(EnumConnectionState.STATUS);
				this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
				break;
			default :
				throw new UnsupportedOperationException("Invalid intention " + packet.getState());
		}
	}

	@Override
	public void onDisconnect(String reason) {}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		if (stateTo != EnumConnectionState.LOGIN && stateTo != EnumConnectionState.STATUS) {
			throw new UnsupportedOperationException("Invalid state " + stateTo);
		}
	}

	@Override
	public void onNetworkTick() {}
}