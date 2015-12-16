package edu.wpi.off.by.one.errors.code;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Preload extends Preloader{
	private Stage preloaderStage;
	
	final static String resourceDir = "/edu/wpi/off/by/one/errors/code/resources/icons/";
	private ImageView preview;
	private String preloaderImgPath = resourceDir + "preloadimg.png";

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.initStyle(StageStyle.UNDECORATED);
		this.preloaderStage = primaryStage;
		preview = new ImageView(new Image(preloaderImgPath));
		BorderPane root = new BorderPane(preview);
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	   public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
	      if (stateChangeNotification.getType() == Type.BEFORE_START) {
	         preloaderStage.hide();
	      }
	   }
}
