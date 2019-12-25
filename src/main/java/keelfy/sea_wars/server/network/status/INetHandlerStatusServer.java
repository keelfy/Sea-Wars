package keelfy.sea_wars.server.network.status;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.status.client.CPacketPing;
import keelfy.sea_wars.common.network.packet.status.client.CPacketQuery;

/**
 * @author keelfy
 */
public interface INetHandlerStatusServer extends INetHandler {

	void processPing(CPacketPing packet);

	void processServerQuery(CPacketQuery packet);
}