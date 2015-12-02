package edu.wpi.off.by.one.errors.code.controller.menupanes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.MainPane;

/**
 * Created by jules on 11/28/2015.
 */
public class DirectionsMenuPane extends BorderPane {
	
	MainPane mainPane;
	
	@FXML Button routeButton;
	
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
    }

	public void setMainPane(MainPane mainPane) { this.mainPane = mainPane; }
	
	private void setListeners(){
		this.routeButton.setOnAction(e -> {
			mainPane.getMapRootPane().drawPath();
		});
	}
}
