package keelfy.sea_wars.common.network.packet.login;

import java.io.IOException;

import keelfy.sea_wars.client.network.login.INetHandlerLoginClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketLogout extends Packet {

	private String reason;

	public SPacketLogout() {}

	public SPacketLogout(String reason) {
		this.reason = reason;
	}

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
		this.reason = packetBuffer.readStringFromBuffer(256);
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeStringToBuffer(this.reason);
	}

	public void processPacket(INetHandlerLoginClient netHandler) {
		netHandler.handleDisconnect(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerLoginClient) netHandler);
	}

	public String getReason() {
		return reason;
	}
}