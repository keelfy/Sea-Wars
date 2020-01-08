package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.client.network.play.NetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class SPacketTheEnd extends Packet {

	private String winner;
	private Map<WorldSide, Field> fields;

	public SPacketTheEnd() {}

	public SPacketTheEnd(String winner, Map<WorldSide, Field> fields) {
		this.winner = winner;
		this.fields = new HashMap<WorldSide, Field>(fields);
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleTheEnd(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.winner = buffer.readStringFromBuffer(256);
		this.fields = new HashMap<WorldSide, Field>();

		for (WorldSide side : WorldSide.values()) {
			Field field = new Field();

			for (int i = 0; i < Field.FIELD_SIZE; i++) {
				for (int j = 0; j < Field.FIELD_SIZE; j++) {
					CellState state = CellState.values()[buffer.readVarIntFromBuffer()];
					field.setCellState(i, j, state);
				}
			}

			this.fields.put(side, field);
		}
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeStringToBuffer(winner);

		for (WorldSide side : WorldSide.values()) {
			Field field = this.fields.get(side);

			for (int i = 0; i < Field.FIELD_SIZE; i++) {
				for (int j = 0; j < Field.FIELD_SIZE; j++) {
					buffer.writeVarIntToBuffer(field.getCellState(i, j).getBaseState().ordinal());
				}
			}
		}
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public String getWinner() {
		return winner;
	}

	public Map<WorldSide, Field> getFields() {
		return fields;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((NetHandlerPlayClient) netHandler);
	}
}
