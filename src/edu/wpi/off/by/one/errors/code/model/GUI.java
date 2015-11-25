package edu.wpi.off.by.one.errors.code.model;
// MY ATTEMPT AT THE GUI, THE DESIGN IS NOT APPEALING AS THIS IS STILL A WORK IN PROGRESS
//I MADE IT JUST TO ENSURE THAT THE BASIC FUNCTIONALITY OF THE UI IS IN ORDER
//THE NEXT ASPECTS THAT NEED TO BE IMPLEMENTED ARE THE BACKEND ALGO'S AND DATA STRUCTURES

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Modality;
//import javafx.scene.control.Separator;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;



public class GUI extends Application {
	
	double x;
	double y;
	
	Label location = new Label("Mouse location");
	Label coord = new Label("coordinates:");
	Label xLabel = new Label("X label:");
	Label yLabel = new Label("Y coord:");
	
	Button newDest = new Button("add Destination");
	Button addNode = new Button("Add Node");
	Button editNode = new Button("Edit Node");
	Button addEdge = new Button("Add Edge");
	Button editEdge = new Button("Edit Edge");
	Button saveNodes = new Button("Save All");
	Button showMap = new Button("Show map");
	Button edit = new Button("Done");
	
	//New code
	//*****************************************
	TextField xField = new TextField();
	TextField yField = new TextField();
	//***************************************
	ImageView imgpic = new ImageView();
	final SimpleDoubleProperty zoomProperty = new SimpleDoubleProperty(200);
	
	@Override
	public void start(Stage primaryStage){
		
		
		showMap.setOnAction(new EventHandler<ActionEvent>(){
			
			Image img = new Image("file:src/images/map.png");
			
			@Override
			public void handle(ActionEvent e){
				imgpic.setImage(img);
			}//end of method handle
		});
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setPadding(new Insets(5.0));
		
		BorderPane root = new BorderPane();
		root.setCenter(imgpic);
		
		//New CODE
//************************************************************************		
		StackPane editbtn = new StackPane();
		editbtn.getChildren().add(edit);
		
		GridPane pane = new GridPane();
		xLabel.setMaxWidth(100);
		yLabel.setMaxWidth(100);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(25.0));
		pane.setMaxWidth(300);
		pane.setMaxHeight(300);
		pane.add(xLabel,1,5);
		pane.add(xField, 2, 5);
		pane.add(yLabel, 1, 6);
		pane.add(yField, 2, 6);
		pane.add(editbtn, 5, 9);
//****************************************************************************		
		
		
// Scrolling capability
		ScrollPane sp = new ScrollPane();
		sp.setContent(root);
		sp.setPannable(true);
		sp.setFitToHeight(true);
		
		
//     ZOOM
//******************************************************************************************************************************************************************		
		//increases the value of the zoomProperty to be added to scroll pane for magnification
		zoomProperty.addListener(new InvalidationListener(){
			@Override
			public void invalidated(Observable arg){
				imgpic.setFitWidth(zoomProperty.get()*4);
				imgpic.setFitHeight(zoomProperty.get()*3);
			}
		});
		
		//adds the scroll pane event filter which increases the magnification
		sp.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>(){
			@Override
			public void handle(ScrollEvent e){
				if(e.getDeltaY() > 0){
					zoomProperty.set(zoomProperty.get()* 1.1);
				}else if(e.getDeltaY() < 0){
					zoomProperty.set(zoomProperty.get()/1.1);
				}
			}
		});
		
//********************************************************************************************************************************************************************	
		
		
		HBox buttons = new HBox();
		
		buttons.getChildren().addAll(newDest,addNode,editNode,addEdge,editEdge,saveNodes,showMap);
		
		grid.add(buttons,0,0);//buttons added to grid
		grid.add(sp, 0, 1); // scroll pane added to grid
		grid.add(location,0,480); //location label added
		grid.add(coord, 0, 500);// coordinates label added
		
				
		Scene mainScene = new Scene(grid, 1000, 1000);
		Scene popup = new Scene(pane,400,400); ///****************************NEW*************
		
		//displays the x and Y coordinates of the mouse click relative to the top-left corner of the map
		imgpic.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				String loc = ("X: "+e.getX()+", Y: "+e.getY());
				coord.setText(loc);
			}
		});
		
		
		
		primaryStage.setTitle("WPI map");
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		//New code
//*****************************************************************************************
		Stage pWindow = new Stage();
		pWindow.setTitle("Edit Node");
		pWindow.initModality(Modality.APPLICATION_MODAL);
		pWindow.setScene(popup);
		
		editNode.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent e){
				pWindow.showAndWait();
			}
		});
		

		edit.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent e){
				x = Integer.parseInt(xField.getText());
				y = Integer.parseInt(yField.getText());
				
				System.out.println(x);
				System.out.println(y);
				
				//add to node coordinates etc
				
				pWindow.close();
//*******************************************************************************************************				
			}//end of handle method
		});

		
	}//end method start
	
	
	public static void main(String[] args){
		launch(args);
	}//

}//end class GUI

