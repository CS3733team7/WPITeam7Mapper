package old.controller.menucontrollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Created by jules on 11/26/2015.
 */
public class MainMenuPane extends HBox {
    //region FXML attributes
    @FXML
    private ToggleButton hamburgerToggleButton;

    @FXML
    private ToggleGroup menuButtonToggleGroup;

    @FXML
    private RadioButton searchRadioButton;

    @FXML
    private RadioButton directionsRadioButton;

    @FXML
    private RadioButton favoritesRadioButton;

    @FXML
    private RadioButton devModeRadioButton;

    @FXML
    private RadioButton settingsRadioButton;

    @FXML
    private RadioButton helpRadioButton;

    @FXML
    private AnchorPane searchLocationMenuPane;

    //More attributes here

    @FXML
    private BorderPane settingsMenuPane;



    //endregion

    /**
     * Constructor to initialize to load the fxml file
     */
    public MainMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/customcontrols/Menu.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        this.setMinWidth(40.0);
        this.setMaxWidth(275.0);

        addListeners();
    }

    /**
     * adds all the listeners need
     */
    private void addListeners(){
        //listener to compact or extend the customcontrols pane once the hamburgerToggleButton is clicked(the selected value is changed)
        hamburgerToggleButton.selectedProperty().addListener((v, oldValue, newValue) -> showMenuDetails(newValue));

    }

    /**
     * Compacts or extends the customcontrols given a boolean.
     * True = extended and false = compacted
     * Compacts the customcontrols by setting the width of the root customcontrols pane to the min width
     * Extends the customcontrols by setting the width of the root customcontrols pane to the max width
     * @param b The boolean to either compact or extend the customcontrols
     */
    private void showMenuDetails(boolean b){
        if (b){
            this.setPrefWidth(this.getMaxWidth());
        }
        else{
            this.setPrefWidth(this.getMinWidth());
        }
    }


    @FXML
    private void showMenuDetails(){
        if(!hamburgerToggleButton.isSelected())
            hamburgerToggleButton.setSelected(true);
    }








}
