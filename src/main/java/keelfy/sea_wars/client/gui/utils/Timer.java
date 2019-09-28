package keelfy.sea_wars.client.gui.utils;

/**
 * @author keelfy
 */
public class Timer {

	public static double getTime() {
		return (double) System.nanoTime() / (double) 1000000000L;
	}

}
