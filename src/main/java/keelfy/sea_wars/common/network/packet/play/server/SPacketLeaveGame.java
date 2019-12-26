package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketLeaveGame extends Packet {

	private String name;

	public SPacketLeaveGame() {}

	public SPacketLeaveGame(String name) {
		this.name = name;
	}

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
		this.name = packetBuffer.readStringFromBuffer(32);
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeStringToBuffer(name);
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleLeaveGame(this);
	}

	public String getName() {
		return name;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayClient) netHandler);
	}
}