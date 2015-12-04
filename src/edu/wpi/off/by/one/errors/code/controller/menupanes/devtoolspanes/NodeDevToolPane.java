package edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.model.*;

/**
 * Created by jules on 11/30/2015.
 */
public class NodeDevToolPane extends VBox {

	MainPane mainPane;
	Display currentDisplay;
	NodeDisplay currentNd;
	@FXML Label nodeIdLabel;
	@FXML TextField xTextField;
	@FXML TextField yTextField;
	@FXML TextField zTextField;
	@FXML TextField tagTextField;
	@FXML ListView<String> tagListView;
	@FXML ListView<Id> edgeListView;
	@FXML Button addTagButton;

	
    public NodeDevToolPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/menupanes/devtoolspanes/NodeDevToolPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
            
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        setListeners();
        //currentDisplay = ControllerSingleton.getInstance().getMapRootPane().getDisplay();
    }
    
    public void displayNodeInfo(NodeDisplay nd){
    	this.currentNd = nd;
    	currentDisplay = ControllerSingleton.getInstance().getMapRootPane().getDisplay();
    	Graph g = currentDisplay.getGraph();
    	Node n = g.returnNodeById(nd.getNode());
    	Coordinate c = g.returnNodeById(nd.getNode()).getCoordinate();
    	nodeIdLabel.setText(nd.getNode().toString());
    	xTextField.setText(Float.toString(c.getX()));
    	yTextField.setText(Float.toString(c.getY()));
    	zTextField.setText(Float.toString(c.getZ()));
    	tagListView.getItems().clear();
    	edgeListView.getItems().clear();
    	tagListView.getItems().addAll((n.GetTags() != null) ? n.GetTags() : new ArrayList<String>());
    	edgeListView.getItems().addAll((n.getEdgelist() != null) ? n.getEdgelist() : new ArrayList<Id>());
    }
    
    private void setListeners(){
    	
    	this.visibleProperty().addListener(e -> {
    		if(this.isVisible()){
    			//mainPane.getMapRootPane().isNodeEditor = true;
    		} else {
    			//mainPane.getMapRootPane().isNodeEditor = false;
    		}
    		
    	});
    	
    	//do sth to adjust node display on map as well
    	this.xTextField.setOnAction(e -> {
    		MapRootPane maproot = ControllerSingleton.getInstance().getMapRootPane();
    		String s = xTextField.getText();
    		Node n = currentDisplay.getGraph().returnNodeById(currentNd.getNode());
    		Coordinate currentc = n.getCoordinate();
    		n.setCoordinate(new Coordinate(Float.parseFloat(s),
    				currentc.getY(), currentc.getZ()));
    		maproot.render();
    	});
    
    	this.yTextField.setOnAction(e -> {
    		MapRootPane maproot = ControllerSingleton.getInstance().getMapRootPane();
    		String s = yTextField.getText();
    		Node n = currentDisplay.getGraph().returnNodeById(currentNd.getNode());
    		Coordinate currentc = n.getCoordinate();
    		n.setCoordinate(new Coordinate(currentc.getX(),
    				Float.parseFloat(s), currentc.getZ()));
    		maproot.render();
    	});
    	
    	this.zTextField.setOnAction(e -> {
    		MapRootPane maproot = ControllerSingleton.getInstance().getMapRootPane();
    		String s = zTextField.getText();
    		Node n = currentDisplay.getGraph().returnNodeById(currentNd.getNode());
    		Coordinate currentc = n.getCoordinate();
    		n.setCoordinate(new Coordinate(currentc.getX(),
    				currentc.getY(), Float.parseFloat(s)));
    		maproot.render();
    	});
    	this.tagTextField.setOnAction(e -> {
    		addTag();
    	});
    	this.addTagButton.setOnAction(e -> {
    		addTag();
    	});
    }
    
    private void addTag(){
    	System.out.println("Adding tag");
    	String s = tagTextField.getText();
    	Node n = currentDisplay.getGraph().returnNodeById(currentNd.getNode());
    	n.addTag(s);
    	tagListView.getItems().clear();
    	tagListView.getItems().addAll((n.GetTags() != null) ? n.GetTags() : new ArrayList<String>());
    }
}
