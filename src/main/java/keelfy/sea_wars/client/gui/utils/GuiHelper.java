package keelfy.sea_wars.client.gui.utils;

import java.awt.Color;

/**
 * @author keelfy
 */
public class GUIHelper {

	public static final Color BACKGROUND_COLOR = GUIHelper.getColor("#FFFFFF");

	public static void drawRect(double x, double y, double width, double height, Color color) {
		Drawer drawer = new Drawer();
		drawer.start();
		drawer.setColor(color);
		drawer.addVertex(x, y);
		drawer.addVertex(x, y + height);
		drawer.addVertex(x + width, y + height);
		drawer.addVertex(x + width, y);
		drawer.draw();
	}

	public static void drawFullscreenRect(int screenWidth, int screenHeight, Color color) {
		drawRect(0, 0, screenWidth, screenHeight, color);
	}

	public static Color getColor(String hue, String opacity) {
		int r;
		int g;
		int b;
		if (hue == null || hue.isEmpty()) {
			r = 0;
			g = 0;
			b = 0;
		} else {
			r = Integer.parseInt(hue.substring(1, 3), 16);
			g = Integer.parseInt(hue.substring(3, 5), 16);
			b = Integer.parseInt(hue.substring(5, 7), 16);
		}
		int a;
		if (opacity == null || opacity.isEmpty()) {
			a = 255;
		} else {
			double x;
			try {
				x = Double.parseDouble(opacity);
			} catch (NumberFormatException e) {
				// some localizations use commas for decimal points
				int comma = opacity.lastIndexOf(',');
				if (comma >= 0) {
					try {
						String repl = opacity.substring(0, comma) + "." + opacity.substring(comma + 1);
						x = Double.parseDouble(repl);
					} catch (Throwable t) {
						throw e;
					}
				} else {
					throw e;
				}
			}
			a = (int) Math.round(x * 255);
		}
		return new Color(r, g, b, a);
	}

	public static Color getColor(String hue) {
		return getColor(hue, "");
	}
}
