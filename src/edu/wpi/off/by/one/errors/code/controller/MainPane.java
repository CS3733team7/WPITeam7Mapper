package edu.wpi.off.by.one.errors.code.controller;
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
	
	Window window;
	
	@FXML ScrollPane mapScrollPane;
	@FXML MenuPane menuPane;
	@FXML MapRootPane mapRootPane;
	@FXML NavigationPane navigationPane;
	
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
    }
    public void setWindow(Window window) { this.window = window; }
    public Window getWindow() { return this.window; }
    public MenuPane getMenuPane() { return this.menuPane; }
    public MapRootPane getMapRootPane() { return this.mapRootPane; }
    public NavigationPane getNavigationPane() { return this.navigationPane;}
    public NodeDevToolPane getNodeTool() { return this.menuPane.getDevToolsMenuPane().getNodeDevToolPane(); }
}
