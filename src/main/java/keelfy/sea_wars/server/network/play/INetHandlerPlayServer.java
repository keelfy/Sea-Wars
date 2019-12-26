package keelfy.sea_wars.server.network.play;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.client.CPacketShipPlace;
import keelfy.sea_wars.common.network.packet.play.client.CPacketGameStage;

/**
 * @author keelfy
 */
public interface INetHandlerPlayServer extends INetHandler {

	void handleAlive(CPacketAlive packet);

	void handleGameStage(CPacketGameStage packet);

	void handleShipPlace(CPacketShipPlace packet);
}