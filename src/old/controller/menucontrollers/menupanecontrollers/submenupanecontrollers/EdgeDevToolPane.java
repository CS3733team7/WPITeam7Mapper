package old.controller.menucontrollers.menupanecontrollers.submenupanecontrollers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by jules on 11/27/2015.
 */
public class EdgeDevToolPane extends VBox {

    public EdgeDevToolPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../view/customcontrols/menupanes/submenupanes/EditorEdgeView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }
}
