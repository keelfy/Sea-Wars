package keelfy.sea_wars.client.network.login;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.login.SPacketLogout;
import keelfy.sea_wars.common.network.packet.login.SPacketLogged;

/**
 * @author keelfy
 */
public interface INetHandlerLoginClient extends INetHandler {

	void handleLoginSuccess(SPacketLogged packet);

	void handleDisconnect(SPacketLogout packet);
}