package keelfy.sea_wars.common.network.packet.play.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.WorldSide;
import keelfy.sea_wars.server.network.play.INetHandlerPlayServer;
import keelfy.sea_wars.server.network.play.NetHandlerPlayServer;

/**
 * @author keelfy
 */
public class CPacketReady extends Packet {

	private WorldSide side;

	public CPacketReady() {}

	public CPacketReady(WorldSide side) {
		this.side = side;
	}

	public void processPacket(INetHandlerPlayServer netHandler) {
		netHandler.handleReady(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		side = WorldSide.values()[buffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(side.ordinal());
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public WorldSide getSide() {
		return side;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((NetHandlerPlayServer) netHandler);
	}
}