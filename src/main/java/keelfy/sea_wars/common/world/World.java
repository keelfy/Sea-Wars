package keelfy.sea_wars.common.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keelfy
 */
public class World {

	private EnumGameStage gameStage;
	private Map<WorldSide, Field> fields;

	private boolean[] ready;

	public World() {
		this.gameStage = EnumGameStage.READY;
		this.fields = new HashMap<WorldSide, Field>();
		this.ready = new boolean[WorldSide.values().length];
		Arrays.fill(ready, false);
	}

	public WorldSide getFreeSide() {
		return !this.fields.containsKey(WorldSide.LEFT) ? WorldSide.LEFT : (!this.fields.containsKey(WorldSide.RIGHT) ? WorldSide.RIGHT : null);
	}

	public EnumGameStage getGameStage() {
		return gameStage;
	}

	public Map<WorldSide, Field> getFields() {
		return fields;
	}

	public Field getField(WorldSide side) {
		return this.fields.get(side);
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
}
