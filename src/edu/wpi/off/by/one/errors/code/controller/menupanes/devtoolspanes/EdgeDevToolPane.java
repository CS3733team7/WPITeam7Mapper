package edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes;

import java.io.IOException;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.model.Display;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by jules on 11/30/2015.
 */
public class EdgeDevToolPane extends VBox {
	
	MainPane mainPane;
	
	@FXML Label edgeIDLabel;
	@FXML Label edgeLengthLabel;
	@FXML Label addEdgeMessage;
	
    public EdgeDevToolPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../view/menupanes/devtoolspanes/EdgeDevToolPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
    }
    
    public void setMainPane(MainPane m){ this.mainPane = m; } 
    
    @FXML public void connectEdgesInOrderAction(){
    	MapRootPane mp = ControllerSingleton.getInstance().getMapRootPane();
    	if(mp.addEdgeDisplayFromQueue()) {
    		addEdgeMessage.setText("Success!");
    		addEdgeMessage.setTextFill(javafx.scene.paint.Color.GREEN);
    	}
    	else{
    		addEdgeMessage.setText("Adding edge(s) failed.");
    		addEdgeMessage.setTextFill(javafx.scene.paint.Color.RED);
    	}
    }
    
    @FXML private void connectEdgesToFirstAction(){
    	MapRootPane mp = ControllerSingleton.getInstance().getMapRootPane();
    	boolean success = mp.addEdgeDisplayFromQueue();
    	if(success) {
    		addEdgeMessage.setText("Success!");
    		addEdgeMessage.setTextFill(javafx.scene.paint.Color.GREEN);
    	}
    	else{
    		addEdgeMessage.setText("Adding edge(s) failed.");
    		addEdgeMessage.setTextFill(javafx.scene.paint.Color.RED);
    	}
    }
    
    public void displayEdgeInfo(EdgeDisplay ed){
    	edgeIDLabel.setText(ed.getEdge().toString());
    	Display d = ControllerSingleton.getInstance().getMapRootPane().getDisplay();
    	float len = d.getGraph().returnEdgeById(ed.getEdge()).getLength();
    	edgeLengthLabel.setText(Float.toString(len));
    }
}
