package keelfy.sea_wars.server.network.login;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.login.CPacketLoginStart;

/**
 * @author keelfy
 */
public interface INetHandlerLoginServer extends INetHandler {
	void processLoginStart(CPacketLoginStart packet);
}