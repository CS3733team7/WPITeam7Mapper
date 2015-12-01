package old.controller.menucontrollers.menupanecontrollers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by jules on 11/26/2015.
 */
public class DirectionsMenuPane extends AnchorPane {

    public DirectionsMenuPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/customcontrols/menupanes/DirectionsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
    }
}
