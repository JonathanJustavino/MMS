package de.tudresden.inf.mms;

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

		int[] rgb;
		
		for (int y = 0; y < image.getHeight(); y+=2){
			for (int x = 0; x < image.getWidth(); x+=2){
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
				tmpImg.setRGB(x +1, y, ImageHelper.toIntRGB(rgb));
				tmpImg.setRGB(x, y + 1, ImageHelper.toIntRGB(rgb));
				tmpImg.setRGB(x + 1, y + 1, ImageHelper.toIntRGB(rgb));
			}
		}
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

		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(ImageHelper.getColorFromPalette((rgb))));
			}
		}
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image orderedDithering() {

		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				int[] bayerRGB;
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				bayerRGB = ImageHelper.toRGBArray((ImageHelper.BAYER8x8[x % 8][y % 8]));
				for (int i = 0; i < rgb.length; i++){
					rgb[i] += bayerRGB[i];
				}
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(ImageHelper.getColorFromPalette(rgb)));
			}
		}

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.3
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image floydSteinbergDithering() {

		/*
		 * ToDo
		 * 
		 * for each y
		   for each x
		      oldpixel        := pixel[x][y]
		      newpixel        := find_closest_palette_color (oldpixel)
		      pixel[x][y]     := newpixel
		      quant_error     := oldpixel - newpixel
		      pixel[x+1][y  ] := pixel[x+1][y  ] + quant_error * 7 / 16
		      pixel[x-1][y+1] := pixel[x-1][y+1] + quant_error * 3 / 16
		      pixel[x  ][y+1] := pixel[x  ][y+1] + quant_error * 5 / 16
		      pixel[x+1][y+1] := pixel[x+1][y+1] + quant_error * 1 / 16
		 * 
		 */
		
		int rgb[];

		for (int y = 0; y < image.getHeight(); y++){
			for (int x = 0; x < image.getWidth(); x++){
				
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				int[] new_pixel = ImageHelper.getColorFromPalette(rgb);
				
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(new_pixel));
				int[] quant_error = new int[3];
				for (int i = 0; i < new_pixel.length; i++){
					quant_error[i] = rgb[i] - new_pixel[i];
				}
				
				tmpImg.setRGB(x + 1, y, ImageHelper.toIntRGB(addElements(ImageHelper.toRGBArray(image.getRGB(x + 1, y)), multiply_factor_to_array(quant_error, (7/16)))));
				tmpImg.setRGB(x - 1, y + 1, ImageHelper.toIntRGB(addElements(ImageHelper.toRGBArray(image.getRGB(x - 1, y + 1)), multiply_factor_to_array(quant_error, (3/16)))));
				tmpImg.setRGB(x, y + 1, ImageHelper.toIntRGB(addElements(ImageHelper.toRGBArray(image.getRGB(x, y + 1)), multiply_factor_to_array(quant_error, (5/16)))));
				tmpImg.setRGB(x + 1, y +1 , ImageHelper.toIntRGB(addElements(ImageHelper.toRGBArray(image.getRGB(x + 1, y + 1)), multiply_factor_to_array(quant_error, (1/16)))));
			}
		}
		
		return tmpImg;
	}
	
	public static int[] addElements(int[] x, int[] y){
		int z[] = new int[x.length];
		for (int i = 0; i < x.length; i++){
			z[i] = x[i] + y[i];
		}
		return z;
	}
	
	public static int[] multiply_factor_to_array(int[] x, int factor){
		for (int i = 0; i < x.length; i++){
			x[i] *= factor;
		}
		return x;
	}

}
