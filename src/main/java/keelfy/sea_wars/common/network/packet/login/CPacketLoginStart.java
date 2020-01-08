package keelfy.sea_wars.common.network.packet.login;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.login.INetHandlerLoginServer;

/**
 * @author keelfy
 */
public class CPacketLoginStart extends Packet {

	private String name;

	public CPacketLoginStart() {}

	public CPacketLoginStart(String name) {
		this.name = name;
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.name = buffer.readStringFromBuffer(256);
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeStringToBuffer(this.name);
	}

	public void processPacket(INetHandlerLoginServer netHandler) {
		netHandler.processLoginStart(this);
	}

	public String getName() {
		return name;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerLoginServer) netHandler);
	}
}