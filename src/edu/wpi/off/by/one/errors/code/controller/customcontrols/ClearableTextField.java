package edu.wpi.off.by.one.errors.code.controller.customcontrols;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by jules on 12/5/2015.
 */
public class ClearableTextField extends BorderPane {

    //region FXML attributes
    @FXML private TextField textField;
    @FXML private Button clearButton;
    //endregion

    //region constructor/s
    public ClearableTextField(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/customcontrols/ClearableTextField.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
            this.getStylesheets().add(getClass().getResource("../../resources/stylesheets/customcontrols/ClearableTextFieldStyleSheet.css").toExternalForm());
        }
        catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        this.getStyleClass().add("clearable-text-field");

        addListeners();
        bind();
    }
    //endregion

    //region Getters, Setters
    public StringProperty textProperty() {
        return textField.textProperty();
    }

    public String getText(){
        return textProperty().get();
    }

    public void setText(String text){
        textProperty().set(text);
    }

    public StringProperty promptTextProperty() {
        return textField.promptTextProperty();
    }

    public String getPromptText(){
        return promptTextProperty().get();
    }

    public void setPromptText(String text){
        promptTextProperty().set(text);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty(){
        return textField.onActionProperty();
    }

    public EventHandler<ActionEvent> getOnAction(){
        return onActionProperty().get();
    }

    public void setOnAction(EventHandler<ActionEvent> actionEvent){
        onActionProperty().set(actionEvent);
    }
    //endregion

    //region listener Methods
    @FXML private void onClearTextBoxButtonClick(){
        textField.clear();
    }
    //endregion

    //region helper Methods

    //endregion

    private void bind(){
        clearButton.disableProperty().bind(textField.textProperty().isEmpty());
    }

    private void addListeners(){
        /*textField.focusedProperty().addListener((v, oldValue, newValue) -> {
            if(newValue)
                textField.positionCaret(0);
                textField.selectAll();
        });
        this.focusedProperty().addListener((v, oldValue, newValue) -> {
            if(newValue)
                textField.positionCaret(0);
            textField.selectAll();
        });*/
    }



}
