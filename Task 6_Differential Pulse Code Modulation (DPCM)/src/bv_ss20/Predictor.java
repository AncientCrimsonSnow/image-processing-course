package bv_ss20;

public interface Predictor {
	
	public void setImage(RasterImage sourceImage);
	public int getPrediction();
	public static int pos(int x, int y, int width) {
		return y*width+x;
	}	
	public static int[] convertGrayToARGB(int[] greyArray) {
		int[] argb = new int[greyArray.length];
		for(int i = 0; i < argb.length; i++) {
    		int grayValue = makeVisual(greyArray[i]);

    		argb[i] = (0xFF<<24) | (grayValue<<16) | (grayValue<<8) | grayValue;
		}
		return argb;
	}
	private static int makeVisual(int grayValue_Error) {
		grayValue_Error = grayValue_Error + 128;
		if(grayValue_Error>255) {
			grayValue_Error = 255;
		}
		if(grayValue_Error<0) {
			grayValue_Error = 0;
		}
		return grayValue_Error;
	}
}
