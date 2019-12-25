package keelfy.sea_wars.client.gui;

import java.awt.Color;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.utils.GUIHelper;
import keelfy.sea_wars.common.world.Field;
import keelfy.sea_wars.common.world.Field.CellState;
import keelfy.sea_wars.common.world.World;
import keelfy.sea_wars.common.world.WorldSide;

/**
 * @author keelfy
 */
public class IngameGUI extends BaseGUI {

	private static final int CELL_SIZE = 20;

	public IngameGUI(SeaWars sw) {
		super(sw);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.drawDefaultBackground();

		if (sw.getPlayer() == null)
			return;

		World world = sw.getPlayer().getWorld();
		Map<WorldSide, Field> field = world.getFields();
		float screenCenter = screenWidth / 2;

		CellState[][] states = field.get(WorldSide.LEFT).getCells();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for (int i = 0; i < Field.FIELD_SIZE; i++) {
			for (int j = 0; j < Field.FIELD_SIZE; j++) {
				CellState cellState = states[i][j];

				switch (cellState) {
					case NONE :
						GUIHelper.drawRect(screenCenter - Field.FIELD_SIZE + CELL_SIZE * i - 20 - i, 50 + Field.FIELD_SIZE + CELL_SIZE * j + j, CELL_SIZE, CELL_SIZE, Color.GRAY);
						break;
				}
			}
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		super.draw(mouseX, mouseY);
	}

	@Override
	public void actionPerfomed(int elementID, int mode) {

	}

}
