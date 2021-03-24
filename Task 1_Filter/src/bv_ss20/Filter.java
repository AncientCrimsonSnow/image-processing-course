// BV Ue1 SS2020 Vorgabe
//
// Copyright (C) 2019-2020 by Klaus Jung
// All rights reserved.
// Date: 2020-04-08

package bv_ss20;

public interface Filter {

	public void setSourceImage(RasterImage sourceImage);

	public void setDestinationImage(RasterImage destinationImage);

	public void setKernelWidth(int kernelWidth);

	public void setKernelHeight(int kernelHeight);

	public void apply();
	
}
