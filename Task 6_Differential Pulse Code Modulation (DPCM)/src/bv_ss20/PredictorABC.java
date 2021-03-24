package bv_ss20;

public class PredictorABC implements Predictor{

	private RasterImage image;
	private int pos;

	public PredictorABC(RasterImage image, int pos) {
		setImage(image);
		this.pos = pos;
	}
	
	@Override
	public void setImage(RasterImage image) {
		this.image = image;
	}

	@Override
	public int getPrediction() {
		int y = (pos-1)/image.width;
		int x = pos-(y*image.width);
		if(y == 0 || x == 0) {
			return 128;
		}
		int A = image.argb[pos-1] & 0xff;
		int B = image.argb[pos-image.width] & 0xff;
		int C = image.argb[pos-image.width-1] & 0xff;
		return A+B-C;	
	}
}

