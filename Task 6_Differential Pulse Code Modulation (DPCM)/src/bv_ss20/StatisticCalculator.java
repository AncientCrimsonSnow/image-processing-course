package bv_ss20;

public interface StatisticCalculator {

	public static double getEntropy(int[] argbInput) {
		double [] p = new double[argbInput.length];
		double entropy = 0;
		int[] grayLevels = getFrequencyDistribution(argbInput);
		
		for(int i = 0; i < grayLevels.length; i++) {
			p[i] = (double)grayLevels[i]/(double)argbInput.length;
			if(p[i] != 0) {
				entropy += p[i] * (Math.log10(p[i])/Math.log10(2));	
			}
		}
		return Math.round(-entropy*100.0)/100.0;
	}
	public static int[] getFrequencyDistribution(int[] argbInput) {
		int[] grayLevels = new int[256];
		
		for(int i = 0; i<grayLevels.length; i++) {
			grayLevels[i] = 0;
		}
		
		for(int i = 0; i<argbInput.length; i++) {
			int tempgray = argbInput[i] & 0xff;
			grayLevels[tempgray] = grayLevels[tempgray]+1;
		}
		return grayLevels;
	}
	public static double MSE(int[] argbInput) {
		double mean = getMean(argbInput);
		double MSE = 0;
		for(int i = 0; i<argbInput.length; i++) {
			MSE += Math.pow(((argbInput[i] & 0xff)-mean), 2)/argbInput.length;
			//System.out.println(MSE);
		}
		return MSE;
	}
	public static double getMean(int[] argbInput) {
		double mean = 0;
		int[] greyLevels = getFrequencyDistribution(argbInput);
		for(int i = 0; i<greyLevels.length;i++) {
			mean += greyLevels[i]*i;
		}
		mean = mean/(double)argbInput.length;
		return mean;
	}
}
