package keelfy.sea_wars.server.network.play;

import java.util.Iterator;

import org.apache.log4j.Logger;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.common.network.EnumConnectionState;
import keelfy.sea_wars.common.network.NetworkManager;
import keelfy.sea_wars.common.network.packet.Packet;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAlive;
import keelfy.sea_wars.common.network.packet.play.client.CPacketAttack;
import keelfy.sea_wars.common.network.packet.play.client.CPacketReady;
import keelfy.sea_wars.common.network.packet.play.client.CPacketShipPlace;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAlive;
import keelfy.sea_wars.common.network.packet.play.server.SPacketAttackResponse;
import keelfy.sea_wars.common.network.packet.play.server.SPacketDisconnect;
import keelfy.sea_wars.common.network.packet.play.server.SPacketGameStage;
import keelfy.sea_wars.common.network.packet.play.server.SPacketLeaveGame;
import keelfy.sea_wars.common.network.packet.play.server.SPacketMove;
import keelfy.sea_wars.common.network.packet.play.server.SPacketTheEnd;
import keelfy.sea_wars.common.world.EnumGameStage;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;
import keelfy.sea_wars.common.world.ship.EnumShipType;
import keelfy.sea_wars.server.SeaWarsServer;
import keelfy.sea_wars.server.ServerPlayer;

/**
 * @author keelfy
 */
public class NetHandlerPlayServer implements INetHandlerPlayServer {

	private static final Logger logger = Logger.getLogger(NetHandlerPlayServer.class.getCanonicalName());

	private final NetworkManager networkManager;
	private final SeaWarsServer server;
	private ServerPlayer player;

	private int networkTickCount;
	private int aliveSendTimeInt;
	private long aliveSendTime;
	private long lastAliveCheck;

	public NetHandlerPlayServer(SeaWarsServer server, NetworkManager networkManager, ServerPlayer player) {
		this.server = server;
		this.networkManager = networkManager;
		networkManager.setNetHandler(this);
		this.player = player;
		this.player.setNetHandler(this);
	}

	@Override
	public void onNetworkTick() {
		++this.networkTickCount;

		if (this.networkTickCount - this.lastAliveCheck > 40L) {
			this.lastAliveCheck = this.networkTickCount;
			this.aliveSendTime = getTime();
			this.aliveSendTimeInt = (int) this.aliveSendTime;
			this.sendPacket(new SPacketAlive(this.aliveSendTimeInt));
		}

		int idleTimeout = 30 * 60 * 1000; // 30 minutes
		if (this.player.getLastActionTime() > 0L && System.currentTimeMillis() - this.player.getLastActionTime() > idleTimeout) {
			this.kickPlayerFromServer("You have been idle for too long!");
		}
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void kickPlayerFromServer(String reason) {
		this.networkManager.scheduleOutboundPacket(new SPacketDisconnect(reason), (future) -> networkManager.closeChannel(reason));
		this.networkManager.disableAutoRead();
	}

	@Override
	public void onDisconnect(String reason) {
		logger.info(this.player.getName() + " lost connection: " + reason);

		Iterator<ServerPlayer> it = this.server.getPlayers().iterator();
		while (it.hasNext()) {
			ServerPlayer next = it.next();
			server.getWorld().getFields().remove(player.getSide());
			if (player.getName().equals(next.getName())) {
				it.remove();
			} else {
				next.getNetHandler().sendPacket(new SPacketLeaveGame(player.getName()));
			}
		}
	}

	@Override
	public void handleAlive(CPacketAlive packet) {
		if (packet.sendTime() == this.aliveSendTimeInt) {
			int difference = (int) (getTime() - this.aliveSendTime);
			this.player.setPing((this.player.getPing() * 3 + difference) / 4);
		}
	}

	@Override
	public void handleReady(CPacketReady packet) {
		SeaWars.getLogger().info(packet.getSide() + " is ready");

		server.getWorld().setReady(packet.getSide(), true);
		if (server.getWorld().isReady()) {
			for (ServerPlayer player : server.getPlayers()) {
				SeaWars.getLogger().info("Starting the game!");
				player.getNetHandler().sendPacket(new SPacketGameStage(EnumGameStage.PROCCESS));
				player.getNetHandler().sendPacket(new SPacketMove(server.getWorld().getSideOfMove()));
			}
		}
	}

	@Override
	public void handleShipPlace(CPacketShipPlace packet) {
		WorldSide side = this.player.getSide();

		int startX = packet.getStartX();
		int startY = packet.getStartY();

		int lengthX = !packet.isVertically() ? packet.getShipType().getLength() : 1;
		int lengthY = packet.isVertically() ? packet.getShipType().getLength() : 1;

		for (int i = startX - 1; i < startX + lengthX + 1; i++) {
			for (int j = startY - 1; j < startY + lengthY + 1; j++) {
				if (i < 0 || i > Field.FIELD_SIZE - 1 || j < 0 || j > Field.FIELD_SIZE - 1)
					continue;

				if (server.getWorld().getField(side).getCellState(i, j) != CellState.NONE) {
					return;
				}
			}
		}

		for (int i = 0; i < lengthX; i++) {
			for (int j = 0; j < lengthY; j++) {
				if (startX + i > Field.FIELD_SIZE - 1 || startY + j > Field.FIELD_SIZE - 1)
					return;
			}
		}

		for (int i = 0; i < lengthX; i++) {
			for (int j = 0; j < lengthY; j++) {
				server.getWorld().getField(side).setCellState(startX + i, startY + j, CellState.SHIP);
			}
		}
	}

	@Override
	public void handleAttack(CPacketAttack packet) {
		WorldSide attackSide = player.getSide().getOpposite();
		int x = packet.getX();
		int y = packet.getY();

		CellState state = server.getWorld().getField(attackSide).getCellState(x, y);
		CellState newState = state;

		switch (state) {
			case NONE :
				newState = CellState.MISS;
				break;
			case SHIP :
				newState = CellState.HIT;
				break;
			default :
				break;
		}

		if (state != newState) {
			server.getWorld().getField(attackSide).setCellState(x, y, newState);

			WorldSide winner = checkGameEnd();
			if (winner != null) {
				SeaWars.getLogger().info("Finishing up!");

				for (ServerPlayer target : server.getPlayers()) {
					target.getNetHandler().sendPacket(new SPacketGameStage(EnumGameStage.THE_END));

					String username = "";
					for (ServerPlayer player : server.getPlayers()) {
						if (player.getSide() == winner) {
							username = player.getName();
						}
					}

					target.getNetHandler().sendPacket(new SPacketTheEnd(username, server.getWorld().getFields()));
				}
			} else {
				server.getWorld().moveHappend();

				for (ServerPlayer target : server.getPlayers()) {
					target.getNetHandler().sendPacket(new SPacketMove(server.getWorld().getSideOfMove()));
					target.getNetHandler().sendPacket(new SPacketAttackResponse(attackSide, x, y, newState));
				}
			}
		}
	}

	public WorldSide checkGameEnd() {
		for (WorldSide side : WorldSide.values()) {
			Field field = server.getWorld().getField(side);
			int hits = field.countHits();

			if (hits == EnumShipType.allLength())
				return side;
		}
		return null;
	}

	public void sendPacket(final Packet packet) {
		this.networkManager.scheduleOutboundPacket(packet);
	}

	private long getTime() {
		return System.nanoTime() / 1000000L;
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState stateFrom, EnumConnectionState stateTo) {
		if (stateTo != EnumConnectionState.PLAY) {
			throw new IllegalStateException("Unexpected change in protocol!");
		}
	}
}