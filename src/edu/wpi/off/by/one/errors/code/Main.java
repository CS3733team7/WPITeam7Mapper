package edu.wpi.off.by.one.errors.code;

import edu.wpi.off.by.one.errors.code.*;
import edu.wpi.off.by.one.errors.code.controller.ControllerMediator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
        ControllerMediator.getInstance().registerWindow(primaryStage);
        primaryStage.setTitle("goatThere - WPI Map Application");
        primaryStage.setScene(new Scene(root, 1600, 1000));
        primaryStage.show();
    }

}
