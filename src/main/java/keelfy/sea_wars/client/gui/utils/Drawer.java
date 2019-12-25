package keelfy.sea_wars.client.gui.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class Drawer {

	public void start() {
		GL11.glBegin(GL11.GL_QUADS);
	}

	public void setColor(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
	}

	public void setColor(Color color) {
		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
	}

	public void addVertex(double x, double y) {
		GL11.glVertex2d(x, y);
	}

	public void draw() {
		GL11.glEnd();
	}

}
