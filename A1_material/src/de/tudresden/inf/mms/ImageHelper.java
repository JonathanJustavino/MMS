package de.tudresden.inf.mms;

/**
 * Benötigte Hilfsfunktionen und -konstrukte. <br>
 * <strong>Nicht bearbeiten!</strong>
 */
public class ImageHelper {

	
	/**
	 * 8x8-Bayer-Matrix für Ordered Dithering (siehe Aufgabe 1.3)
	 */
	public static int[][] BAYER8x8 = { { 0, 32, 8, 40, 2, 34, 10, 42 },
			{ 48, 16, 56, 24, 50, 18, 58, 26 },
			{ 12, 44, 4, 36, 14, 46, 6, 38 },
			{ 60, 28, 52, 20, 62, 30, 54, 22 },
			{ 3, 35, 11, 43, 1, 33, 9, 41 },
			{ 51, 19, 59, 27, 49, 17, 57, 25 },
			{ 15, 47, 7, 39, 13, 45, 5, 37 },
			{ 63, 31, 55, 23, 61, 29, 53, 21 } };

	/**
	 * Vorgegebene Palette von 16 Farben (siehe Aufgabe 1.3).
	 */
	public static int[][] palette = { { 14, 32, 11 }, { 101, 146, 37 },
			{ 40, 78, 25 }, { 127, 93, 109 }, { 190, 152, 161 },
			{ 197, 99, 49 }, { 201, 21, 35 }, { 252, 206, 39 },
			{ 69, 110, 24 }, { 185, 207, 164 }, { 122, 159, 86 },
			{ 246, 233, 242 }, { 232, 169, 202 }, { 217, 202, 99 },
			{ 203, 97, 132 }, { 199, 46, 87 } };

	/**
	 * Umrechnung eines RGB-Integer-Wertes in ein RGB-Integer-Tripel.
	 * 
	 * @param rgb
	 *            Integer-Wert der RGB-Farbe
	 * @return Array aus drei Integer-Werten [0..255] für Rot, Grün und Blau
	 * @see java.awt.image.BufferedImage#getRGB(int, int)
	 */
	public static int[] toRGBArray(int rgb) {
		int[] rgbArray = new int[3];
		rgbArray[0] = ((rgb & 0x00ff0000) >> 16);
		rgbArray[1] = ((rgb & 0x0000ff00) >> 8);
		rgbArray[2] = ((rgb & 0x000000ff));

		return rgbArray;
	}

	/**
	 * Umrechnung eines RGB-Integer-Tripel in einen RGB-Integer-Wert.
	 * 
	 * @param rgbArray
	 *            Array aus drei Integer-Werten [0..255] für Rot, Grün und Blau
	 * @return Integer-Wert der RGB-Farbe
	 * @see java.awt.image.BufferedImage#setRGB(int, int, int)
	 */
	public static int toIntRGB(int[] rgbArray) {

		rgbArray[0] = (rgbArray[0] < 0) ? 0 : rgbArray[0];
		rgbArray[0] = (rgbArray[0] > 255) ? 255 : rgbArray[0];
		rgbArray[1] = (rgbArray[1] < 0) ? 0 : rgbArray[1];
		rgbArray[1] = (rgbArray[1] > 255) ? 255 : rgbArray[1];
		rgbArray[2] = (rgbArray[2] < 0) ? 0 : rgbArray[2];
		rgbArray[2] = (rgbArray[2] > 255) ? 255 : rgbArray[2];

		return ((int) (rgbArray[0] << 16) & 0x00ff0000)
				| ((int) (rgbArray[1] << 8) & 0x0000ff00)
				| ((int) (rgbArray[2]) & 0x000000ff);

	}

	/**
	 * Liefert die zu einem RGB-Wert (Integer-Tripel) ähnlichste Farbe aus
	 * {@link ImageHelper#palette}.
	 * 
	 * @param value
	 *            Eingabefarbwert
	 * @return RGB-Wert der ähnlichsten Farbe der Palette
	 */
	public static int[] getColorFromPalette(int[] value) {
		int distance;
		int minDist = 255;
		int[] newValue = new int[3];

		for (int i = 0; i < palette.length; i++) {
			distance = (int) Math.sqrt(Math.abs(value[0] - palette[i][0])
					* Math.abs(value[0] - palette[i][0])
					+ Math.abs(value[1] - palette[i][1])
					* Math.abs(value[1] - palette[i][1])
					+ Math.abs(value[2] - palette[i][2])
					* Math.abs(value[2] - palette[i][2]));
			if (minDist >= distance) {
				minDist = distance;
				newValue[0] = palette[i][0];
				newValue[1] = palette[i][1];
				newValue[2] = palette[i][2];
			}
		}
		return newValue;
	}

}
