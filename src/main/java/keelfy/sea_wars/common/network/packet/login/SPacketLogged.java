package keelfy.sea_wars.common.network.packet.login;

import java.io.IOException;

import keelfy.sea_wars.client.network.login.INetHandlerLoginClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketLogged extends Packet {

	public SPacketLogged() {}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {}

	public void processPacket(INetHandlerLoginClient netHandler) {
		netHandler.handleLoginSuccess(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerLoginClient) netHandler);
	}
}