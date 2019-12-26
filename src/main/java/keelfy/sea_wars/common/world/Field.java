package keelfy.sea_wars.common.world;

/**
 * @author keelfy
 */
public class Field {

	public static final int FIELD_SIZE = 10;
	private final CellState[][] cells = new CellState[FIELD_SIZE][FIELD_SIZE];

	public Field() {
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
				cells[i][j] = CellState.NONE;
			}
		}
	}

	public CellState[][] getCells() {
		return cells;
	}

	public CellState getCellState(int i, int j) {
		return cells[i][j];
	}

	public void setCellState(int i, int j, CellState state) {
		cells[i][j] = state;
	}

	public static enum CellState {
		SHIP, HIT, MISS, NONE;
	}
}
