package keelfy.sea_wars.client.network.status;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.status.server.SPacketInfo;
import keelfy.sea_wars.common.network.packet.status.server.SPacketPong;

/**
 * @author keelfy
 */
public interface INetHandlerStatusClient extends INetHandler {

	void handleServerInfo(SPacketInfo packet);

	void handlePong(SPacketPong packet);
}