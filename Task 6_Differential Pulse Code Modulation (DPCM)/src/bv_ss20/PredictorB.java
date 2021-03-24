package bv_ss20;

public class PredictorB implements Predictor{

	private RasterImage image;
	private int pos;

	public PredictorB(RasterImage image, int pos) {
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
		return image.argb[pos-image.width] & 0xff;	
	}
}

