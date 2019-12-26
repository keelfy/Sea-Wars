package keelfy.sea_wars.client.gameplay;

import java.util.ArrayList;
import java.util.List;

import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class WorldClient extends World {

	private List<ClientPlayer> players;

	public WorldClient() {
		super();

		players = new ArrayList<ClientPlayer>();
		this.createField(WorldSide.LEFT);
		this.createField(WorldSide.RIGHT);
	}

	public List<ClientPlayer> getPlayers() {
		return players;
	}

}
