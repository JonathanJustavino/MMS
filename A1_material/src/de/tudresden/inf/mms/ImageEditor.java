package de.tudresden.inf.mms;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author <Jonathan> <Justavino L�deritz>, <4102359>
 * 
 */
public class ImageEditor {

	/**
	 * Originalbild (Ausgangspunkt f�r Berechnungen)
	 */
	private BufferedImage image;

	/**
	 * Tempor�re Kopie bzw. Ergebnisbild
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
		
		int[] h = new int[256];
		int[]rgb; 
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
			
				double ys = Math.floor(0.299 * rgb[0]) + (0.587 * rgb[1]) +(0.114 * rgb[2]);
							
				h[(int) ys]++;	
			}
		}
		
		double factor = ((double) 255) / (image.getWidth() * image.getHeight());
		int[] f = new int[256];
		int h_sum = 0;
		for (int a = 0; a < 256; a++){
			h_sum += h[a];
			f[a] = (int)(factor * h_sum);
			
		}
		
		for (int x = 0; x < image.getWidth(); x++){
			for (int y = 0; y < image.getHeight(); y++){
				
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				
				double ys_old = Math.floor(0.299 * rgb[0]) + (0.587 * rgb[1]) +(0.114 * rgb[2]);
				double ys_new = f[(int)ys_old];
				double cb = Math.floor(128 - (0.168736 * rgb[0]) -(0.331264 * rgb[1]) + (0.5 * rgb[2]));
				double cr = Math.floor(128 + (0.5 * rgb[0]) - (0.418688 * rgb[1]) - (0.081312 * rgb[2]));
				
				
				rgb[0] = (int)(ys_new + 1.402 * (cr - 128));
				rgb[1] = (int)(ys_new - 0.34414 * (cb - 128) - 0.71414 * (cr -128));
				rgb[2] = (int)(ys_new + 1.772 * (cb - 128));
				
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


		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixel00 = new int[3];
		int[] pixel01 = new int[3];
		int[] pixel10 = new int[3];
		int[] pixel11 = new int[3];
		int[] rgb_buffer = new int[3];
		
		for (int y = 0; y < height; y+=2){
			for (int x = 0; x < width; x+=2){
				
				to_YCbCr(image.getRGB(x, y), pixel00);
				to_YCbCr(image.getRGB(x + 1, y), pixel01);
				to_YCbCr(image.getRGB(x, y + 1), pixel10);
				to_YCbCr(image.getRGB(x + 1, y + 1), pixel11);
				
				int cb = (pixel00[1] + pixel01[1] + pixel10[1] + pixel11[1]) >> 2;
				int cr = (pixel00[2] + pixel01[2] + pixel10[2] + pixel11[2]) >> 2;
				
				tmpImg.setRGB(x, y, to_rgb(pixel00[0], cb, cr, rgb_buffer));
				tmpImg.setRGB(x + 1, y, to_rgb(pixel01[0], cb, cr, rgb_buffer));
				tmpImg.setRGB(x, y + 1, to_rgb(pixel10[0], cb, cr, rgb_buffer));
				tmpImg.setRGB(x + 1, y + 1, to_rgb(pixel11[0], cb, cr, rgb_buffer));
				
			}
		}
		
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
	
		Graphics g = tmpImg.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		int rgb[];	

		int max_x = image.getWidth() - 1;
		int max_y = image.getHeight() - 1;

		for (int y = 0; y < max_y; y++){
			for (int x = 0; x < max_x; x++){
				
				rgb = ImageHelper.toRGBArray(tmpImg.getRGB(x, y));
				int[] new_pixel = ImageHelper.getColorFromPalette(rgb);			
				
				int quant_error_r = rgb[0] - new_pixel[0];
				int quant_error_g = rgb[1] - new_pixel[1];
				int quant_error_b = rgb[2] - new_pixel[2];
				
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(new_pixel));
				
				if(x < max_x){
					add_quant_error(x + 1, y, quant_error_r, quant_error_g, quant_error_b, 7);
				}
				if(x > 1 && y< max_y){
					add_quant_error(x - 1, y + 1, quant_error_r, quant_error_g, quant_error_b, 3);
				}
				if(y < max_y){
					add_quant_error(x, y + 1, quant_error_r, quant_error_g, quant_error_b, 5);
				}
				if(x < max_x && y < max_y){
					add_quant_error(x + 1, y + 1, quant_error_r, quant_error_g, quant_error_b, 1);
				}
				
			}
		}
		
		return tmpImg;
	}
	
	public void add_quant_error(int x, int y, int quant_error_r, int quant_error_g, int quant_error_b, int quant_weight){
		int[] pixel = ImageHelper.toRGBArray(tmpImg.getRGB(x, y));
		pixel[0] += quant_error_r * quant_weight >> 4;
		pixel[1] += quant_error_g * quant_weight >> 4; 
		pixel[2] += quant_error_b * quant_weight >> 4;
		
		tmpImg.setRGB(x, y, ImageHelper.toIntRGB(pixel));
	}
	
	public void to_YCbCr(int rgb_int, int[] yCbCr){
		
		int[] rgb = ImageHelper.toRGBArray(rgb_int);
		
		yCbCr[0] = (int)(Math.floor(0.299 * rgb[0]) + (0.587 * rgb[1]) +(0.114 * rgb[2]));
		yCbCr[1] = (int)(Math.floor(128 - (0.168736 * rgb[0]) -(0.331264 * rgb[1]) + (0.5 * rgb[2])));
		yCbCr[2] = (int)(Math.floor(128 + (0.5 * rgb[0]) - (0.418688 * rgb[1]) - (0.081312 * rgb[2])));
	}
	
	public int to_rgb(int y, int cb, int cr, int[] rgb){
		
		rgb[0] = (int)(y + 1.402 * (cr - 128));
		rgb[1] = (int)(y - 0.34414 * (cb - 128) - 0.71414 * (cr -128));
		rgb[2] = (int)(y + 1.772 * (cb - 128));
		
		return ImageHelper.toIntRGB(rgb);
	}

}
