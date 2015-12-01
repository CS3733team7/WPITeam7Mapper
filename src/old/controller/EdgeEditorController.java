package old.controller;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import old.controller.menucontrollers.menupanecontrollers.ControllerMediator;

import java.net.URL;
import java.util.ResourceBundle;

public class EdgeEditorController implements Initializable {
	
	@FXML private Label edgeIdField, nodeAIdField, nodeBIdField;
	@FXML private TextField edgeLengthField;
	@FXML private Button drawEdgeFromSelectedButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println("Edge Controller initiated");
		ControllerMediator.getInstance().registerEdgeEditorController(this);
	}
	
	@FXML
	private void onDrawEdgeFromSelected(){
		EditorEvent selectedEvent = new EditorEvent(EditorEvent.DRAW_EDGES);
		drawEdgeFromSelectedButton.fireEvent(selectedEvent);
	}
	
	public void updateEdgeInfo(EdgeDisplay ed){
		
		this.edgeIdField.setText(ed.getEdge().toString());
		this.edgeLengthField.setText(Float.toString(ed.getLength()));
	}

}
