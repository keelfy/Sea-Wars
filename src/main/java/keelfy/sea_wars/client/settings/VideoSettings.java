package keelfy.sea_wars.client.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import keelfy.sea_wars.client.SeaWars;

/**
 * @author keelfy
 */
public class VideoSettings implements ISettings {

	private File path;
	private Properties properties;

	private int width;
	private int height;
	private int framesLimit;

	public VideoSettings(File path) {
		this.path = path;
		this.properties = new Properties();
	}

	@Override
	public void load() {
		FileInputStream fis;

		try {
			fis = new FileInputStream(path);
			this.properties.load(fis);
		} catch (FileNotFoundException e) {
			SeaWars.getLogger().error("Video settings file not found", e);
			return;
		} catch (IOException e) {
			SeaWars.getLogger().error("Error occurred while loading video settings", e);
			return;
		}
	}

	@Override
	public void reload() {
		this.load();
		this.width = Integer.parseInt(this.properties.getProperty("screen_width", "1280"));
		this.height = Integer.parseInt(this.properties.getProperty("screen_height", "720"));
		this.framesLimit = Integer.parseInt(this.properties.getProperty("fps_limit", "60"));
		this.save();
	}

	@Override
	public void save() {
		try {
			if (!path.exists()) {
				path.getParentFile().mkdirs();
				path.createNewFile();
			}

			this.properties.setProperty("screen_width", String.valueOf(width));
			this.properties.setProperty("screen_height", String.valueOf(height));
			this.properties.setProperty("fps_limit", String.valueOf(framesLimit));
			this.properties.store(new FileOutputStream(path), "Video settings file of Sea Wars");
		} catch (IOException e) {
			SeaWars.getLogger().error("Unnable to save video settings file");
		}
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
