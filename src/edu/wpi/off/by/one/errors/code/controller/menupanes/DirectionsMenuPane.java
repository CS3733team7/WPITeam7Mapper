package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * Created by jules on 11/28/2015.
 */
public class DirectionsMenuPane extends BorderPane {
	
	@FXML Button routeButton;
    @FXML private ListView<String> directionsListView;
	
    public DirectionsMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/DirectionsMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            setListeners();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }

        this.getStylesheets().add(getClass().getResource("../../resources/stylesheets/menupanes/DirectionsPaneStyleSheet.css").toExternalForm());
    }

	private void setListeners(){
		this.routeButton.setOnAction(e -> {
			ControllerSingleton.getInstance().getMapRootPane().drawPath();
		});
	}

    public ListView<String> getdirectionsListView(){
        return this.directionsListView;
    }
}
