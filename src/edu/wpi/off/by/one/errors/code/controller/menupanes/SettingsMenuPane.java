package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * Created by jules on 11/28/2015.
 */
public class SettingsMenuPane extends BorderPane {
    //region Attributes
    //region Constants for Identify each line when save/loading user info from textfile.
    private final String NAME_STRING = "USER_NAME";
    private final String PHONE_STRING = "USER_PHONE";
    private final String EMAIL_STRING = "USER_EMAIL";
    //endregion

    //region FXML attributes

    //endregion

    //region Other attributes
    private final String FILE_LOCATION = "@../../resources/UserSettings.txt";
    private FileWriter writer;
    
    //endregion
    //endregion

    //region Constructors
    /**
     * Constructor
     * Will load the fxml file and initialize necessary controls and layouts
     */
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
    //endregion

}
