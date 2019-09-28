package keelfy.sea_wars.client.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public class VideoSettingsHandler implements ISettingsHandler {

	private Properties properties;

	private int width = 1280;
	private int height = 720;
	private int framesLimit = 60;

	public VideoSettingsHandler() {
		this.properties = new Properties();
	}

	@Override
	public void reload() {
		FileInputStream fis;

		try {
			fis = new FileInputStream(SeaWars.getDataFolder());
			this.properties.load(fis);

			this.width = Integer.parseInt(this.properties.getProperty("screen_width", "1280"));
			this.height = Integer.parseInt(this.properties.getProperty("screen_height", "720"));
			this.framesLimit = Integer.parseInt(this.properties.getProperty("fps_limit", "60"));
		} catch (FileNotFoundException e) {
			SeaWars.getLogger().error("Video settings file not found", e);
			return;
		} catch (IOException e) {
			SeaWars.getLogger().error("Error occurred while loading video settings", e);
			return;
		}

		SeaWars.getLogger().info("Video settings loaded");
	}

	@Override
	public void save() {

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFramesLimit() {
		return framesLimit;
	}

}
