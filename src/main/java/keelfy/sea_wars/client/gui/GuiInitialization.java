package keelfy.sea_wars.client.gui;

import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.utils.GuiHelper;

/**
 * @author keelfy
 */
public class GuiInitialization implements Gui {

	private static final SeaWars SW = SeaWars.getInstance();

	@Override
	public void init() {

	}

	@Override
	public void draw() {
		GL11.glColor4f(1.0F, 0F, 1.0F, 1.0F);

		GuiHelper.drawRect(0f, 0f, 1f, 1f);
	}

	@Override
	public void onClose() {
	}

}
