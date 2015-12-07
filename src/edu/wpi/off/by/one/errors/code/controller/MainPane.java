package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes.NodeDevToolPane;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;


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
	@FXML private StackPane mapContainer;
	@FXML private Button rotateLeftButton;
	@FXML private Button rotateRightButton;
	@FXML private Button zoomInButton;
	@FXML private Button zoomOutButton;
    @FXML private Button openNavigationPaneButton;
	@FXML private ScrollPane mapScrollPane;
	@FXML private MenuPane menuPane;
	@FXML private MapRootPane mapRootPane;
	@FXML private NavigationPane navigationPane;
	
	public Coordinate dropStartC;
	public	Coordinate dropEndC;
	public float dropStartR = 0.0f;
	public float dropEndR = 0.0f;
	public float dropStartS = 1.0f;
	public float dropEndS = 1.0f;
	public float dropTime = 0.0f;
	
	public Timeline dropzoom = new Timeline(new KeyFrame(
			Duration.millis(33.3),
			ae -> {
				float sx = dropStartC.getX();
				float sy = dropStartC.getY();
				float sz = dropStartC.getZ();
				float ex = dropEndC.getX();
				float ey = dropEndC.getY();
				float ez = dropEndC.getZ();
				float inbetx = sx* (1.0f - dropTime) + ex * dropTime;
				float inbety = sy* (1.0f - dropTime) + ey * dropTime;
				float inbetz = sz* (1.0f - dropTime) + ez * dropTime;
				mapRootPane.translate.setAll(inbetx, inbety, inbetz);
				
				mapRootPane.rot = dropStartR * (1.0f - dropTime) + dropEndR * dropTime;
				mapRootPane.zoom = dropStartS * (1.0f - dropTime) + dropEndS * dropTime;
				dropTime+= 0.05;
				if(dropTime >= 1.0) stopdrop();
				mapRootPane.render();
			}));
	private void stopdrop(){ dropzoom.stop(); dropTime = 0.0f;
		mapRootPane.translate.setAll(dropEndC.getX(), dropEndC.getY(), dropEndC.getZ());
		mapRootPane.rot = dropEndR;
		mapRootPane.zoom = dropEndS;
	}
	
	Timeline lttl = new Timeline(new KeyFrame(
			Duration.millis(33.3),
			ae -> {
				mapRootPane.rot -= 1.0;
				//mapRootPane.translate.setAll(mapRootPane.translate.getX() + 1.0f, mapRootPane.translate.getY(), mapRootPane.translate.getZ());
				mapRootPane.render();
			}));
	Timeline rttl = new Timeline(new KeyFrame(
			Duration.millis(33.3),
			ae -> {
				mapRootPane.rot += 1.0;
				mapRootPane.render();
			}));
	Timeline zitl = new Timeline(new KeyFrame(
			Duration.millis(33.3),
			ae -> {
				mapRootPane.zoom *= 1.05;
				mapRootPane.render();
			}));
	Timeline zotl = new Timeline(new KeyFrame(
			Duration.millis(33.3),
			ae -> {
				mapRootPane.zoom *= 0.95;
				mapRootPane.render();
			}));
    public MainPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        ControllerSingleton.getInstance().registerMainPane(this);
        mapRootPane = ControllerSingleton.getInstance().getMapRootPane();
        lttl.setCycleCount(Timeline.INDEFINITE);
        rttl.setCycleCount(Timeline.INDEFINITE);
        zitl.setCycleCount(Timeline.INDEFINITE);
        zotl.setCycleCount(Timeline.INDEFINITE);
        dropzoom.setCycleCount(Timeline.INDEFINITE);
        addListeners();

        this.getStylesheets().add(getClass().getResource("../resources/stylesheets/MainPaneStyleSheet.css").toExternalForm());
    }

    private void addListeners(){
        openNavigationPaneButton.visibleProperty().bind(navigationPane.visibleProperty().not());
    	rotateLeftButton.setOnMousePressed(e -> lttl.play());
    	rotateLeftButton.setOnMouseReleased(e -> lttl.stop());
    	rotateRightButton.setOnMousePressed(e -> rttl.play());
    	rotateRightButton.setOnMouseReleased(e -> rttl.stop() );
    	zoomInButton.setOnMousePressed(e -> zitl.play());
    	zoomInButton.setOnMouseReleased(e -> zitl.stop() );
    	zoomOutButton.setOnMousePressed(e -> zotl.play());
    	zoomOutButton.setOnMouseReleased(e -> zotl.stop() );
    }

    @FXML private void onOpenNavigationPaneButtonClick(){
        navigationPane.open();
    }
    public void setWindow(Window window) { 
    	this.window = window; 
    	mapScrollPane.setVmax(0);
    	mapScrollPane.setHmax(0);
    	window.heightProperty().addListener(e -> {
    		mapRootPane.updateCanvasSize(mapContainer.getWidth(), mapContainer.getHeight());
    	});
    	
    	window.widthProperty().addListener(e -> {
    		mapRootPane.updateCanvasSize(mapContainer.getWidth(), mapContainer.getHeight());
    	});
    }
    public Window getWindow() { return this.window; }
    public NodeDevToolPane getNodeTool() { return this.menuPane.getDevToolsMenuPane().getNodeDevToolPane(); }
    
}

