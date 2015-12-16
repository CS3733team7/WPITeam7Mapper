package edu.wpi.off.by.one.errors.code.controller.other;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import edu.wpi.off.by.one.errors.code.model.GoogleMail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by jules on 12/16/2015.
 */
public class EmailPane extends BorderPane {
    @FXML protected Button send;
    @FXML protected ClearableTextField to;
    @FXML protected ClearableTextField subject;
    @FXML protected TextArea body;


    public EmailPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/off/by/one/errors/code/view/Other/EmailPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }

    //region Methods for listeners
    @FXML private void onCancelButtonClick(){
        ControllerSingleton.getInstance().getMainPane().showEmailPane(false);
    }
    //endregion

    @FXML protected void onSendButtonClick(){
        GoogleMail googleMail = new GoogleMail();
        googleMail.send(to.getText().trim(), this.subject.getText(), this.body.getText());
        ControllerSingleton.getInstance().getMainPane().showEmailPane(false);

    }

    public void show(boolean b){
        this.setVisible(b);
        if(b)
            addValues();
    }

    public void addValues(){
        this.to.setText(ControllerSingleton.getInstance().getSettingsMenuPane().getUserEmail());
        this.subject.setText("Directions");
        this.body.setText(getDirections());
    }

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
}
