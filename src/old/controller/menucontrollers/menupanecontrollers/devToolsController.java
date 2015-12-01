package old.controller.menucontrollers.menupanecontrollers;

import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.FileIO;
import edu.wpi.off.by.one.errors.code.model.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import old.controller.MainController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class devToolsController implements Initializable{
	
	@FXML
	MainController root;
	@FXML ToggleGroup ElementEditor;
	@FXML ToggleGroup DisplayItemEditor;
	@FXML AnchorPane switchableEditPane;
	@FXML VBox editorPane;
	
	private String viewDir = "/";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Register this controller to the mediator
		ControllerMediator.getInstance().registerEditorController(this);
		System.out.println("Editor Controller Initialized");
		setListeners();	
	}
	
	private void setListeners(){
		
		/*
		 * Listener for Add/Edit/Delete Events
		 */
		ElementEditor.selectedToggleProperty().addListener(e -> {
			ToggleButton selected = (ToggleButton) ElementEditor.getSelectedToggle();
			String selectedName;
			EditorEvent selectedEvent;
			// Try getting selected name. If user deselects toggle, then set to "".
			try{
				selectedName = selected.getText();
			} catch (NullPointerException n){
				selectedName = "";
			}
			// Fire event that says a toggle button has been selected/deselected
			switch(selectedName){
			case "Add":
				selectedEvent = new EditorEvent(EditorEvent.ADD);
				selected.fireEvent(selectedEvent);
				break;
			case "Edit":
				selectedEvent = new EditorEvent(EditorEvent.EDIT);
				selected.fireEvent(selectedEvent);
				break;
			case "Delete":
				selectedEvent = new EditorEvent(EditorEvent.DELETE);
				selected.fireEvent(selectedEvent);
				break;
			default:
				selectedEvent = new EditorEvent(EditorEvent.NONE);
				editorPane.fireEvent(selectedEvent);
				break;
			}
			
		});
		/*
		 * Listener for if Map/Node/Edge toggle button was selected
		 * For now, the listener only swaps views in the editor pane
		 * TODO Fire event to MainPane that a display toggle has been selected
		 */
		DisplayItemEditor.selectedToggleProperty().addListener(e -> {
			ToggleButton selected = (ToggleButton) DisplayItemEditor.getSelectedToggle();
			String selectedName;
			EditorEvent selectedEvent;
			try{
				selectedName = selected.getText();
			} catch (NullPointerException n){
				selectedName = "";
			}
			switchableEditPane.getChildren().clear();
			FXMLLoader loader;
			switch(selectedName){
			case "Map":
				selectedEvent = new EditorEvent(EditorEvent.MAP);
				try {
					loader = new FXMLLoader(getClass().getResource("../../../view/customcontrols/menupanes/submenupanes/EditorMapView.fxml"));
					//loader.setController(this);
					loader.setRoot(switchableEditPane);
					loader.load();
					System.out.println(switchableEditPane.getChildren());
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				selected.fireEvent(selectedEvent);
				break;
			case "Node":
				selectedEvent = new EditorEvent(EditorEvent.NODE);
				try {
					loader = new FXMLLoader(getClass().getResource("../../../view/customcontrols/menupanes/submenupanes/EditorNodeView.fxml"));
					//loader.setController(this);
					loader.setRoot(switchableEditPane);
					loader.load();
					//switchableEditPane.getChildren().add(FXMLLoader.load(getClass().getResource(viewDir + "EditorNodeView.fxml")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				selected.fireEvent(selectedEvent);
				break;
			case "Edge":
				selectedEvent = new EditorEvent(EditorEvent.EDGE);
				try {
					loader = new FXMLLoader(getClass().getResource("../../../view/customcontrols/menupanes/submenupanes/EditorEdgeView.fxml"));
					//loader.setController(this);
					loader.setRoot(switchableEditPane);
					loader.load();
					//switchableEditPane.getChildren().add(FXMLLoader.load(getClass().getResource(viewDir + "EditorEdgeView.fxml")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				selected.fireEvent(selectedEvent);
				break;
			default:
				selectedEvent = new EditorEvent(EditorEvent.NONE);
				editorPane.fireEvent(selectedEvent);
				break;
			}
			
		});
	
	}



	@FXML
	private void drawPathDisplay(){
		ControllerMediator cm = ControllerMediator.getInstance();
		cm.drawPath();
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
                 new ExtensionFilter("Image Files", "*.png"),
                 new ExtensionFilter("All Files", "*.*"));
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
        		new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
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
        		new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
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
                 new ExtensionFilter("Text Files", "*.txt"),
                 new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(cm.getWindow());
        //selectedFile.getAbsolutePath();
        FileIO.save(selectedFile.getAbsolutePath(), cm.getDisplay());
	}
	
	
}
