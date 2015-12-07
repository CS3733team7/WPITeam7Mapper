package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * Created by jules on 11/28/2015.
 */
public class SettingsMenuPane extends AnchorPane {

    public SettingsMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/SettingsMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }

	public void setMainPane(MainPane mainPane) {
		// TODO Auto-generated method stub
		
	}
}
