package keelfy.sea_wars.client.gui.utils;

/**
 * @author keelfy
 */
public class GuiHelper {

	public static void drawRect(double x, double y, double width, double height, int color) {
		Drawer drawer = new Drawer();
		drawer.start();
		drawer.setColor(color);
		drawer.addVertex(x, y);
		drawer.addVertex(x + width, y);
		drawer.addVertex(x + width, y + height);
		drawer.addVertex(x, y + height);
		drawer.draw();
	}
}
