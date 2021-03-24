// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16

package bv_ss20;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ToneCurve {
	
	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    
    private int brightness = 0;
    private double gamma = 1.0;
    private double contrast = 1.0;
    
    private int[] grayTable = new int[grayLevels];

	public ToneCurve(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public void setBrightness(int brightness) {
		this.brightness = brightness;
		updateTable();
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
		updateTable();
	}
	public void setContrast(double contrast) {
		this.contrast = contrast;
	}

	private void updateTable() {
		// TODO: Fill the grayTable[] array to map gray input values to gray output values.
		// It will be used as follows: grayOut = grayTable[grayIn].
		//
		// Use brightness and gamma values.
		// See "Gammakorrektur" at slide no. 18 of 
		// http://home.htw-berlin.de/~barthel/veranstaltungen/GLDM/vorlesungen/04_GLDM_Bildmanipulation1_Bildpunktoperatoren.pdf
		//
		// First apply the brightness change, afterwards the gamma correction.
		double temp = Math.pow(255.0, 1.0/gamma);
		for(int x = 0; x<grayTable.length;x++) {
			int tempx = x+brightness;
			grayTable[x] = (int) ((255.0*Math.pow(tempx, 1.0/gamma))/temp);
			//Kontraständerung
			RGB rgbx = new RGB(grayTable[x],grayTable[x],grayTable[x]);
			YUV yuvx = rgbx.transformToYUV();
			yuvx = yuvx.changeKontrast(contrast);
			rgbx = yuvx.transformToRGB();
			grayTable[x] = rgbx.getR();
			if(grayTable[x]>255)
				grayTable[x] = 255;
		}
	}
	
	public int mappedGray(int inputGray) {
		return grayTable[inputGray];
	}
	
	public void draw() {
		gc.clearRect(0, 0, grayLevels, grayLevels);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(3);

		// TODO: draw the tone curve into the gc graphic context

		// Remark: This is some dummy code to give you an idea for graphics drawing with pathes	
		for(int x = 0; x<grayLevels-1;x++) {
			gc.beginPath();
			gc.moveTo(grayTable[x],255-x);
			gc.lineTo(grayTable[x+1],255-x+1);
			gc.stroke();
		}
	}

	
}
