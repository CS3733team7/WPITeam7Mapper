package edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes;

import java.io.IOException;
import java.util.Queue;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Edge;
import edu.wpi.off.by.one.errors.code.model.Graph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by jules on 11/30/2015.
 */
public class EdgeDevToolPane extends VBox {
	
	MainPane mainPane;
	@FXML CheckBox accessibleCheckbox;
	@FXML CheckBox walkwayCheckbox;
	@FXML Label edgeIDLabel;
	@FXML Label edgeLengthLabel;
	@FXML Label addEdgeMessage;
	//@FXML Label selectedEdgesLabel;
	
	Edge currentEdge;
	Queue<EdgeDisplay> edges;
	
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
    	MapRootPane maproot = ControllerSingleton.getInstance().getMapRootPane();
    	Graph g = maproot.getDisplay().getGraph();
    	edges = maproot.getSelectedEdges();
    	Edge e = g.returnEdgeById(ed.getEdge());
    	float len = e.getLength();
    	edgeLengthLabel.setText(Float.toString(len));
    	//selectedEdgesLabel.setText(Integer.toString(edges.size()));
    	//if(edges.size() < 1) { edgeLengthLabel.setDisable(false); edgeLengthLabel.setText(Float.toString(len)); }
    	//else edgeLengthLabel.setDisable(true);
//    	if(edges.size() > 1){
//    		boolean isAllSelected = g.returnEdgeById(edges.peek().getEdge()).getAccessible();
//    		for(EdgeDisplay eds : edges){
//    			isAllSelected = isAllSelected && g.returnEdgeById(eds.getEdge()).getAccessible();
//    			if(isAllSelected) break;
//    		}
//    		accessibleCheckbox.setSelected(isAllSelected);
//    	}
    	accessibleCheckbox.setSelected(g.returnEdgeById(ed.getEdge()).getAccessible());
    }
    
    @FXML public void switchAccessible(){
    	Graph g = ControllerSingleton.getInstance().getMapRootPane().getDisplay().getGraph();
    	for(EdgeDisplay ed : edges){
    		Edge e = g.returnEdgeById(ed.getEdge());
    		e.setAccessible(accessibleCheckbox.isSelected() ? true : false);
    	}
    }
    @FXML public void switchWalkway(){
    	// TODO
    }
}
