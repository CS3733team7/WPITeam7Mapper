package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * Created by jules on 11/28/2015.
 */
public class SettingsMenuPane extends BorderPane {
    //region Attributes

    //region Constants for Identify each line when save/loading user info from textfile.
    private final char NAME_LINE_ID = 'n';
    private final char PHONE_LINE_ID = 'p';
    private final char EMAIL_LINE_ID = 'e';

    //endregion

    //region FXML attributes
    @FXML private ClearableTextField nameTextField;
    @FXML private ClearableTextField phoneTextField;
    @FXML private ClearableTextField emailTextField;
    @FXML CheckBox pirateCheckbox;
    @FXML protected ComboBox carrierChoiceBox;
    //endregion

    //region Other attributes
    private final String FILE_LOCATION = "src/edu/wpi/off/by/one/errors/code/resources/UserSettings.txt";
    private final Path path = Paths.get(FILE_LOCATION);
    private FileWriter fileWriter;
    private PrintWriter printWriter;
    //endregion

    //endregion

    private Button emailButton;
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

        loadUserSettings();
        ControllerSingleton.getInstance().registerSettingsMenuPane(this);
        populateCarrierChoiceBox();
    }
    //endregion

    @FXML private void onSaveButtonClick(){

        if(Files.exists(path)){
            try {
                fileWriter = new FileWriter(FILE_LOCATION);
                printWriter = new PrintWriter(fileWriter);
                printWriter.println(NAME_LINE_ID + nameTextField.getText());
               // printWriter.println(PHONE_LINE_ID + phoneTextField.getText());
                if(isEmailValid()) {
                    printWriter.println(EMAIL_LINE_ID + emailTextField.getText());
                    ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().disableEmailButton(false);
                    emailTextField.hideErrorLabel();
                }
                else {
                    emailTextField.showErrorLabel("!Error Invalid Email");
                    ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().disableEmailButton(true);
                }
                if(isNumberValid()) {
                    printWriter.println(PHONE_LINE_ID + phoneTextField.getText());
                    ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().disableSMSButton(false);
                    phoneTextField.hideErrorLabel();
                }
                else {
                    phoneTextField.showErrorLabel("!Error Invalid Phone Number");
                    ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().disableSMSButton(true);
                }
                printWriter.close();
            }catch (IOException excpt){
                excpt.printStackTrace();
            }
        }

    }
    
    private void populateCarrierChoiceBox(){
    	carrierChoiceBox.getItems().addAll("Alltel Wireless", "AT&T Wireless", "AT&T Mobility",
    			"Boost Mobile", "Cricket", "Metro PCS", "Sprint PCS", "Sprint Nextel", "Straight Talk",
    			"T-Mobile", "U.S. Cellular", "Verizon", "Virgin Mobile");
    }

    private void loadUserSettings(){
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            for(String line : lines){
                char lineID = line.charAt(0);
                switch (lineID){
                    case NAME_LINE_ID : nameTextField.setText(line.substring(1)); break;
                    case PHONE_LINE_ID : phoneTextField.setText(line.substring(1)); break;
                    case EMAIL_LINE_ID : emailTextField.setText(line.substring(1)); break;
                    default: break;
                }
            }
        }catch (IOException excpt){
            excpt.printStackTrace();
        }

    }

    public String getUserEmail(){
        return emailTextField.getText();
    }
    
    public String getUserNumber(){
    	return phoneTextField.getText();
    }
    
    public String getUserName(){
    	return nameTextField.getText();
    }
    
    public String getCarrier(){
    	return (String) carrierChoiceBox.getValue();
    }
    

    
	@FXML private void selectPirate(){
		ControllerSingleton.getInstance().getMapRootPane().isPirateMode = pirateCheckbox.isSelected() ? true : false;
		System.out.println(ControllerSingleton.getInstance().getMapRootPane().isPirateMode);
		ControllerSingleton.getInstance().getMapRootPane().render();
	}
    public boolean isEmailValid(){
        boolean isValid;
        Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_REGEX.matcher(getUserEmail());
        return matcher.find();
    }

    public boolean getIsEmailValid(){
        return false;
        //return isEmailValidProperty.get();
    }

    public void setIsEmailVaild(boolean b){
        //isEmailValidProperty.set(b);
    }
    
    public boolean isNumberValid(){
    	boolean isValid;
    	String number = getUserNumber();
    	Pattern pattern = Pattern.compile("\\d{10}");
    	Matcher matcher = pattern.matcher(number);
    	if (matcher.matches()){
    		isValid = true;
    	}else{
    		isValid = false;
    	}
    	return isValid;
    }
}
