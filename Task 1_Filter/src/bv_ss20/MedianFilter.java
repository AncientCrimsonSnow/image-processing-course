package bv_ss20;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MedianFilter implements Filter {

	RasterImage sourceImage;
	RasterImage destinationImage;
	int kernelWidth;
	int kernelHeight;
	@Override
	public void setSourceImage(RasterImage sourceImage) {
		this.sourceImage = sourceImage;	
	}

	@Override
	public void setDestinationImage(RasterImage destinationImage) {
		this.destinationImage = destinationImage;
	}

	@Override
	public void setKernelWidth(int kernelWidth) {
		this.kernelWidth = kernelWidth;
	}

	@Override
	public void setKernelHeight(int kernelHeight) {
		this.kernelHeight = kernelHeight;
	}

	@Override
	public void apply() {
		int imageWidth = sourceImage.width;
		int imageHeight =sourceImage.height;
		
		Kernel myKernel = new Kernel(kernelHeight, kernelWidth, imageHeight, imageWidth);
		for(int y = 0; y<imageWidth;y++) {
			for(int x = 0; x<imageHeight;x++) {
				
				//get the current position:
				int pos = y*imageWidth + x;
				myKernel.setPos(pos);
				//now searching for the median
				int r = 255;
				int kernel_pos;
				ArrayList<Integer> temp = new ArrayList<Integer>();	
				for(int x_K = 0; x_K<kernelWidth; x_K++) {
					for(int y_K = 0; y_K<kernelHeight; y_K++) {
						kernel_pos = myKernel.positions[x_K][y_K];
						
						if(kernel_pos < 0) {
							continue;
						}
						r = (sourceImage.argb[kernel_pos] >> 16) & 0xff;
						temp.add(r);
					}
				}
				//Test purpose
				/*
				if(x == 0) {
					System.out.println("x: " + x + " y: " + y);
					System.out.println(myKernel);
				}
				*/
				int[] tempArray = new int[temp.size()];
				
				for(int i = 0; i<tempArray.length; i++) {
					tempArray[i] = temp.get(i);
				}
				int med = GFG.findMedian(tempArray, tempArray.length);
				//now set the median as Value
				destinationImage.argb[pos] = (0xFF<<24) | (med<<16) | (med<<8) | med;
			}
		}
	}
}
