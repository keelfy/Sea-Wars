package keelfy.sea_wars.client.gui.elements;

import java.awt.Color;

import keelfy.sea_wars.client.gui.font.Fonts;
import keelfy.sea_wars.client.gui.utils.GUIHelper;

/**
 * @author keelfy
 */
public class ButtonGUI implements IElementGUI {

	public static final Color HOVER_COLOR = GUIHelper.getColor("#1C9DE8");
	public static final Color COLOR = GUIHelper.getColor("#005AC7");
	public static final Color HOVER_TEXT = GUIHelper.getColor("#EFFA57");
	public static final Color TEXT = Color.BLACK;

	private int id;
	private int x;
	private int y;
	private int width;
	private int height;

	private String text;

	private boolean visible;
	private boolean enabled;

	protected long lastPressed = System.currentTimeMillis();

	public ButtonGUI(int id, String text, int x, int y, int width, int height) {
		this.id = id;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visible = true;
		this.enabled = true;
	}

	@Override
	public void init() {}

	@Override
	public void draw(double mouseX, double mouseY) {
		boolean mouseOver = isMouseOver(mouseX, mouseY);

		GUIHelper.drawRect(x, y, width, height, getColor(mouseOver));
		Fonts.drawCenteredString(text, x + width / 2, y + 4, enabled && mouseOver ? HOVER_TEXT : Color.WHITE);
	}

	@Override
	public void update() {}

	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	public Color getColor(boolean mouseOver) {
		if (!enabled)
			return Color.DARK_GRAY;

		return mouseOver ? HOVER_COLOR : COLOR;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void onClose() {}

	@Override
	public void keyTyped(int key, int keyCode) {}

	@Override
	public boolean mousePressed(double mouseX, double mouseY, int mouseButton) {
		return isMouseOver(mouseX, mouseY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		return isMouseOver(mouseX, mouseY);
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
