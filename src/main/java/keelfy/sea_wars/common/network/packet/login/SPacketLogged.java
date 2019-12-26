package keelfy.sea_wars.common.network.packet.login;

import java.io.IOException;

import keelfy.sea_wars.client.network.login.INetHandlerLoginClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class SPacketLogged extends Packet {

	private WorldSide side;

	public SPacketLogged() {}

	public SPacketLogged(WorldSide side) {
		this.side = side;
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		side = WorldSide.values()[buffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(side.ordinal());
	}

	public void processPacket(INetHandlerLoginClient netHandler) {
		netHandler.handleLoginSuccess(this);
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
		this.processPacket((INetHandlerLoginClient) netHandler);
	}
}