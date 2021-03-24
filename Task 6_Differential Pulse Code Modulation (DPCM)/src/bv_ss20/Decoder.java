package bv_ss20;

public class Decoder {
	public RasterImage image;
	private int[] errorCode;
	
	
	public Decoder(RasterImage image, String predictor, int[] errorCode) {
		this.errorCode = errorCode;
		this.image = image;
		decodeErrorCode(predictor);
	}
	private void decodeErrorCode(String predictor) {
		for(int i = 0; i < image.argb.length; i++) {
			int decodedPixel = getPrediction(predictor, i) + errorCode[i];
			image.argb[i] = (0xFF<<24) | (decodedPixel<<16) | (decodedPixel<<8) | decodedPixel;
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
}
