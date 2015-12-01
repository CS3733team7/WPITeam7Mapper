package edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.owasp.html.*;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Map;

/**
 * Created by jules on 11/30/2015.
 */
public class MapDevToolPane extends VBox {
	
	Display currentDisplay;
	Map currentMap;
	
	MainPane mainPane;
	HashMap<String,Display> displayList;
	
	@FXML ChoiceBox<String> displayChoiceBox;
	@FXML TextField nameTextField;
	@FXML TextField xTextField;
	@FXML TextField yTextField;
	@FXML TextField zTextField;
	@FXML TextField rotationTextField;
	@FXML TextField scaleTextField;
	@FXML Label pathLabel;
	@FXML Button changeImageButton;
	
	PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);


    public MapDevToolPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/menupanes/devtoolspanes/MapDevToolPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            setListeners();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
    }
    
    private void setListeners(){
    	this.nameTextField.setOnAction(e -> {
    		String old_s = currentMap.getName();
    		if(old_s == null) old_s = currentMap.getImgUrl();
    		String s = policy.sanitize(nameTextField.getText());
    		currentMap.setName(s);
    		updateDisplayList(old_s, s);
    	});
    	
    	this.rotationTextField.setOnAction(e -> {
    		String s = policy.sanitize(rotationTextField.getText());
    		currentMap.setRotation(Float.parseFloat(s));
    		String name = (currentMap.getName() != null) ?  currentMap.getName(): currentMap.getImgUrl();
    		Display d = displayList.get(name);
    		d.setMap(currentMap);
    		displayList.put(name, d);
    	});
    	
    	this.scaleTextField.setOnAction(e -> {
    		String s = policy.sanitize(scaleTextField.getText());
    		currentMap.setScale(Float.parseFloat(s));
    		String name = (currentMap.getName() != null) ?  currentMap.getName(): currentMap.getImgUrl();
    		Display d = displayList.get(name);
    		d.setMap(currentMap);
    		displayList.put(name, d);
    	});
    	
    	this.xTextField.setOnAction(e -> {
    		String s = policy.sanitize(xTextField.getText());
    		Coordinate currentc = currentMap.getCenter();
    		currentMap.setCenter(new Coordinate(Float.parseFloat(s),
    				currentc.getY(), currentc.getZ()));
    		String name = (currentMap.getName() != null) ?  currentMap.getName(): currentMap.getImgUrl();
    		Display d = displayList.get(name);
    		d.setMap(currentMap);
    		displayList.put(name, d);
    	});
    	
    	this.yTextField.setOnAction(e -> {
    		String s = policy.sanitize(yTextField.getText());
    		Coordinate currentc = currentMap.getCenter();
    		currentMap.setCenter(new Coordinate(currentc.getX(),
    				Float.parseFloat(s), currentc.getZ()));
    		String name = (currentMap.getName() != null) ?  currentMap.getName(): currentMap.getImgUrl();
    		Display d = displayList.get(name);
    		d.setMap(currentMap);
    		displayList.put(name, d);
    	});
    	
    	this.zTextField.setOnAction(e -> {
    		String s = policy.sanitize(zTextField.getText());
    		Coordinate currentc = currentMap.getCenter();
    		currentMap.setCenter(new Coordinate(currentc.getX(),
    				currentc.getY(), Float.parseFloat(s)));
    		String name = (currentMap.getName() != null) ?  currentMap.getName(): currentMap.getImgUrl();
    		Display d = displayList.get(name);
    		d.setMap(currentMap);
    		displayList.put(name, d);
    	});
    }
    
    public void setMainPane(MainPane m) { 
    	mainPane = m; 
    	this.currentMap = mainPane.getMapRootPane().getDisplay().getMap(); 
    	displayList = mainPane.getMapRootPane().getAllDisplays();
    	displayChoiceBox.getItems().addAll(displayList.keySet());
    }
    
    public void setMap(Map m) { 
    	this.currentMap = m;
    	if(m.getName() != null) this.nameTextField.setText(m.getName());
    	else this.nameTextField.clear();
    	this.xTextField.setText(Float.toString(m.getCenter().getX()));
    	this.yTextField.setText(Float.toString(m.getCenter().getY()));
    	this.zTextField.setText(Float.toString(m.getCenter().getZ()));
    	this.scaleTextField.setText(Float.toString(m.getScale()));
    	this.rotationTextField.setText(Float.toString(m.getRotation()));
    	this.pathLabel.setText(m.getImgUrl());
    }
    
    @FXML public void changeImage(){
		Display display = mainPane.getMapRootPane().getDisplay();
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(
                                                 new ExtensionFilter("Image Files", "*.png"),
                                                 new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(mainPane.getWindow());
        if (selectedFile != null) {
            String inpath = selectedFile.getName();
            System.out.println(inpath);
            Map newmap = new Map();
            newmap.setImgUrl(inpath);
            display.setMap(newmap);
            mainPane.getMapRootPane().updateDisplay(display, "NEW");
            //mapView.setImage(new Image("/edu/wpi/off/by/one/errors/code/resources/" + inpath));
            //window.display(selectedFile);
        }
    }
    
    @FXML public void changeDisplay(){
    	String k = displayChoiceBox.getSelectionModel().getSelectedItem();
    	this.currentDisplay = displayList.get(k);
    	setMap(this.currentDisplay.getMap());
    	//send msg to maprootpane to change display
    	mainPane.getMapRootPane().updateDisplay(this.currentDisplay, "NEW");
    }
    
    private void updateDisplayList(String old_s, String new_s){
    	Display disp = displayList.get(old_s);
		disp.setMap(this.currentMap);
		displayList.put(new_s, disp);
		displayList.remove(old_s);
		int i = displayChoiceBox.getItems().indexOf(old_s);
		displayChoiceBox.getItems().set(i, new_s);
    }
}
