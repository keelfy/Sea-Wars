package keelfy.sea_wars.client.gui.utils;

import org.lwjgl.opengl.GL11;

public class Drawer {

	public void start() {
		GL11.glBegin(GL11.GL_QUADS);
	}

	public void setColor(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
	}

	public void setColor(int color) {
		int blue = color & 255;
		int green = (color >> 8) & 255;
		int red = (color >> 16) & 255;

		GL11.glColor4i(red, green, blue, 255);
	}

	public void addVertex(double x, double y) {
		GL11.glVertex2d(x, y);
	}

	public void draw() {
		GL11.glEnd();
	}

}
