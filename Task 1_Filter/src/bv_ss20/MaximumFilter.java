package bv_ss20;

public class MaximumFilter implements Filter {

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
		for(int x = 0; x<imageWidth;x++) {
			for(int y = 0; y<imageHeight;y++) {
				
				//get the current position:
				int pos = y*imageWidth + x;
				myKernel.setPos(pos);
				//now searching for the maximum
				int r = 255;
				int max = 0;
				int kernel_pos;
				
				for(int x_K = 0; x_K<kernelWidth; x_K++) {
					for(int y_K = 0; y_K<kernelHeight; y_K++) {
						kernel_pos = myKernel.positions[x_K][y_K];
						
						if(kernel_pos < 0) {
							continue;
						}

						r = (sourceImage.argb[kernel_pos] >> 16) & 0xff;

						if(r > max) {
							max  = r;
						}
					}
				}
				//now set the minimum as Value
				destinationImage.argb[pos] = (0xFF<<24) | (max<<16) | (max<<8) | max;
			}
		}
	}
}
