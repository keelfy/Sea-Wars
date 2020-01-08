package keelfy.sea_wars.common.network.packet.play.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.play.INetHandlerPlayClient;
import keelfy.sea_wars.client.network.play.NetHandlerPlayClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.EnumGameStage;

/**
 * @author keelfy
 */
public class SPacketGameStage extends Packet {

	private EnumGameStage gameStage;

	public SPacketGameStage() {}

	public SPacketGameStage(EnumGameStage gameStage) {
		this.gameStage = gameStage;
	}

	public void processPacket(INetHandlerPlayClient netHandler) {
		netHandler.handleGameStage(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.gameStage = EnumGameStage.values()[buffer.readVarIntFromBuffer()];
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(this.gameStage.ordinal());
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public EnumGameStage getGameStage() {
		return gameStage;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((NetHandlerPlayClient) netHandler);
	}
}