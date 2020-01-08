package keelfy.sea_wars.client.network.play;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAttackResponse;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketTheEnd;
import keelfy.sea_wars.common.network.packet.play.server.SPacketGameStage;
import keelfy.sea_wars.common.network.packet.play.server.SPacketJoinGame;
import keelfy.sea_wars.common.network.packet.play.server.SPacketLeaveGame;
import keelfy.sea_wars.common.network.packet.play.server.SPacketMove;

/**
 * @author keelfy
 */
public interface INetHandlerPlayClient extends INetHandler {

	void handleJoinGame(SPacketJoinGame packet);

	void handleLeaveGame(SPacketLeaveGame packet);

	void handleDisconnect(SPacketDisconnect packet);

	void handleAlive(SPacketAlive packet);

	void handleGameStage(SPacketGameStage packet);

	void handleTheEnd(SPacketTheEnd packet);

	void handleMove(SPacketMove packet);

	void handleAttackResponse(SPacketAttackResponse packet);

}