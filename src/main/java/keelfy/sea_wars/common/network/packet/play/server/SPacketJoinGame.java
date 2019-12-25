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
public class SPacketJoinGame extends Packet {

	private WorldSide side;

	public SPacketJoinGame() {}

	public SPacketJoinGame(WorldSide side) {
		this.side = side;
	}

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
		this.side = WorldSide.values()[packetBuffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeVarIntToBuffer(this.side.ordinal());
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleJoinGame(this);
	}

	@Override
	public String serialize() {
		return String.format("side=%s", this.side.toString().toLowerCase());
	}

	public WorldSide getSide() {
		return side;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayClient) netHandler);
	}
}