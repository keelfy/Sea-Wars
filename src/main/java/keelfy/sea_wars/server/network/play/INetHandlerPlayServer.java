package keelfy.sea_wars.server.network.play;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;

/**
 * @author keelfy
 */
public interface INetHandlerPlayServer extends INetHandler {

	void processAlive(CPacketAlive packet);
}