package keelfy.sea_wars.common.network.packet.play.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.play.INetHandlerPlayServer;

/**
 * @author keelfy
 */
public class CPacketAlive extends Packet {

	private int sendTime;

	public CPacketAlive() {}

	public CPacketAlive(int time) {
		this.sendTime = time;
	}

	public void processPacket(INetHandlerPlayServer netHandler) {
		netHandler.processAlive(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.sendTime = buffer.readInt();
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeInt(this.sendTime);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public int sendTime() {
		return this.sendTime;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayServer) netHandler);
	}
}