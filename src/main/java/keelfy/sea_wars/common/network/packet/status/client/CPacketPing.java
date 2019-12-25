package keelfy.sea_wars.common.network.packet.status.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.status.INetHandlerStatusServer;

/**
 * @author keelfy
 */
public class CPacketPing extends Packet {

	private long sendTime;

	public CPacketPing() {
	}

	public CPacketPing(long sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.sendTime = buffer.readLong();
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeLong(this.sendTime);
	}

	public void processPacket(INetHandlerStatusServer netHandler) {
		netHandler.processPing(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public long getSendTime() {
		return this.sendTime;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerStatusServer) netHandler);
	}
}