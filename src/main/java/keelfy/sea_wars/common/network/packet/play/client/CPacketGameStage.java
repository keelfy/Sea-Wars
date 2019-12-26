package keelfy.sea_wars.common.network.packet.play.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.EnumGameStage;
import keelfy.sea_wars.server.network.play.INetHandlerPlayServer;

/**
 * @author keelfy
 */
public class CPacketGameStage extends Packet {

	private EnumGameStage gameStage;

	public CPacketGameStage() {}

	public CPacketGameStage(EnumGameStage gameStage) {
		this.gameStage = gameStage;
	}

	public void processPacket(INetHandlerPlayServer netHandler) {
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
		this.processPacket((INetHandlerPlayServer) netHandler);
	}
}