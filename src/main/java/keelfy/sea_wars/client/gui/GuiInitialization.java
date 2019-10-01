package keelfy.sea_wars.client.gui;

import keelfy.sea_wars.client.DisplayHandler;
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
	public void draw(double mouseX, double mouseY) {
		DisplayHandler display = SeaWars.getInstance().getDisplay();

//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.drawRect(0, 0, display.getWidth(), display.getHeight(), 0xFFFFFF);

		double size = 20;
//		GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		GuiHelper.drawRect(0, 0, 50, display.getHeight(), 0xFF00FF);

		GuiHelper.drawRect(mouseX, mouseY, size, size, 0xFF00FF);
	}

	@Override
	public void onClose() {
	}

}
