// BV Ue3 WS2019/20 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ss20;

import java.util.ArrayList;
import java.util.HashMap;

public class MorphologicFilter {
	
	public enum FilterType { 
		DILATION("Dilation"),
		EROSION("Erosion");
		
		private final String name;       
	    private FilterType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	// filter implementations go here:
	
	public void copy(RasterImage src, RasterImage dst) {
		// TODO: just copy the image
		dst.argb = src.argb;
	}
	
	public void dilation(RasterImage src, RasterImage dst, double radius) {
		// TODO: dilate the image using a structure element that is a neighborhood with the given radius
		
		ArrayList<Dilation_Helper> H = new ArrayList<Dilation_Helper>();
		
		double radius_P2 = Math.pow(radius, 2);
			
		//x^2 + x^2 = r^2
		//-+x = -+r*0.707
		int min_I_J = (int)(-radius*0.707);
		int max_I_J = (int)Math.ceil(radius*0.707);
		
		for(int j = min_I_J; j <= max_I_J; j++) {
			for(int i = min_I_J; i<= max_I_J; i++) {
				Dilation_Helper temp = new Dilation_Helper();
				temp.i = i;
				temp.j = j;
				if(Math.pow(i, 2) + Math.pow(j, 2) <= radius_P2) {
					temp.isclose = true;
				}
				else {
					temp.isclose = false;
				}
				H.add(temp);
			}
		}
		for(int y = 0; y < src.height; y++) {
			for(int x = 0; x < src.width; x++) {
				dst.argb[pos(x, y, dst.width)] = 0xffffffff;
			}
		}
		for(int y = 0; y < src.height; y++) {
			for(int x = 0; x < src.width; x++) {
				if(src.argb[pos(x,y,src.width)] == 0xFF000000) {
					for(int k = 0; k < H.size(); k++) {
						Dilation_Helper temp = H.get(k);
						int x_i = x+temp.i;
						int y_j = y+temp.j;
						
						if(	x_i < 0 			||
								x_i > src.width-1	||
								y_j < 0				||
								y_j > src.height-1)
								continue;
						
						if(temp.isclose) {
							dst.argb[pos(x_i, y_j, dst.width)] = 0xff000000;
						}
					}
				}
			}
		}
	}

	public void erosion(RasterImage src, RasterImage dst, double radius) {
		// TODO: erode the image using a structure element that is a neighborhood with the given radius
		src.invert();
		dilation(src, dst, radius);
		dst.invert();
		src.invert();
	}
	private int pos(int x, int y, int width) {
		return y*width+x;
	}

}
