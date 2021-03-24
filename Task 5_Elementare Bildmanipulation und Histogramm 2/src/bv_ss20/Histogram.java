// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2019 by Klaus Jung
// All rights reserved.
// Date: 2019-05-12

package bv_ss20;

import com.sun.scenario.effect.Brightpass;

import bv_ss20.ImageAnalysisAppController.StatsProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.security.provider.EntropySource;

public class Histogram {

	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    private int maxHeight;
    
    private int[] histogram = new int[grayLevels];

	public Histogram(GraphicsContext gc, int maxHeight) {
		this.gc = gc;
		this.maxHeight = maxHeight;
	}
	public double[] autoKontrastCalc(RasterImage image) {
		//sort
		int[] pixelSorted = image.argb;
		
		
		
		
		Quicksort mySort = new Quicksort(pixelSorted);
		pixelSorted = mySort.sort(0, pixelSorted.length-1);
		
		int min = pixelSorted[0] & 0xff;
		int max = pixelSorted[pixelSorted.length-1] & 0xff;
		int mid = (min+max)/2;
		
		int realMid = grayLevels/2;
		//the number of which brightness need to change:
		int diff = realMid - mid;
		
		int newMin = min + diff;
		
		double contrast = 1.0;
		while(newMin != 0) {
			RGB rgbMin = new RGB(newMin, newMin, newMin);
			YUV yuvMin = rgbMin.transformToYUV();
			yuvMin = yuvMin.changeKontrast(contrast);
			rgbMin = yuvMin.transformToRGB();
			newMin = rgbMin.getR();
			contrast += 0.1;
		}
		double[] brightness_x_contrast = {diff,contrast};
			
		return brightness_x_contrast;
	}
	public void update(RasterImage image, Point2D ellipseCenter, Dimension2D ellipseSize, int selectionMax, ObservableList<StatsProperty> statsData) {
		// TODO: calculate histogram[] out of the gray values of the image for pixels inside the given ellipse
		
		for(int i = 0; i<histogram.length;i++) {
			histogram[i] = 0;
		}
		
		for(int y = 0; y < image.height;y++) {
			for(int x = 0; x < image.width;x++) {
				if(isInElipse(x, y, ellipseSize.getWidth()/2, ellipseSize.getHeight()/2, ellipseCenter.getX(), ellipseCenter.getY())) {
					int pos = pos(x, y, image.width);
					int tempgray = image.argb[pos] & 0xff;
					histogram[tempgray] = histogram[tempgray]+1;
				}
			}
		}
		// Remark: Please ignore selectionMax and statsData in Exercise 4. It will be used in Exercise 5.

		//sort
		int[] pixelSorted = image.argb;
		
		
		
		
		Quicksort mySort = new Quicksort(pixelSorted);
		pixelSorted = mySort.sort(0, pixelSorted.length-1);
		
		//real min of GrayLevel we got in our image:
		int realMin = pixelSorted[0] & 0xff;
		
		
		int numberOfPixelMax = image.height * image.width;
		
		if(selectionMax == -1)
			selectionMax = 255;
		int numberOfPixelSelected = 0;
		if(selectionMax != 255)
			for(int i = 0; i <= selectionMax; i++) 
				numberOfPixelSelected += histogram[i];
		else
			numberOfPixelSelected = numberOfPixelMax;
		
		//median
		double median = 0;
		
		if(numberOfPixelSelected != 0) {
			//grade
			if(numberOfPixelSelected%2 == 0) {
				int pos1 = numberOfPixelSelected/2;
				int pos2 = pos1-1;
				median = ((pixelSorted[pos1] & 0xff)+(pixelSorted[pos2] & 0xff))/2.0;
			}
			//ungrade
			else {			
				int pos = numberOfPixelSelected/2;
				median = (pixelSorted[pos] & 0xff);
			}
		}

		
		for(StatsProperty property : statsData) {
			switch(property) {
			
			case Level:	
				property.setValueInPercent((double)numberOfPixelSelected/(double)numberOfPixelMax);
				break;
			
			case Minimum:
				if(realMin<selectionMax)
					property.setValue(realMin);
				else
					property.setValue(0);
				break;
			
			case Maximum:

				if(realMin<selectionMax) {
					int realMax = pixelSorted[numberOfPixelSelected-1] & 0xff;
					property.setValue(realMax);
				}
				else
					property.setValue(0);
				break;
			case Mean:
				
				double mean = 0;
				for(int i = 0; i<=selectionMax;i++) {
					mean += histogram[i]*i;
				}
				mean = mean/(double)numberOfPixelMax;
				property.setValue(mean);
				break;
					
			case Median:
				property.setValue(median);
				break;		
			case Variance:		
				double variance = 0;
				for(int i = 0; i<numberOfPixelSelected; i++) {
					variance += Math.pow(((pixelSorted[i] & 0xff)-median), 2)/numberOfPixelSelected;
				}
				property.setValue(variance);
				break;
			case Entropy:
				double [] p = new double[selectionMax+1];
				for(int i = realMin; i <= selectionMax;i++) {
					p[i] = (double)histogram[i]/(double)numberOfPixelSelected;
				}
				double entropy = 0;
				for(int j = realMin; j <= selectionMax;j++) {
					if(p[j] != 0)
						entropy += p[j] * (Math.log10(p[j])/Math.log10(2));		
				}
				property.setValue(-entropy);
				break;
			}
			
		}
		
		
		
	}
	    
	public void draw() {
		gc.clearRect(0, 0, grayLevels, maxHeight);
		gc.setLineWidth(1);

		// TODO: draw histogram[] into the gc graphic context
		
		// Remark: This is some dummy code to give you an idea for graphics drawing		
		double shift = 0.5;
		// note that we need to add 0.5 to all coordinates to get a one pixel thin line 
		/*
		gc.setStroke(Color.GREEN);
		gc.strokeLine(128 + shift, shift, 128 + shift, maxHeight + shift);
		gc.strokeLine(shift, maxHeight/2 + shift, grayLevels + shift, maxHeight/2 + shift);
		gc.setStroke(Color.ORANGE);
		gc.strokeLine(shift, shift, grayLevels + shift, maxHeight + shift);
		gc.strokeLine(grayLevels + shift, shift, shift, maxHeight + shift);
		*/
		
		int max=1;
		for(int e: histogram) {
			if(max < e)
				max = e;
		}
		double factor = (double)maxHeight/max;
		gc.setStroke(Color.BLACK);	
		for(int x = 0; x <=255;x++) {
			gc.strokeLine(x + shift, maxHeight + shift, x + shift, maxHeight-((double)histogram[x]*factor)+shift);
		}
	}
	private int pos(int x, int y, int width) {
		return y*width+x;
	}
	private boolean isInElipse(int x, int y, double d, double e, double f, double g) {	
		double temp = (Math.pow(x-f,2)/Math.pow(d, 2))+(Math.pow(y-g, 2)/Math.pow(e, 2));
		if(temp <= 1)
			return true;
		else
			return false;
	}
}
