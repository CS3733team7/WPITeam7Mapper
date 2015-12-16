package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.menupanes.DevToolsMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.DirectionsMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.FavoritesMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.HelpMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.SearchMenuPane;
import edu.wpi.off.by.one.errors.code.controller.menupanes.SettingsMenuPane;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Created by jules on 11/28/2015.
 */
public class MenuPane extends HBox {
    //region FXML file attributes
    @FXML protected ToggleButton hamburgerToggleButton;
    @FXML protected RadioButton searchMenuRadioButton;
    @FXML protected RadioButton directionsMenuRadioButton;
    @FXML protected RadioButton favoritesMenuRadioButton;
    @FXML protected RadioButton devToolMenuRadioButton;
    @FXML protected RadioButton settingsMenuRadioButton;
    @FXML protected RadioButton helpMenuRadioButton;
    @FXML protected AnchorPane detailsMenuContainerAnchorPane;
    @FXML protected SearchMenuPane searchMenuPane;
    @FXML protected DirectionsMenuPane directionsMenuPane;
    @FXML protected FavoritesMenuPane favoritesMenuPane;
    @FXML protected DevToolsMenuPane devToolsMenuPane;
    @FXML protected SettingsMenuPane settingsMenuPane;
    @FXML protected HelpMenuPane helpMenuPane;
    //endregion

    //region Other attributes
    /**
     * Animation object to close/compact the menu
     */
    private final Animation collapseAnimation = new Transition() {
        {
            setCycleDuration(Duration.millis(100));
        }
        protected void interpolate(double frac) {
            final double currentWidth = getMaxWidth() * (1.0 - frac);
            setPrefWidth(currentWidth);
        }
    };

    /**
     * Animation object to expand the menu
     */
    private final Animation expandAnimation = new Transition() {
        {
            setCycleDuration(Duration.millis(100));
        }
        protected void interpolate(double frac) {
            final double currentWidth = getMaxWidth() * frac;
            setPrefWidth(currentWidth);
        }
    };
    //endregion

    //region Constructors
    /**
     * Default Constructor
     * This Constructor will get the associated FXML file, set this class as the root node/layout and the controller for that FXML file and then try to load the fxml file.
     * Finally it will set/add all the listeners needed for all the Nodes/controls/layouts that make up this pane.
     * @exception RuntimeException Thrown if file cannot be loaded.
     */
    public MenuPane() {
        //initializing the fxml file.
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/off/by/one/errors/code/view/MenuPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            this.getStylesheets().add(getClass().getResource("/edu/wpi/off/by/one/errors/code/resources/stylesheets/MenuPaneStyleSheet.css").toExternalForm());
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
        addListeners();
        
        removeRadioButtonStyles();
        ControllerSingleton.getInstance().registerMenuPane(this);

    }
    //endregion

    //region getters
    public SearchMenuPane getSearchMenuPane() { return searchMenuPane; }
    public DirectionsMenuPane getDirectionsMenuPane() { return directionsMenuPane; }
	public FavoritesMenuPane getFavoritesMenuPane() { return favoritesMenuPane; }
	public DevToolsMenuPane getDevToolsMenuPane() { return devToolsMenuPane; }
	public SettingsMenuPane getSettingsMenuPane() { return settingsMenuPane; }
	public HelpMenuPane getHelpMenuPane() { return helpMenuPane; }
    //endregion

	//region Listener Methods
    /**
     * adds all the listeners needed
     */
    private void addListeners(){
        hamburgerToggleButton.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue){
                expandAnimation.play();
            }
            else{
                collapseAnimation.play();
            }
        });
    }
    //endregion

    //region Helper Methods
    /**
     * Expands the pane by selecting the toggle button
     */
    public void expand(){
        if(!hamburgerToggleButton.isSelected()){
            hamburgerToggleButton.setSelected(true);
        }
    }

    /**
     * Selects the directionsMenuRadioButton in order to show the directions pane.
     */
    public void showDirections(){
        directionsMenuRadioButton.setSelected(true);
    }

    /**
     * Removes the built in css styleclass for all the radioButtons in this Pane
     */
    public void removeRadioButtonStyles(){
        searchMenuRadioButton.getStyleClass().remove("radio-button");
        directionsMenuRadioButton.getStyleClass().remove("radio-button");
        favoritesMenuRadioButton.getStyleClass().remove("radio-button");
        devToolMenuRadioButton.getStyleClass().remove("radio-button");
        settingsMenuRadioButton.getStyleClass().remove("radio-button");
        helpMenuRadioButton.getStyleClass().remove("radio-button");
    }
    //endregion

}
