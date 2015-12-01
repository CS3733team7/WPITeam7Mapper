package edu.wpi.off.by.one.errors.code.controller.menupanes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes.*;
import edu.wpi.off.by.one.errors.code.model.Map;

/**
 * Created by jules on 11/28/2015.
 */
public class DevToolsMenuPane extends BorderPane {
	
	MainPane mainPane;
	
	@FXML RadioButton mapPaneRadioButton;
	@FXML MapDevToolPane mapDevToolPane;
	@FXML NodeDevToolPane nodeDevToolPane;
	@FXML EdgeDevToolPane edgeDevToolPane;
	@FXML PathDevToolPane pathDevToolPane;
	

    public DevToolsMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/DevToolsMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        
        try{
            loader.load();
            setListeners();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        
    }
    
    private void setListeners(){
    	this.visibleProperty().addListener(change -> {
    		Map currentMap = mainPane.getMapRootPane().getDisplay().getMap();
    		mapDevToolPane.setMap(currentMap);
    	});
    	
    }
    public NodeDevToolPane getNodeDevToolPane() { return nodeDevToolPane; }
    public EdgeDevToolPane getEdgeDevToolPane() { return edgeDevToolPane; }
    public PathDevToolPane getPathDevToolPane() { return pathDevToolPane; }
	public void setMainPane(MainPane mainPane) {
		this.mainPane = mainPane;
		mapDevToolPane.setMainPane(mainPane);
		nodeDevToolPane.setMainPane(mainPane);
		//edgeDevToolPane.setMainPane(mainPane);
		//pathDevToolPane.setMainPane(mainPane);
		
	}
    
}
