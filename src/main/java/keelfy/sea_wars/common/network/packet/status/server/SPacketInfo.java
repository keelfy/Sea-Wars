package keelfy.sea_wars.common.network.packet.status.server;

import java.io.IOException;

import keelfy.sea_wars.client.network.status.INetHandlerStatusClient;
import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;

/**
 * @author keelfy
 */
public class SPacketInfo extends Packet {

	public SPacketInfo() {
	}

	@Override
	public void readPacketData(PacketBuffer buffer) throws IOException {

	}

	@Override
	public void writePacketData(PacketBuffer buffer) throws IOException {

	}

	public void processPacket(INetHandlerStatusClient netHandler) {
		netHandler.handleServerInfo(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerStatusClient) netHandler);
	}
}