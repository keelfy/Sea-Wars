package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketAlive extends Packet {

	private int sendTime;

	public SPacketAlive() {}

	public SPacketAlive(int time) {
		this.sendTime = time;
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleAlive(this);
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

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayClient) netHandler);
	}

	public int getSendTime() {
		return this.sendTime;
	}
}