package keelfy.sea_wars.client.gui.font;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public class Fonts {

	private static boolean loaded = false;
	public static Ley2dFontLWJGL3 monofonto;

	public static void create() {
		try {
			monofonto = new Ley2dFontLWJGL3(SeaWars.class.getResource("monofonto.ttf"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		loaded = true;
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void drawString(String text, float x, float y, Color color, float scale) {
		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		monofonto.drawText(text, 0, 0);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void drawString(String text, float x, float y, Color color) {
		drawString(text, x, y, color, 1F);
	}

	public static void drawCenteredString(String text, float x, float y, Color color) {
		drawString(text, x - getStringWidth(text) / 2, y, color);
	}

	public static float getStringWidth(String text) {
		float result = 0F;
		for (Character ch : text.toCharArray()) {
			result += monofonto.getFontUtil().getCharWidth(ch);
		}
		return result;
	}

	public static float getFontHeight() {
		return monofonto.getFontUtil().getCharHeight();
	}
}
