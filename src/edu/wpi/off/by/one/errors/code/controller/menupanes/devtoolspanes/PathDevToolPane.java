package edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by jules on 11/30/2015.
 */
public class PathDevToolPane extends VBox {
    public PathDevToolPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/menupanes/devtoolspanes/PathDevToolPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }
}