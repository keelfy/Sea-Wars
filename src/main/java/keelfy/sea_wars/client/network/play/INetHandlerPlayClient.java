package keelfy.sea_wars.client.network.play;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;

/**
 * @author keelfy
 */
public interface INetHandlerPlayClient extends INetHandler {

	void handleJoinGame(SPacketJoinGame packet);

	void handleDisconnect(SPacketDisconnect packet);

	void handleAlive(SPacketAlive packet);
}