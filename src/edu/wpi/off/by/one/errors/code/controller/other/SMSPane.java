package edu.wpi.off.by.one.errors.code.controller.other;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import edu.wpi.off.by.one.errors.code.model.GoogleMail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

/**
 * Created by jules on 12/16/2015.
 */
public class SMSPane extends BorderPane {
    @FXML protected TextArea smsBody;
    @FXML protected ClearableTextField phoneTextField;
    @FXML protected Button smsButton;
    @FXML protected ChoiceBox carrierChoiceBox;

    public SMSPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/off/by/one/errors/code/view/Other/SMSPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        populateCarrierChoiceBox();


        this.visibleProperty().addListener((v, oldValue, newValue) -> {
            if(newValue){
                addInput();
            }
        });
    }

    //region Method for EventListener

    /**
     * Hides the parent container
     */
    @FXML private void onCancelButtonClick(){
        ControllerSingleton.getInstance().getMainPane().showSMSPane(false);
    }
    //endregion

    private String getDirections(){

        List<String> directions = ControllerSingleton.getInstance().getMapRootPane().getPath().getTextual();

        String body = "Hello "+ ControllerSingleton.getInstance().getSettingsMenuPane().getUserName() + "!\n";

        int i = 1;
        for (String s : directions){
            body += (i + ". " + s + "\n");
            i++;
        }

        int numMessage = body.length() / 300;
        char[] message = new char[301];
        String output = "";
        int count = 0;

        return body;

    }

    public void sendMessage(String body){

        String userNumber = phoneTextField.getText();

        GoogleMail googleMail = new GoogleMail();

        String user;
        switch(carrierChoiceBox.getValue().toString()){
            case "AT&T Wireless":
                user = userNumber + "@txt.att.net";
                break;
            case "Alltel Wireless":
                user = userNumber + "@text.wireless.alltel.com";
                break;
            case "AT&T Mobility":
                user = userNumber + "@cingularme.com";
                break;
            case "Boost Mobile":
                user = userNumber + "@myboostmobile.com";
                break;
            case "Cricket":
                user = userNumber + "@sms.mycricket.com";
                break;
            case "Metro PCS":
                user = userNumber + "@mymetropcs.com";
                break;
            case "Sprint PCS":
                user = userNumber + "@messaging.sprintpcs.com";
                break;
            case "Sprint Nextel":
                user = userNumber + "@page.nextel.com";
                break;
            case "Straight Talk":
                user = userNumber + "@vtext.com";
                break;
            case "T-Mobile":
                user = userNumber + "@tmomail.net";
                break;
            case "U.S. Cellular":
                user = userNumber + "@email.uscc.net";
                break;
            case "Verizon":
                user = userNumber + "@vtext.com";
                break;
            case "Virgin Mobile":
                user = userNumber + "@vmobl.com";
                break;
            default:
                user = userNumber + "@vtext.com";
                break;

        }
        googleMail.send(user, "Directions from goatThere()", body);
    }

    private void addInput(){
        this.carrierChoiceBox.setValue(ControllerSingleton.getInstance().getSettingsMenuPane().getCarrier());
        this.phoneTextField.setText(ControllerSingleton.getInstance().getSettingsMenuPane().getUserNumber());
        if(getDirections() != null)
            this.smsBody.setText(getDirections());
    }

    private void populateCarrierChoiceBox(){
        carrierChoiceBox.getItems().addAll("Alltel Wireless", "AT&T Wireless", "AT&T Mobility",
                "Boost Mobile", "Cricket", "Metro PCS", "Sprint PCS", "Sprint Nextel", "Straight Talk",
                "T-Mobile", "U.S. Cellular", "Verizon", "Virgin Mobile");
    }

    @FXML protected void onSendSMSButtonClick(){
        sendMessage(smsBody.getText());
        ControllerSingleton.getInstance().getMainPane().showSMSPane(false);
    }

    public void show(boolean b){
        this.setVisible(b);

        if(b)
            addInput();
    }
}
