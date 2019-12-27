package keelfy.sea_wars.client.gui.font;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

/**
 * @author keelfy
 */
public class Ley2dFontLWJGL3 {

	private Ley2dFontUtilAwt fontUtil;
	private int fontTextureId;

	Ley2dFontLWJGL3(File ttfFile) throws Throwable {
		fontUtil = new Ley2dFontUtilAwt(ttfFile, 20);
		generateTexture(fontUtil.getFontAsByteBuffer());
	}

	private void generateTexture(ByteBuffer bb) {
		this.fontTextureId = glGenTextures();

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, this.fontTextureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int) fontUtil.getFontImageWidth(), (int) fontUtil.getFontImageHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, bb);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		GL11.glDisable(GL_TEXTURE_2D);
	}

	public void drawFontTexture(int x, int y) {
		glBindTexture(GL_TEXTURE_2D, this.fontTextureId);
		glBegin(GL_QUADS);

		glTexCoord2f(0, 0);
		glVertex3f(x, y, 0);

		glTexCoord2f(1, 0);
		glVertex3f(x + fontUtil.getFontImageWidth(), y, 0);

		glTexCoord2f(1, 1);
		glVertex3f(x + fontUtil.getFontImageWidth(), y + fontUtil.getFontImageHeight(), 0);

		glTexCoord2f(0, 1);
		glVertex3f(x, y + fontUtil.getFontImageHeight(), 0);

		glEnd();
	}

	public void drawText(String text, float xPosition, float yPosition) {
		glBindTexture(GL_TEXTURE_2D, this.fontTextureId);
		glBegin(GL_QUADS);
		float xTmp = xPosition;
		for (char c : text.toCharArray()) {
			float width = fontUtil.getCharWidth(c);
			float height = fontUtil.getCharHeight();
			float w = 1f / fontUtil.getFontImageWidth() * width;
			float h = 1f / fontUtil.getFontImageHeight() * height;
			float x = 1f / fontUtil.getFontImageWidth() * fontUtil.getCharX(c);
			float y = 1f / fontUtil.getFontImageHeight() * fontUtil.getCharY(c);

			glTexCoord2f(x, y);
			glVertex3f(xTmp, yPosition, 0);

			glTexCoord2f(x + w, y);
			glVertex3f(xTmp + width, yPosition, 0);

			glTexCoord2f(x + w, y + h);
			glVertex3f(xTmp + width, yPosition + height, 0);

			glTexCoord2f(x, y + h);
			glVertex3f(xTmp, yPosition + height, 0);

			xTmp += width;
		}
		glEnd();
	}

	public Ley2dFontUtilAwt getFontUtil() {
		return fontUtil;
	}
}