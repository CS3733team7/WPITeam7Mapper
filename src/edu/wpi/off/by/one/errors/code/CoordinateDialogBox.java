package edu.wpi.off.by.one.errors.code;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CoordinateDialogBox {
	/**
	 * A coordinate to show, edit and/or save
	 */
	private Coordinate coordinate;
	
	//UI components
	Stage window;
	
	//The root layout
	VBox rootBox;
			
	//layout to hold controls
	HBox textFieldBox;
	HBox buttonBox;
			
	//Controls
	TextField xTextField;
	TextField yTextField;
	TextField zTextField;
	Button okayButton;
	Button cancelButton;
	
	/**
	 * Constructor
	 * @param coordinate
	 */
	public CoordinateDialogBox(Coordinate coordinate){
		this.coordinate = coordinate;
		
		//initialize UI components
		window = new Stage();
		
		rootBox = new VBox();
		
		textFieldBox = new HBox();
		buttonBox = new HBox();
		
		xTextField = new TextField();
		yTextField = new TextField();
		zTextField = new TextField();
		
		okayButton = new Button("Okay");
		cancelButton = new Button("Cancel");
	}
	
	public Coordinate getCoordinate(){
		return coordinate;
	}
	
	/**
	 * Display the coordinate dialog box
	 * @param title
	 */
	public void display(String title){
		
		//making sure events only go to this window.
		window.initModality(Modality.APPLICATION_MODAL);
		
		//set the title of the window
		window.setTitle(title);	
		
		//set the textfield text to the coordinate data
		xTextField.setText(String.valueOf(coordinate.getX()));
		yTextField.setText(String.valueOf(coordinate.getY()));
		zTextField.setText(String.valueOf(coordinate.getZ()));
		
		//Events for the buttons
		okayButton.setOnAction(e -> {
			try{
				float x = Float.parseFloat(xTextField.getText());
				float y = Float.parseFloat(yTextField.getText());
				float z = Float.parseFloat(zTextField.getText());
				
				coordinate.setAll(x, y, z);
				window.close();
			}catch (NumberFormatException nfe){
				
			}
			
		});
		
		cancelButton.setOnAction(e -> window.close());
		
		//adding all the controls and layouts
		rootBox.getChildren().addAll(textFieldBox, buttonBox);
		rootBox.setAlignment(Pos.CENTER);
		textFieldBox.getChildren().addAll(xTextField, yTextField, zTextField);
		textFieldBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(okayButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		
		Scene scene = new Scene(rootBox);
		window.setScene(scene);
		
		//show the window
		window.showAndWait();
		
	}
}
