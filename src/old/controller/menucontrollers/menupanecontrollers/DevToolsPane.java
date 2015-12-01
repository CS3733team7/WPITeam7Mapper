package old.controller.menucontrollers.menupanecontrollers;

import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.FileIO;
import edu.wpi.off.by.one.errors.code.model.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by jules on 11/27/2015.
 */

public class DevToolsPane extends BorderPane {

    public DevToolsPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/customcontrols/menupanes/DevToolsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException excpt){
            throw new RuntimeException(excpt);
        }
    }

    /*
	 * Following methods are related to loading/saving displays onto the map
	 * from the editor pane.
	 */

    /**
     * Loads a new image onto map, with no existing nodes.
     */
    @FXML
    private void onNewImageClick(){
        ControllerMediator cm = ControllerMediator.getInstance();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(cm.getWindow());
        if (selectedFile != null) {
            String inpath = selectedFile.getName();
            System.out.println(inpath);
            Map newmap = new Map();
            newmap.setImgUrl(inpath);
            Display newdisp = new Display();
            newdisp.setMap(newmap);
            cm.updateDisplay(newdisp, "NEW");
        }
    }

    /**
     * Loads new display
     */
    @FXML
    private void onLoadNewDisplayClick(){
        ControllerMediator cm = ControllerMediator.getInstance();
        Display newdisp = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(cm.getWindow());
        if (selectedFile != null) {
            String inpath = selectedFile.getPath();
            System.out.println(inpath);
            cm.updateDisplay(FileIO.load(inpath, newdisp), "NEW");
        }

    }

    /**
     * Appends existing graph onto display
     */
    @FXML
    private void onLoadDisplayClick(){
        ControllerMediator cm = ControllerMediator.getInstance();
        Display currdisp = cm.getDisplay();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(cm.getWindow());
        if (selectedFile != null) {
            String inpath = selectedFile.getPath();
            System.out.println(inpath);
            cm.updateDisplay(FileIO.load(inpath, currdisp), "APPEND");
        }

    }
    /**
     * Saves current display
     */
    @FXML
    private void onSaveDisplayClick(){
        ControllerMediator cm = ControllerMediator.getInstance();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(cm.getWindow());
        //selectedFile.getAbsolutePath();
        FileIO.save(selectedFile.getAbsolutePath(), cm.getDisplay());
    }
}
