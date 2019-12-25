package keelfy.sea_wars.common.network.packet.status.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.status.INetHandlerStatusClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketPong extends Packet {

	private long sendTime;

	public SPacketPong() {
	}

	public SPacketPong(long sendTime) {
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

	public void processPacket(INetHandlerStatusClient netHandler) {
		netHandler.handlePong(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerStatusClient) netHandler);
	}

	/** Only for client */
	public long getSendTime() {
		return this.sendTime;
	}
}