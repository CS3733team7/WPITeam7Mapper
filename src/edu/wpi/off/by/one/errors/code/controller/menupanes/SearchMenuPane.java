package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.AutoCompleteTextField;
import edu.wpi.off.by.one.errors.code.model.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

/**
 * Created by jules on 11/28/2015.
 */
public class SearchMenuPane extends BorderPane {
	
	@FXML AutoCompleteTextField searchField;
	@FXML Slider floorSlider;
	@FXML ComboBox<String> buildingChoiceBox;
	
	int currentLevel;
	
    public SearchMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/SearchMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        this.getStylesheets().add(getClass().getResource("../../resources/stylesheets/menupanes/SearchPaneStyleSheet.css").toExternalForm());
        setListeners();
        
        //SortedSet<String> entries = new TreeSet<String>();
        /*
        for(Map m : ControllerSingleton.getInstance().getMapRootPane().getDisplay().getMaps()){
        	String name = (m.getName() == null) ? m.getImgUrl() : m.getName();
        	//buildingChoiceBox.getItems().add(name);
        }*/
    }
	
	public void updateMapList(ArrayList<Map> maps){
		buildingChoiceBox.getItems().clear();
		for(Map m : ControllerSingleton.getInstance().getMapRootPane().getDisplay().getMaps()){
        	String name = (m.getName() == null) ? m.getImgUrl() : m.getName();
        	buildingChoiceBox.getItems().add(name);
        }
	}
	
	private void setListeners(){
		this.floorSlider.setOnMouseClicked(e -> {
			MapRootPane maproot = ControllerSingleton.getInstance().getMapRootPane();
			int floor = (int) floorSlider.getValue();
			//System.out.println(floor);
			//do a thing with it
			maproot.currentLevel = floor;
			maproot.getMapRootPane().translate.setAll(maproot.translate.getX(), maproot.translate.getY(), floor);
			maproot.getMapRootPane().render();
		});
		
		/*this.buildingChoiceBox.setOnContextMenuRequested(e -> {
			//TODO update map list
		});
		
		this.buildingChoiceBox.setOnAction(e -> {
			int index = buildingChoiceBox.getItems().indexOf(buildingChoiceBox.getSelectionModel().getSelectedItem());
			//System.out.println(index);
			Map m = mainPane.getMapRootPane().getDisplay().getMaps().get(index);
			if(m == null) return;

			mainPane.dropStartC = mainPane.getMapRootPane().translate;
			mainPane.dropStartR = mainPane.getMapRootPane().rot;
			mainPane.dropStartS = mainPane.getMapRootPane().zoom;
			
			//float zx = (float) (m.getCenter().getX() + m.getImage().getWidth() * 0.5f * m.getScale());
			//float zy = (float) (m.getCenter().getY() + m.getImage().getHeight() * 0.5f * m.getScale());
			mainPane.dropEndC = m.getCenter();
			mainPane.dropEndR = -m.getRotation();
			float mx = (float)(mainPane.getMapRootPane().canvas.getWidth()/(m.getImage().getWidth() * m.getScale()) );
			float my = (float)(mainPane.getMapRootPane().canvas.getHeight()/(m.getImage().getHeight() * m.getScale()));
			mainPane.dropEndS = mx < my ? mx : my;
			if(mainPane.dropEndS <= 0.05f) mainPane.dropEndS = 1.0f;
			mainPane.dropzoom.play();
			mainPane.getMapRootPane().render();
			buildingChoiceBox.getSelectionModel().clearSelection();
		});*/
	}
}
