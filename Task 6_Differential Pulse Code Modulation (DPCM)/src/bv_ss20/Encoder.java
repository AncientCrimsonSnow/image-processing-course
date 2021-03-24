package bv_ss20;

public class Encoder {
	public RasterImage image;
	public int[] errorCode;
	
	
	public Encoder(RasterImage image, String predictor) {
		this.image = image;
		createErrorCode(predictor);
		applyErrorCode(errorCode);
	}
	private void createErrorCode(String predictor) {
		errorCode = new int[image.argb.length];
		
		for(int i = 0; i < image.argb.length; i++) {
			errorCode[i] = (image.argb[i] & 0xff) - getPrediction(predictor, i);
		}
		
	}
	public int getPrediction(String predictor,int pos) {
		switch(predictor) {
		case "A (horizontal)":
			return new PredictorA(image, pos).getPrediction();
		case "B (vertikal)":
			return new PredictorB(image, pos).getPrediction();
		case "C (diagonal)":
			return new PredictorC(image, pos).getPrediction();
		case "A+B-C":
			return new PredictorABC(image, pos).getPrediction();
		case "adaptive":
			return new PredictorAdaptiv(image, pos).getPrediction();
		default:
			return Integer.MIN_VALUE;
		}
	}
	public void applyErrorCode(int [] errorCode) {
		image.argb = convertGrayToARGB(errorCode);
	}
	private int[] convertGrayToARGB(int[] greyArray) {
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
