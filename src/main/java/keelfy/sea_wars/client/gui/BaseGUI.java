package keelfy.sea_wars.client.gui;

import java.util.ArrayList;
import java.util.List;

import keelfy.sea_wars.client.SeaWars;
import keelfy.sea_wars.client.gui.elements.IElementGUI;
import keelfy.sea_wars.client.gui.utils.GuiHelper;

/**
 * @author keelfy
 */
public abstract class BaseGUI implements GraphicalUI {

	protected final SeaWars sw;

	protected int screenWidth;
	protected int screenHeight;

	protected final List<IElementGUI> elements;

	public BaseGUI(SeaWars sw) {
		this.sw = sw;

		this.elements = new ArrayList<IElementGUI>();
	}

	@Override
	public void init() {
		this.screenWidth = sw.getDisplay().getWidth();
		this.screenHeight = sw.getDisplay().getHeight();

		this.elements.clear();
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		drawElements(mouseX, mouseY);
	}

	protected void drawDefaultBackground() {
		GuiHelper.drawFullscreenRect(screenWidth, screenHeight, GuiHelper.BACKGROUND_COLOR);
	}

	@Override
	public void update() {}

	protected void drawElements(double mouseX, double mouseY) {
		for (IElementGUI element : elements) {
			if (!element.isVisible())
				continue;

			element.draw(mouseX, mouseY);
		}
	}

	@Override
	public void onClose() {
		for (IElementGUI element : elements) {
			element.onClose();
		}
	}

	public abstract void actionPerfomed(int elementID, int mode);

	@Override
	public void keyTyped(int key, int keyCode) {}

	@Override
	public boolean mousePressed(double mouseX, double mouseY, int mouseButton) {
		for (IElementGUI element : elements) {
			if (!element.isEnabled())
				continue;

			if (element.mousePressed(mouseX, mouseY, mouseButton)) {
				this.actionPerfomed(element.getID(), 0);
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (IElementGUI element : elements) {
			if (!element.isEnabled())
				continue;

			if (element.mouseReleased(mouseX, mouseY, mouseButton)) {
				this.actionPerfomed(element.getID(), 1);
			}
		}
		return false;
	}
}
