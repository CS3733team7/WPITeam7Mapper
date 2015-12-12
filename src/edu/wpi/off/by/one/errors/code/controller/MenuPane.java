package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.menupanes.DevToolsMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.DirectionsMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.FavoritesMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.HelpMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.SearchMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.SettingsMenuPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Created by jules on 11/28/2015.
 */
public class MenuPane extends HBox {

    //region FXML file attributes
    @FXML
    private ToggleButton hamburgerToggleButton;

    @FXML
    private AnchorPane detailsMenuContainerAnchorPane;
    
    @FXML public SearchMenuPane searchMenuPane;
    @FXML DirectionsMenuPane directionsMenuPane;
    //@FXML FavoritesMenuPane favoritesMenuPane;
    @FXML DevToolsMenuPane devToolsMenuPane;
    @FXML SettingsMenuPane settingsMenuPane;
    //@FXML HelpMenuPane helpMenuPane;

    //Menu RadioButton
    @FXML private RadioButton searchMenuRadioButton;
    @FXML private RadioButton directionsMenuRadioButton;
    @FXML private RadioButton favoritesMenuRadioButton;
    @FXML private RadioButton devToolMenuRadioButton;
    @FXML private RadioButton settingsMenuRadioButton;
   // @FXML private RadioButton helpMenuRadioButton;

    private BooleanProperty isCompactProperty;

    //endregion

    //region Constructors
    /**
     * Default Constructor
     * This Constructor will get the associated FXML file, set this class as the root node/layout and the controller for that FXML file and then try to load the fxml file.
     * Finally it will set/add all the listeners needed for all the Nodes/controls/layouts that make up this pane.
     * @exception RuntimeException Thrown if file cannot be loaded.
     */
    public MenuPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
        isCompactProperty = new SimpleBooleanProperty();

        removeRadioButtonStyles();
        addListeners();
        this.getStylesheets().add(getClass().getResource("../resources/stylesheets/MenuPaneStyleSheet.css").toExternalForm());
        ControllerSingleton.getInstance().registerMenuPane(this);

        compact();

    }
    //endregion
    
    public SearchMenuPane getSearchMenuPane() { return searchMenuPane; }
    public DirectionsMenuPane getDirectionsMenuPane() { return directionsMenuPane; }
	//public FavoritesMenuPane getFavoritesMenuPane() { return favoritesMenuPane; }
	public DevToolsMenuPane getDevToolsMenuPane() { return devToolsMenuPane; }
	public SettingsMenuPane getSettingsMenuPane() { return settingsMenuPane; }
	//public HelpMenuPane getHelpMenuPane() { return helpMenuPane; }


	//region Listener Methods
    /**
     * adds all the listeners needed
     */
    private void addListeners(){
        isCompactProperty.addListener((v, oldValue, newValue) -> {
            if (newValue){
                this.setPrefWidth(this.getMaxWidth());
                detailsMenuContainerAnchorPane.setVisible(true);
            }
            else{
                this.setPrefWidth(this.getMinWidth());
                detailsMenuContainerAnchorPane.setVisible(false);
            }
        });

        isCompactProperty.bindBidirectional(hamburgerToggleButton.selectedProperty());
    }
    //endregion

    //region Helper Methods
    /**
     * Extends the pane by selecting the toggle button
     */
    public void extend(){
        if(!isCompactProperty.get()){
            isCompactProperty.setValue(true);
        }
    }

    /**
     * Compacts the pane by deselection the toggle button
     */
    public void compact(){
        if(isCompactProperty.get()){
            isCompactProperty.setValue(false);
        }
    }
    //endregion
    
    public void showDirections(){
    	directionsMenuRadioButton.setSelected(true);
    }

    public void removeRadioButtonStyles(){
        searchMenuRadioButton.getStyleClass().remove("radio-button");
        directionsMenuRadioButton.getStyleClass().remove("radio-button");
        favoritesMenuRadioButton.getStyleClass().remove("radio-button");
        devToolMenuRadioButton.getStyleClass().remove("radio-button");
        settingsMenuRadioButton.getStyleClass().remove("radio-button");
        //helpMenuRadioButton.getStyleClass().remove("radio-button");
    }

}
