package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

/**
 * Created by jules on 11/28/2015.
 */
public class HelpMenuPane extends ScrollPane {
    @FXML private Label label;

    public HelpMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/off/by/one/errors/code/view/menupanes/HelpMenuPane.fxml"));

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
