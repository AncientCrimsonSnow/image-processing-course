// BV Ue1 SS2020 Vorgabe
//
// Copyright (C) 2019-2020 by Klaus Jung
// All rights reserved.
// Date: 2020-04-08

package bv_ss20;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {
	
	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}
	
	public RasterImage(RasterImage src) {
		// copy constructor
		this.width = src.width;
		this.height = src.height;
		argb = src.argb.clone();
	}
	
	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}
	
	
	// image point operations to be added here
	
	public void convertToGray() {
		// TODO: convert the image to grayscale
		for(int y = 0; y<height; y++) {
			for(int x = 0; x<width; x++) {
				
				//get the current position:
				int pos = y*width + x;
				
				//Get the current pixelvalue:
				int currentARGB = argb[pos];
				
				int r = (currentARGB >> 16) & 0xff;
				int g = (currentARGB >>  8) & 0xff;
				int b =  currentARGB        & 0xff;
				
				RGB currentPixel_RGB = new RGB(r,g,b);
				//safe our pixelvalue in YUV
				YUV currentPixel_YUV = currentPixel_RGB.transformToYUV();
				//in YUV the colors are safed in U and in V so if we set them to 0 we get our greyscale pixelvalue
				currentPixel_YUV.setU(0);
				currentPixel_YUV.setV(0);
				
				//convert is now Back:
				currentPixel_RGB = currentPixel_YUV.transformToRGB();
				
				//now set the pixel:
				argb[pos] = (0xFF<<24) | (currentPixel_RGB.getR()<<16) | (currentPixel_RGB.getG()<<8) | currentPixel_RGB.getB();
			}
		}
	}
}
