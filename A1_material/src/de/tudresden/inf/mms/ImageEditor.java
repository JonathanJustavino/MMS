package de.tudresden.inf.mms;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author <Jonathan> <Justavino Lüderitz>, <4102359>
 * 
 */
public class ImageEditor {

	/**
	 * Originalbild (Ausgangspunkt für Berechnungen)
	 */
	private BufferedImage image;

	/**
	 * Temporäre Kopie bzw. Ergebnisbild
	 */
	private BufferedImage tmpImg;

	public ImageEditor(BufferedImage image) {
		super();
		this.image = image;
		tmpImg = new BufferedImage(image.getWidth(), image.getHeight(),
				image.getType());
	}

	/**
	 * Konvertiert das vorgegebene RGB-Bild ({@link ImageEditor#image}) in ein
	 * Graustufenbild.
	 * 
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image grayscale() {
		int gray;

		int[] rgb;

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				gray = (int) Math.floor((rgb[0] + rgb[1] + rgb[2]) / 3);
				rgb[0] = gray;
				rgb[1] = gray;
				rgb[2] = gray;
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
			}
		}

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image invert() {

		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				rgb[0] = 255 - rgb[0];
				rgb[1] = 255 - rgb[1];
				rgb[2] = 255 - rgb[2];
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
			}
		}
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image rotate() {

		AffineTransform transfrom = new AffineTransform();
		transfrom.translate(image.getWidth()/2, image.getHeight()/2);
		transfrom.rotate(Math.toRadians(180));
		transfrom.translate(-image.getWidth()/2, -image.getHeight()/2);

		Graphics2D graph2D = tmpImg.createGraphics();
		
		graph2D.drawImage(image, transfrom, null);
		
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.3
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image linHist() {

		int[]rgb;
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				
				double ys = Math.floor(0.299 * rgb[0]) + (0.587 * rgb[1]) +(0.114 * rgb[2]);
				double cb = Math.floor(128 - (0.168736 * rgb[0]) -(0.331264 * rgb[1]) + (0.5 * rgb[2]));
				double cr = Math.floor(128 + (0.5 * rgb[0]) - (0.418688 * rgb[1]) - (0.081312 * rgb[2]));
				
				rgb[0] = (int)(ys + 1.402 * (cr - 128));
				rgb[1] = (int)(ys - 0.34414 * (cb - 128) - 0.71414 * (cr -128));
				rgb[2] = (int)(ys + 1.772 * (cb - 128));
				
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
			}
		}

		return tmpImg;
	}
	
	/**
	 * @see Aufgabe 1.2.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image subsampling() {

		/*
		 * ToDo
		 */
		
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.2.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image colorSubsampling() {

		/*
		 * ToDo
		 */
		
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image colorQuant() {

		/*
		 * ToDo
		 */

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image orderedDithering() {

		/*
		 * ToDo
		 */

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.3
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image floydSteinbergDithering() {

		/*
		 * ToDo
		 */

		return tmpImg;
	}

}
