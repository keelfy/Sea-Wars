package keelfy.sea_wars.common.network.packet.play.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.common.world.ship.EnumShipType;
import keelfy.sea_wars.server.network.play.INetHandlerPlayServer;

/**
 * @author keelfy
 */
public class CPacketShipPlace extends Packet {

	private int startX;
	private int startY;
	private EnumShipType shipType;
	private boolean vertically;

	public CPacketShipPlace() {}

	public CPacketShipPlace(int startX, int startY, EnumShipType shipType, boolean vertically) {
		this.startX = startX;
		this.startY = startY;
		this.shipType = shipType;
		this.vertically = vertically;
	}

	public void processPacket(INetHandlerPlayServer netHandler) {
		netHandler.handleShipPlace(this);
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {
		this.startX = buffer.readVarIntFromBuffer();
		this.startY = buffer.readVarIntFromBuffer();
		this.shipType = EnumShipType.values()[buffer.readVarIntFromBuffer()];
		this.vertically = buffer.readBoolean();
	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {
		buffer.writeVarIntToBuffer(startX);
		buffer.writeVarIntToBuffer(startY);
		buffer.writeVarIntToBuffer(this.shipType.ordinal());
		buffer.writeBoolean(vertically);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public EnumShipType getShipType() {
		return shipType;
	}

	public boolean isVertically() {
		return vertically;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerPlayServer) netHandler);
	}
}