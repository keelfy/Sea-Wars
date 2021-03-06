package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class SPacketMove extends Packet {

	private WorldSide side;

	public SPacketMove() {}

	public SPacketMove(WorldSide side) {
		this.side = side;
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.side = WorldSide.values()[buffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(side.ordinal());
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleMove(this);
	}

	public WorldSide getSide() {
		return side;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayClient) netHandler);
	}
}
