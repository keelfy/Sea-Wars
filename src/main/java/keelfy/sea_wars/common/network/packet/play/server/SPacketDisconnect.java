package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketDisconnect extends Packet {

	private String reason;

	public SPacketDisconnect() {
	}

	public SPacketDisconnect(String reason) {
		this.reason = reason;
	}

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
		this.reason = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeStringToBuffer(this.reason);
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleDisconnect(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayClient) netHandler);
	}

	public String getReason() {
		return reason;
	}
}