package keelfy.sea_wars.client.settings;

import java.io.File;

/**
 * @author keelfy
 */
public class SettingsHandler {

	private final File settingsFolder;
	private VideoSettings videoSettings;

	public SettingsHandler(File dataFolder) {
		this.settingsFolder = new File(dataFolder.getAbsoluteFile(), "config");
		this.videoSettings = new VideoSettings(getSettingsFile("video-settings"));
	}

	public void preInit() {
		this.videoSettings.reload();
	}

	public void init() {

	}

	public void postInit() {

	}

	private File getSettingsFile(String name) {
		return new File(settingsFolder, name + ".cfg");
	}

	public VideoSettings getVideo() {
		return videoSettings;
	}
}
