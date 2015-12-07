package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

/**
 * Created by jules on 11/28/2015.
 */
public class NavigationPane extends GridPane {
	
    public NavigationPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NavigationPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        ControllerSingleton.getInstance().registerNavigationPane(this);
        this.getStylesheets().add(getClass().getResource("../resources/stylesheets/NavigationPaneStyleSheet.css").toExternalForm());
    }

    public void open(){
        this.setPrefHeight(this.getMaxHeight());
        this.setVisible(true);
    }

    public void close(){
        this.setPrefHeight(this.getMinHeight());
        this.setVisible(false);
    }

    @FXML
    private void onClosePaneButtonClick(){
        close();
    }

}
