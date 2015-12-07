package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

/**
 * Created by jules on 11/28/2015.
 */
public class DirectionsMenuPane extends BorderPane {
	@FXML private ClearableTextField originTextField;
    @FXML private ClearableTextField destinationTextField;
	@FXML private Button routeButton;
    @FXML private ListView directionsListView;
	
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

    @FXML private void onSwitchDirectionsButtonClick(){
        String originContent = originTextField.getText();
        originTextField.setText(destinationTextField.getText());
        destinationTextField.setText(originContent);
    }
}
