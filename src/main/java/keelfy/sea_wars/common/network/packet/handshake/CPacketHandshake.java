package keelfy.sea_wars.common.network.packet.handshake;

import java.io.IOException;

import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.handshake.INetHandlerHandshakeServer;

/**
 * @author keelfy
 */
public class CPacketHandshake extends Packet {

	private String address;
	private int port;
	private EnumConnectionState state;

	public CPacketHandshake() {}

	// Client sided constructor
	public CPacketHandshake(String address, int port, EnumConnectionState state) {
		this.address = address;
		this.port = port;
		this.state = state;
	}

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
		address = packetBuffer.readStringFromBuffer(255);
		port = packetBuffer.readVarIntFromBuffer();
		state = EnumConnectionState.values()[packetBuffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeStringToBuffer(this.address);
		packetBuffer.writeVarIntToBuffer(port);
		packetBuffer.writeVarIntToBuffer(this.state.ordinal());
	}

	public void processPacket(INetHandlerHandshakeServer netHandler) {
		netHandler.processHandshake(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public EnumConnectionState getState() {
		return this.state;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerHandshakeServer) netHandler);
	}
}