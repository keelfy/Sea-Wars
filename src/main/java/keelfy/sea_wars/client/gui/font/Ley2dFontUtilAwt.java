package keelfy.sea_wars.client.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keelfy
 */
public class Ley2dFontUtilAwt {

	private static final float DEFAULT_FONT_SIZE = 32F;
	private static final Map<Integer, String> CHARS = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -5634414360950535370L;
		{
			put(0, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			put(1, "abcdefghijklmnopqrstuvwxyz");
			put(2, "0123456789");
			put(3, "$+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~ ");
		}
	};

	private Font font;
	private FontMetrics fontMetrics;

	public Ley2dFontUtilAwt(File ttfFile, int fontSize) throws Throwable {
		this.loadFont(ttfFile, fontSize);
	}

	public ByteBuffer getFontAsByteBuffer() {

		BufferedImage bufferedImage = this.generateBufferedImage();

		this.drawFontChars(bufferedImage);

		ByteBuffer byteBuffer = this.generateByteBuffer(bufferedImage);

		return byteBuffer;
	}

	public float getFontImageWidth() {
		return (float) CHARS.values().stream().mapToDouble(e -> fontMetrics.getStringBounds(e, null).getWidth()).max().getAsDouble();
	}

	public float getFontImageHeight() {
		return CHARS.keySet().size() * (this.getCharHeight());
	}

	/**
	 * @return the "start"-PositionX of the Char on the FontImage
	 */
	public float getCharX(char c) {
		String originStr = CHARS.values().stream().filter(e -> e.contains("" + c)).findFirst().orElse("" + c);
		return (float) fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null).getWidth();
	}

	/**
	 * @return the "start"-PositionY of the Char on the FontImage
	 */
	public float getCharY(char c) {
		float lineId = CHARS.keySet().stream().filter(i -> CHARS.get(i).contains("" + c)).findFirst().orElse(0);
		return this.getCharHeight() * lineId;
	}

	/**
	 * @return the width of the specific Character
	 */
	public float getCharWidth(char c) {
		return fontMetrics.charWidth(c);
	}

	/**
	 * Hint: getMaxAscent is the max Height above the baseline getMaxDescent is
	 * the max Height under the baseline
	 *
	 * @returns the max Height of every possible Char
	 */
	public float getCharHeight() {
		return fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
	}

	private BufferedImage generateBufferedImage() {
		// Configure
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
		graphics.setFont(font);
		fontMetrics = graphics.getFontMetrics();
		return graphics.getDeviceConfiguration().createCompatibleImage((int) getFontImageWidth(), (int) getFontImageHeight(), Transparency.TRANSLUCENT);
	}

	private void drawFontChars(BufferedImage imageBuffer) {
		// Draw the characters on our image
		Graphics2D imageGraphics = (Graphics2D) imageBuffer.getGraphics();
		imageGraphics.setFont(font);
		imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// draw every CHAR by line...
		imageGraphics.setColor(Color.WHITE);
		CHARS.keySet().stream().forEach(i -> imageGraphics.drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + (this.getCharHeight() * i)));
	}

	private ByteBuffer generateByteBuffer(BufferedImage imageBuffer) {
		// Generate texture data
		int[] pixels = new int[imageBuffer.getWidth() * imageBuffer.getHeight()];
		imageBuffer.getRGB(0, 0, imageBuffer.getWidth(), imageBuffer.getHeight(), pixels, 0, imageBuffer.getWidth());
		ByteBuffer imageData = ByteBuffer.allocateDirect((imageBuffer.getWidth() * imageBuffer.getHeight() * 4));

		for (int y = 0; y < imageBuffer.getHeight(); y++) {
			for (int x = 0; x < imageBuffer.getWidth(); x++) {
				int pixel = pixels[y * imageBuffer.getWidth() + x];
				imageData.put((byte) ((pixel >> 16) & 0xFF));
				imageData.put((byte) ((pixel >> 8) & 0xFF));
				imageData.put((byte) (pixel & 0xFF));
				imageData.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		imageData.flip();
		return imageData;
	}

	private void loadFont(File ttfFile, int fontSize) throws Throwable {
		try {
			font = Font.createFont(java.awt.Font.TRUETYPE_FONT, ttfFile).deriveFont(DEFAULT_FONT_SIZE);
		} catch (FontFormatException ex) {
			throw new Exception("Font is not a TTF!");
		}
	}
}
