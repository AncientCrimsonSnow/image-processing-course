// BV Ue1 SS2020 Vorgabe
//
// Copyright (C) 2019-2020 by Klaus Jung
// All rights reserved.
// Date: 2020-04-08

package bv_ss20;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class PredictorAppController {
	private static final String initialFileName = "test1.jpg";
	private static File fileOpenPath = new File(".");
	
	public enum PredictorType { 
		A("A (horizontal)"), 
		B("B (vertikal)"),
		C("C (diagonal)"),
		ABC("A+B-C"),
		adaptiv("adaptive");
		
		private final String name;       
	    private PredictorType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	private int quantization;
	
    @FXML
    private ImageView originalImageView;

    @FXML
    private ImageView predictionErrorImageView;

    @FXML
    private ImageView reconstructedImageView;
    
    @FXML
    private Label entropyLabel1;
    
    @FXML
    private Label entropyLabel2;
    
    @FXML
    private Label entropyLabel3;
    
    @FXML
    private Label MSELabel;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private ComboBox<PredictorType> predictorSelection;
    
    @FXML
    private Slider quantizationSlider;

    @FXML
    private Label quantizationLabel;
	
	
	@FXML
	public void initialize() {
		// set combo boxes items
		predictorSelection.getItems().addAll(PredictorType.values());
		predictorSelection.setValue(PredictorType.A);
		
		// load and process default image
		RasterImage img = new RasterImage(new File(initialFileName));
		img.convertToGray();
		img.setToView(originalImageView);
		processImages();
	}
	private void processImages() {
		if(originalImageView.getImage() == null)
			return; // no image: nothing to do
		
		long startTime = System.currentTimeMillis();
		
		RasterImage origImg = new RasterImage(originalImageView); 
		
		RasterImage predictionErrorImg = new RasterImage(origImg); 
		Encoder encoder = new Encoder(predictionErrorImg, predictorSelection.getValue().toString());
		
		RasterImage reconstructedImg = new RasterImage(predictionErrorImg);
		Decoder decoder = new Decoder(reconstructedImg, predictorSelection.getValue().toString(), encoder.errorCode);
		
		predictionErrorImg.setToView(predictionErrorImageView);
		reconstructedImg.setToView(reconstructedImageView);
		
		double entropy1 = StatisticCalculator.getEntropy(origImg.argb);
		double entropy2 = StatisticCalculator.getEntropy(predictionErrorImg.argb);
		double entropy3 = StatisticCalculator.getEntropy(reconstructedImg.argb);

		entropyLabel1.setText("Entropy = " + entropy1);
		entropyLabel2.setText("Entropy = " + entropy2);
		entropyLabel3.setText("Entropy = " + entropy3);
		
		double MSE = StatisticCalculator.MSE(reconstructedImg.argb);
		MSELabel.setText("MSE = " + MSE);
		
	   	messageLabel.setText("Processing time: " + (System.currentTimeMillis() - startTime) + " ms");
	}
    @FXML
    void openImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(fileOpenPath); 
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if(selectedFile != null) {
			fileOpenPath = selectedFile.getParentFile();
			RasterImage img = new RasterImage(selectedFile);
			img.convertToGray();
			img.setToView(originalImageView);
	    	processImages();
	    	messageLabel.getScene().getWindow().sizeToScene();;
		}
    }
    @FXML
    void predictorChanged() {
    	processImages();
    }
    @FXML
    void quantizationChanged() {
    	quantization = (int)quantizationSlider.getValue();
    	quantizationLabel.setText("" + quantization);
    	processImages();
    }
}
