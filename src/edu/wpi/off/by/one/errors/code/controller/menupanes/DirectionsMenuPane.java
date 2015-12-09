package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.model.GoogleMail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import edu.wpi.off.by.one.errors.code.model.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * Created by jules on 11/28/2015.
 */
public class DirectionsMenuPane extends BorderPane {
	@FXML private ClearableTextField originTextField;
    @FXML private ClearableTextField destinationTextField;
	@FXML Button routeButton;
    @FXML private ListView<String> directionsListView;
    @FXML CheckBox accessibleCheckbox;
	
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
	
	@FXML private void selectAccessible(){
		ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode = accessibleCheckbox.isSelected() ? true : false;
		System.out.println("Accessibility: " + ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode);
	}
	
	public Node getDirectionsToNode() { return null; }
	public Node getDirectionsFromNode() { return null; }
	
	public void setDirectionsToNode(){
		//TODO Should take an input (String? NodeDisplay? Node?)
		//Put String name in the To box
		//set directionsTo variable (NOT MADE YET)
	}
	
	public void setDirectionsFromNode(){
		//TODO Should take an input (String? NodeDisplay? Node?)
		//Put String name in the From box
		//set directionsFrom variable (NOT MADE YET)
	}

    public ListView<String> getdirectionsListView(){
        return this.directionsListView;
    }

    @FXML private void onSwitchDirectionsButtonClick(){
        String originContent = originTextField.getText();
        originTextField.setText(destinationTextField.getText());
        destinationTextField.setText(originContent);
    }

    @FXML private void onEmailButtonClick(){
        GoogleMail googleMail = new GoogleMail();
        googleMail.send("", "Testing Mapper Email", "This is a test to see if the email class works");
    }
}
