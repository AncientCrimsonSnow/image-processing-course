// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2019 by Klaus Jung
// All rights reserved.
// Date: 2019-05-12

package bv_ss20;

import bv_ss20.ImageAnalysisAppController.StatsProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Histogram {

	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    private int maxHeight;
    
    private int[] histogram = new int[grayLevels];

	public Histogram(GraphicsContext gc, int maxHeight) {
		this.gc = gc;
		this.maxHeight = maxHeight;
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
		System.out.println(factor);
		gc.setStroke(Color.BLACK);
		
		for(int x = 0; x <255;x++) {
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
