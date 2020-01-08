package keelfy.sea_wars.common.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keelfy
 */
public class World {

	protected EnumGameStage gameStage;
	protected Map<WorldSide, Field> fields;

	protected boolean[] ready;

	protected WorldSide sideOfMove;

	public World() {
		this.gameStage = EnumGameStage.PREPARATION;
		this.fields = new HashMap<WorldSide, Field>();
		this.ready = new boolean[WorldSide.values().length];
		Arrays.fill(ready, false);
		this.sideOfMove = WorldSide.LEFT;
	}

	public WorldSide getFreeSide() {
		return !this.fields.containsKey(WorldSide.LEFT) ? WorldSide.LEFT : (!this.fields.containsKey(WorldSide.RIGHT) ? WorldSide.RIGHT : null);
	}

	public EnumGameStage getGameStage() {
		return gameStage;
	}

	public void setGameStage(EnumGameStage gameStage) {
		this.gameStage = gameStage;
	}

	public Map<WorldSide, Field> getFields() {
		return fields;
	}

	public Field getField(WorldSide side) {
		return this.fields.get(side);
	}

	public void createField(WorldSide side) {
		this.fields.put(side, new Field());
	}

	public void setReady(WorldSide side, boolean value) {
		this.ready[side.ordinal()] = value;
	}

	public boolean isReady(WorldSide side) {
		return this.ready[side.ordinal()];
	}

	public boolean isReady() {
		for (WorldSide side : WorldSide.values()) {
			if (!this.ready[side.ordinal()])
				return false;
		}
		return true;
	}

	public WorldSide getSideOfMove() {
		return sideOfMove;
	}

	public void setSideOfMove(WorldSide side) {
		this.sideOfMove = side;
	}

	public void moveHappend() {
		this.sideOfMove = sideOfMove.getOpposite();
	}
}
