package edu.wpi.off.by.one.errors.code;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public final MainPane root = new MainPane();

	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //ControllerMediator.getInstance().registerWindow(primaryStage);
        root.getStylesheets().add(getClass().getResource("resources/stylesheets/MainPaneStyleSheet.css").toExternalForm());
    	root.setWindow(primaryStage);
        primaryStage.setTitle("goatThere - WPI Map Application");
        primaryStage.setScene(new Scene(root, 1600, 1000));
        
        primaryStage.show();
    }

}