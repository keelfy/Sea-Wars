package keelfy.sea_wars.common.network.packet.play.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.play.INetHandlerPlayServer;
import keelfy.sea_wars.server.network.play.NetHandlerPlayServer;

/**
 * @author keelfy
 */
public class CPacketAttack extends Packet {

	private int x;
	private int y;

	public CPacketAttack() {}

	public CPacketAttack(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void processPacket(INetHandlerPlayServer netHandler) {
		netHandler.handleAttack(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.x = buffer.readVarIntFromBuffer();
		this.y = buffer.readVarIntFromBuffer();
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(x);
		buffer.writeVarIntToBuffer(y);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((NetHandlerPlayServer) netHandler);
	}
}