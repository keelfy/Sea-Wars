package keelfy.sea_wars.client.gui.elements;

import java.awt.Color;

import keelfy.sea_wars.client.gui.Gui;
import keelfy.sea_wars.client.gui.utils.GuiHelper;

/**
 * @author keelfy
 */
public abstract class GuiButton implements Gui {

	private float x;
	private float y;
	private float width;
	private float height;

	private String text;
	private Color color;

	public GuiButton(String text, float x, float y, float width, float height, String color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = Color.decode(color);
	}

	@Override
	public void init() {

	}

	@Override
	public void draw(double mouseX, double mouseY) {
		GuiHelper.drawRect(x, y, width, height, color.getRGB());
	}

	public abstract void actionPerfomed();

	@Override
	public void onClose() {

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

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setColor(String color) {
		this.color = Color.decode(color);
	}
}
