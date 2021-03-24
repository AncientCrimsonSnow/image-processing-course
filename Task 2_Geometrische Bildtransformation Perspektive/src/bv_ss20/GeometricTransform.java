// BV Ue2 SS2020 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ss20;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GeometricTransform {

	public enum InterpolationType { 
		NEAREST("Nearest Neighbour"), 
		BILINEAR("Bilinear");
		
		private final String name;       
	    private InterpolationType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	public void perspective(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion, InterpolationType interpolation) {
		switch(interpolation) {
		case NEAREST:
			perspectiveNearestNeighbour(src, dst, angle, perspectiveDistortion);
			break;
		case BILINEAR:
			perspectiveBilinear(src, dst, angle, perspectiveDistortion);
			break;
		default:
			break;	
		}
		
	}

	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	public void perspectiveNearestNeighbour(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {
		// TODO: implement the geometric transformation using nearest neighbor image rendering

		//source base x,y
		int[] base_src = {
				src.width/2,
				src.height/2
			};
		//destination base x,y
		int[] base_dst = {
				dst.width/2,
				dst.height/2
			};
		for(int y = 0; y<dst.height; y++) {
			
			//the value of y_dst in our coordination-system
			int y_cord_dst = -(y-base_dst[1]);
			
			for(int x = 0; x<dst.width; x++) {
		
				//the value of x_dst in our coordination-system
				int x_cord_dst = x-base_dst[0];
						
				//the value of x_src in our coordination-system
				//the value of y_src in our coordination-system
				//useing the formula
				int x_cord_src = (int) Math.round(x_cord_dst/(-x_cord_dst*Math.sin(Math.toRadians(angle))*perspectiveDistortion + Math.cos(Math.toRadians(angle))));
				int y_cord_src = (int) Math.round(y_cord_dst*Math.sin(Math.toRadians(angle))*perspectiveDistortion * x_cord_src + y_cord_dst);
					
				//converting back
				int x_src = x_cord_src+base_src[0];
				int y_src = base_src[1]-y_cord_src;
				
				//get the current position:
				int pos_dst = y*dst.width + x;
				int pos_src = y_src*src.width + x_src;

				
				if(x_src < 0 || x_src > src.width-1) {
					dst.argb[pos_dst] = 0xFFFFFFFF;
					continue;
				}
				if(y_src < 0 || y_src > src.height-1) {
					dst.argb[pos_dst] = 0xFFFFFFFF;
					continue;
				}
				dst.argb[pos_dst] = src.argb[pos_src];		
			}	
		}		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radians
	}


	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	public void perspectiveBilinear(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {
		// TODO: implement the geometric transformation using bilinear interpolation
				
		//backgroundcolor
		int backgroundColor = 0xFFFFFFFF;
		
		//the value of y_dst in our coordination-system
		double y_cord_dst;
		//the value of x_dst in our coordination-system
		double x_cord_dst;
		
		//real position in src on the coordinationsystem
		double x_cord_src_real;
		double y_cord_src_real;
		
		//real position in src
		double x_src_real;
		double y_src_real;
		
		//round position in src
		int x_src;
		int y_src;
		
		//rounded position in src. we call this point A
		int ax;
		int ay;
		//rounded position in src. we call this point B
		int bx;
		int by;
		//rounded position in src. we call this point c
		int cx;
		int cy;
		//rounded position in src. we call this point d
		int dx;
		int dy;
		
		//position in array in which we are in dst
		int pos_dst;
		
		//source base x,y
		int[] base_src = {
				src.width/2,
				src.height/2
			};
		//destination base x,y
		int[] base_dst = {
				dst.width/2,
				dst.height/2
			};
		
		//safes rgb value
		int rgb;
		double r;
		double g;
		double b;
		for(int y = 0; y<dst.height; y++) {
			
			y_cord_dst = -(y-base_dst[1]);
			
			for(int x = 0; x<dst.width; x++) {
				
				pos_dst = pos(x,y,dst.width);
				
				x_cord_dst = x-base_dst[0];
					
				//the value of x_src in our coordination-system
				//the value of y_src in our coordination-system
				//Using the formal
				
				x_cord_src_real = x_cord_dst/(-x_cord_dst*Math.sin(Math.toRadians(angle))*perspectiveDistortion + Math.cos(Math.toRadians(angle)));
				y_cord_src_real = y_cord_dst*Math.sin(Math.toRadians(angle))*perspectiveDistortion * x_cord_src_real + y_cord_dst;
				
				//round them to 0.00
				x_cord_src_real = Math.round(x_cord_src_real*100.0)/100.0;
				y_cord_src_real = Math.round(y_cord_src_real*100.0)/100.0;
							
				//converting back
				x_src_real = x_cord_src_real+base_src[0];
				y_src_real = base_src[1]-y_cord_src_real;
				

				x_src = (int) Math.round(x_src_real);
				y_src = (int) Math.round(y_src_real);
				
				
				//if the calculated position doesnt exits we turn it white.
				if(	x_src < 0 			||
					x_src > src.width-1	||
					y_src < 0			||
					y_src > src.height-1) {
					dst.argb[pos_dst] = backgroundColor;
					continue;
				}
				
				ax = (int)x_src_real;
				ay = (int)y_src_real;
				
				dx = (int)Math.ceil(x_src_real);
				dy = (int)Math.ceil(y_src_real);
				
				bx = dx;
				by = ay;
				
				cx = ax;
				cy = dy;
				
				double h = x_src_real - ax;
				double v = y_src_real - ay;
	
				RGB A;
				RGB B;
				RGB C;
				RGB D;
				
				
				if(x_src > src.width-2 && y_src > src.height - 2){
					rgb = src.argb[pos(ax, ay, src.width)];
					A = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
					     			 rgb & 0xff);
					B = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					C = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					D = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
				}
				else if(y_src > src.height - 2) {
					rgb = src.argb[pos(ax, ay, src.width)];
					A = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(bx, by, src.width)];
					B = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(ax, ay, src.width)];
					C = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(bx, by, src.width)];
					D = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
				}
				else if(x_src > src.width-2){
					rgb = src.argb[pos(ax, ay, src.width)];
					A = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(ax, ay, src.width)];
					B = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(cx, cy, src.width)];
					C = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(cx, cy, src.width)];
					D = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
				}
				else {
					rgb = src.argb[pos(ax, ay, src.width)];
					A = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(bx, by, src.width)];
					B = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(cx, cy, src.width)];
					C = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
					rgb = src.argb[pos(dx, dy, src.width)];
					D = new RGB(	(rgb >> 16) & 0xff,
									(rgb >> 8) & 0xff,
									 rgb & 0xff);
				}		
				r = A.getR()*(1-h)*(1-v) + B.getR()*h*(1-v) + C.getR()*(1-h)*v + D.getR()*h*v;
				g = A.getG()*(1-h)*(1.0-v) + B.getG()*h*(1-v) + C.getG()*(1-h)*v + D.getG()*h*v;
				b = A.getB()*(1-h)*(1.0-v) + B.getB()*h*(1-v) + C.getB()*(1-h)*v + D.getB()*h*v;
				
				RGB P = new RGB(r, g, b);
				dst.argb[pos(x, y, dst.width)] = (0xff<<24) | (P.getR()<<16) | (P.getG()<<8) | (P.getB());
			}	
		}
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radians
		
 	}
	private int pos(int x, int y, int width) {
		return y*width+x;
	}


}
