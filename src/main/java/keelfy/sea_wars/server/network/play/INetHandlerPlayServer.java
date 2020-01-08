package keelfy.sea_wars.server.network.play;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAttack;
import keelfy.sea_wars.common.network.packet.play.client.CPacketReady;
import keelfy.sea_wars.common.network.packet.play.client.CPacketShipPlace;

/**
 * @author keelfy
 */
public interface INetHandlerPlayServer extends INetHandler {

	void handleAlive(CPacketAlive packet);

	void handleShipPlace(CPacketShipPlace packet);

	void handleReady(CPacketReady packet);

	void handleAttack(CPacketAttack packet);
}