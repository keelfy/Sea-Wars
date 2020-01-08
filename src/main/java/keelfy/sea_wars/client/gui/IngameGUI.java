package keelfy.sea_wars.client.gui;

import java.awt.Color;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gameplay.WorldClient;
import keelfy.sea_wars.client.gui.elements.ButtonGUI;
import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.client.gui.ingame.PreparationGUI;
import keelfy.sea_wars.client.gui.utils.GuiHelper;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class IngameGUI extends BaseGUI {

	public static final int CELL_SIZE = 30;

	public IngameGUI(SeaWars sw) {
		super(sw);
	}

	@Override
	public void init() {
		super.init();

		this.elements.add(new ButtonGUI(0, "Leave from server", screenWidth / 2 - 200, screenHeight - 60, 400, 50));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		if (SeaWars.getInstance().getPlayer() == null)
			return;

		WorldClient world = sw.getPlayer().getWorld();
		switch (world.getGameStage()) {
			case PREPARATION :
				sw.openGUI(new PreparationGUI(sw, this));
				break;
			case PROCCESS :
				sw.openGUI(new ProccessGUI(sw, this));
				break;
			case THE_END :
				sw.openGUI(new TheEndGUI(sw, this));
				break;
		}

		super.draw(mouseX, mouseY);
	}

	public int[] drawField(double mouseX, double mouseY, WorldSide side, Field field, boolean allowToHover) {
		CellState[][] states = field.getCells();
		float fieldSize = Field.FIELD_SIZE * CELL_SIZE + Field.FIELD_SIZE - 1;
		float posX = (screenWidth / 2 - fieldSize) / 2 + screenWidth / 2 * (side == null ? WorldSide.LEFT : side).ordinal();

		GuiHelper.drawRect(posX - 2, screenHeight / 2 - fieldSize / 2 - 2, fieldSize + 4, fieldSize + 4, Color.GRAY);

		int[] pair = null;

		if (side != null) {
			Fonts.drawCenteredString(side == sw.getPlayer().getSide() ? "You" : "Enemy", posX + fieldSize / 2 + 2, screenHeight / 2 - fieldSize / 2 - 45,
					side == sw.getPlayer().getSide() ? Color.GREEN : Color.RED);
		}

		for (int i = 0; i < Field.FIELD_SIZE; i++) {
			for (int j = 0; j < Field.FIELD_SIZE; j++) {
				CellState cellState = states[i][j];

				float x = posX + CELL_SIZE * i;
				float y = super.screenHeight / 2 - fieldSize / 2 + CELL_SIZE * j;

				boolean mouseOver = mouseX > x && mouseX < x + CELL_SIZE && mouseY > y && mouseY < y + CELL_SIZE;
				if (mouseOver) {
					pair = new int[2];
					pair[0] = i;
					pair[1] = j;
				}

				switch (cellState) {
					case NONE :
						GuiHelper.drawRect(x + i, y + j, CELL_SIZE, CELL_SIZE, allowToHover && mouseOver ? Color.LIGHT_GRAY : Color.WHITE);
						break;
					case HIT :
						GuiHelper.drawRect(x + i, y + j, CELL_SIZE, CELL_SIZE, allowToHover && mouseOver ? Color.LIGHT_GRAY : Color.RED);
						break;
					case MISS :
						GuiHelper.drawRect(x + i, y + j, CELL_SIZE, CELL_SIZE, allowToHover && mouseOver ? Color.LIGHT_GRAY : Color.DARK_GRAY);
						break;
					case SHIP :
						GuiHelper.drawRect(x + i, y + j, CELL_SIZE, CELL_SIZE, allowToHover && mouseOver ? Color.MAGENTA : Color.BLACK);
						break;
				}
			}
		}
		return pair;
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {
		if (mode != 0)
			return;

		switch (elementID) {
			case 0 :
				sw.getNetHandler().getNetworkManager().closeChannel("Disconnected by user");
				sw.openGUI(new MainMenuGUI(sw));
				sw.setPlayer(null);
				break;
		}
	}

}
