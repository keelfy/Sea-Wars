package keelfy.sea_wars.common.network.packet.status.client;

import java.io.IOException;

import keelfy.sea_wars.common.network.INetHandler;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.PacketBuffer;
import keelfy.sea_wars.server.network.status.INetHandlerStatusServer;

public class CPacketQuery extends Packet {

	@Override
	public void readPacketData(PacketBuffer packetBuffer) throws IOException {
	}

	@Override
	public void writePacketData(PacketBuffer packetBuffer) throws IOException {
	}

	public void processPacket(INetHandlerStatusServer netHandler) {
		netHandler.processServerQuery(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler netHandler) {
		this.processPacket((INetHandlerStatusServer) netHandler);
	}
}