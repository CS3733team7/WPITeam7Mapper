package edu.wpi.off.by.one.errors.code.controller;

import javafx.fxml.FXML;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes.NodeDevToolPane;


/**
 * The controller that manages the logic for most of the application view:
 * 		- Map
 * 		- Menu
 *		- Directions
 * 
 */
public class MainPane extends BorderPane {
    private BooleanProperty isDevModeOn;


	Window window;

    @FXML private Button openNavigationPaneButton;
	@FXML private ScrollPane mapScrollPane;
	@FXML private MenuPane menuPane;
	@FXML private MapRootPane mapRootPane;
	@FXML private NavigationPane navigationPane;
    public MainPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            mapScrollPane.setHvalue(mapScrollPane.getHmin() + (mapScrollPane.getHmax() - mapScrollPane.getHmin()) / 2);
            mapScrollPane.setVvalue(mapScrollPane.getVmin() + (mapScrollPane.getVmax() - mapScrollPane.getVmin()) / 2);
            mapRootPane.setMainPane(this);
            menuPane.setMainPane(this);
            navigationPane.setMainPane(this);
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }

        addListeners();

        this.getStylesheets().add(getClass().getResource("../resources/stylesheets/MainPaneStyleSheet.css").toExternalForm());
    }

    private void addListeners(){
        openNavigationPaneButton.visibleProperty().bind(navigationPane.visibleProperty().not());

    }

    @FXML private void onOpenNavigationPaneButtonClick(){
        navigationPane.open();
    }
    public void setWindow(Window window) { 
    	this.window = window; 
    	window.heightProperty().addListener(e -> {
    		System.out.println("Scroll Pane Size: " + mapScrollPane.getHeight());
    		mapRootPane.updateCanvasSize(mapScrollPane.getWidth(), mapScrollPane.getHeight());
    	});
    	
    	window.widthProperty().addListener(e -> {
    		System.out.println("Scroll Pane Size: " + mapScrollPane.getWidth());
    		mapRootPane.updateCanvasSize(mapScrollPane.getWidth(), mapScrollPane.getHeight());
    	});
    }
    public Window getWindow() { return this.window; }
    public MenuPane getMenuPane() { return this.menuPane; }
    public MapRootPane getMapRootPane() { return this.mapRootPane; }
    public NavigationPane getNavigationPane() { return this.navigationPane;}
    public NodeDevToolPane getNodeTool() { return this.menuPane.getDevToolsMenuPane().getNodeDevToolPane(); }
    
}

