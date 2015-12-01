package old.controller.menucontrollers.menupanecontrollers.submenupanecontrollers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by jules on 11/28/2015.
 */
public class PathDevToolPane extends VBox {

    public PathDevToolPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../view/customcontrols/menupanes/submenupanes/PathDevToolsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }

}
