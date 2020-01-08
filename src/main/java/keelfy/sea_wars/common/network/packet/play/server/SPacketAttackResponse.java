package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.client.network.play.NetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class SPacketAttackResponse extends Packet {

	private WorldSide side;
	private int x;
	private int y;
	private CellState state;

	public SPacketAttackResponse() {}

	public SPacketAttackResponse(WorldSide side, int x, int y, CellState state) {
		this.side = side;
		this.x = x;
		this.y = y;
		this.state = state;
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleAttackResponse(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.side = WorldSide.values()[buffer.readVarIntFromBuffer()];
		this.x = buffer.readVarIntFromBuffer();
		this.y = buffer.readVarIntFromBuffer();
		this.state = CellState.values()[buffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(side.ordinal());
		buffer.writeVarIntToBuffer(x);
		buffer.writeVarIntToBuffer(y);
		buffer.writeVarIntToBuffer(state.ordinal());
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public WorldSide getSide() {
		return side;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public CellState getState() {
		return state;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((NetHandlerPlayClient) netHandler);
	}
}