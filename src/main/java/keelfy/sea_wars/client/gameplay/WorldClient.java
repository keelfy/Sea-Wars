package keelfy.sea_wars.client.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class WorldClient extends World {

	private String winner;
	private List<ClientPlayer> players;

	public WorldClient() {
		super();

		players = new ArrayList<ClientPlayer>();
		this.createField(WorldSide.LEFT);
		this.createField(WorldSide.RIGHT);
	}

	public String getPlayerNameBySide(WorldSide side) {
		for (ClientPlayer player : players) {
			if (player.getSide() == side) {
				return player.getName();
			}
		}
		return "";
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public List<ClientPlayer> getPlayers() {
		return players;
	}

	public void setFields(Map<WorldSide, Field> fields) {
		this.fields = fields;
	}
}
