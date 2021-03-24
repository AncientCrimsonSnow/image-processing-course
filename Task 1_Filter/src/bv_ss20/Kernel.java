package bv_ss20;

public class Kernel {
	private int kernelHeight;
	private int kernelWidth;
	private int imageHeight;
	private int imageWidth;
	
	public int getKernelHeight() {
		return kernelHeight;
	}
	public void setKernelHeight(int kernelHeight) {
		this.kernelHeight = kernelHeight;
	}
	public int getKernelWidth() {
		return kernelWidth;
	}
	public void setKernelWidth(int kernelWidth) {
		this.kernelWidth = kernelWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	private int pos;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		
		this.pos = pos;
		
		positions = new int[kernelWidth][kernelHeight];
		
		int rx = kernelWidth/2;
		int ry = kernelHeight/2;
		for(int y = 0; y<kernelHeight; y++) {
			for(int x = 0; x<kernelWidth; x++) {
				int ydiff = y-ry;
				int xdiff = x-rx;

				positions[x][y] = pos + ((ydiff * imageWidth) + xdiff);
				
				//position is higher than the max amount of pixel
				if(positions[x][y] > (imageHeight*imageWidth) - 1)
					positions[x][y] = -1;
				//position is under 0
				else if(positions[x][y] < 0)
					positions[x][y] = -2;
				//position is over right border
				else if(positions[x][y]%imageWidth == 0 && xdiff > 0) {
					int i = 0;
					for(; x+i <kernelWidth;i++) {
						positions[x+i][y] = -3;
					}
					x+=i;
				}
				//position is over left border
				else if((positions[x][y]+1)%imageWidth == 0 && xdiff < 0) {
					int i = 0;
					for(;x-i >=0;i++) {
						positions[x-i][y] = -4;
					}
				}
			}
		}
			
	}
	public int[][] positions;
	
	public Kernel(int kernelHeight, int kernelWidth, int imageHeight, int imageWidth) {
		this.kernelHeight = kernelHeight;
		this.kernelWidth = kernelWidth;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		setPos(0);
		
		positions = new int[kernelWidth][kernelHeight];
	}
	public Kernel(int kernelHeight, int kernelWidth, int imageHeight, int imageWidth, int pos) {
		this.kernelHeight = kernelHeight;
		this.kernelWidth = kernelWidth;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		setPos(pos);
		positions = new int[kernelWidth][kernelHeight];
	}
	public String toString() {
		String output ="Kernel: " + kernelWidth +"x" + kernelHeight + "\n";
		for(int y = 0; y<kernelWidth; y++) {
			for(int x = 0; x<kernelHeight; x++) {
				output += positions[x][y] + " "; 
			}
			output += "\n";
		}
		return output;
	}
}
