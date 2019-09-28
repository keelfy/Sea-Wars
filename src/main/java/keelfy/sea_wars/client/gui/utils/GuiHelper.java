package keelfy.sea_wars.client.gui.utils;

import org.lwjgl.opengl.GL11;

/**
 * @author keelfy
 */
public class GuiHelper {

	public static void drawRect(float x, float y, float sizeX, float sizeY) {
		x -= 1;
		y -= 1;
		sizeX += 1;
		sizeY += 1;

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + sizeX, y);
		GL11.glVertex2f(x + sizeX, y + sizeY);
		GL11.glVertex2f(x, y + sizeY);
		GL11.glEnd();
	}

}
