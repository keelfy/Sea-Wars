package keelfy.sea_wars.server.network.handshake;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.handshake.CPacketHandshake;

/**
 * @author keelfy
 */
public interface INetHandlerHandshakeServer extends INetHandler {

	void processHandshake(CPacketHandshake packet);
}