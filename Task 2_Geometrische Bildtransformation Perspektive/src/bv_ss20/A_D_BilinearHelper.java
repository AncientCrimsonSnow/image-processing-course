package bv_ss20;

public class A_D_BilinearHelper implements Comparable<A_D_BilinearHelper>{
	//the position in the Array
	public int position;
	//the distance to the real position
	private double distance;
	//if the postion exits
	public boolean posExits;	
	public void setDistance(double distance) {
		this.distance = distance;

	}

	public  double getDistance() {
		return distance;
	}
	@Override
	public int compareTo(A_D_BilinearHelper anotherA_D_BilinearHelper) {
		return Double.compare(position, anotherA_D_BilinearHelper.getDistance());
	}
	
}
