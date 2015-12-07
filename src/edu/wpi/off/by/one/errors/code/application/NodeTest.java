package edu.wpi.off.by.one.errors.code.application;

//THE CLASSES BELOW ARE NOT PROPER IMPLEMENTATIONS. I AM NOT A 100% SURE ABOUT THIS CODE 
//AND I DONT WANT TO RISK MESSING UP THE CODE THIS LATE SO CHECK IT OUT 
//AND IF YOU HAVE QUESTIONS FEEL FREE TO ASK

//I am using properties, they are kinda troublesome. 
//Sometimes they dont work when outside a block of code

import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;

public class NodeTest {
	
	
	/*
	public static void main(String[] args){

		
		Button btn  = new Button("Edit node"); //EXAMPLE OF BUTTONS TO BE USED
		Button btn1 = new Button("add Edge");
		
		btn.setDisable(true); //they should initially be disabled as nothing has been clicked yet
		btn1.setDisable(true);
		
	ArrayList<NodeDisplay> listOfNodes = new ArrayList<NodeDisplay>(); //example of list of Nodes to be used
	
	for(int i = 0; i < 6; i++){
		listOfNodes.add(new NodeDisplay(i)); //add nodes
	}
	
	for(int i = 0; i < 6;i++){
		NodeDisplay current = listOfNodes.get(i);
		current.addEventHandler(MouseEvent.MOUSE_CLICKED, current.nodeClicked); //add an event handler for the node being clicked on
	}//Added to all Nodes in the List
	
	//A change listener is added to each Node. if the node is clicked the buttons are enabled
	for(int i = 0; i < 6; i++){
		NodeDisplay current = listOfNodes.get(i);
		current.clickedProperty().addListener(new ChangeListener(){
			
			@Override
			public void changed(ObservableValue o, Object oldVal, Object newVal){
				
				if(current.getClicked() == true){
					
					btn.setDisable(false);
					btn1.setDisable(false);
					
				}else{
					
						btn.setDisable(true);
						btn1.setDisable(true);
				}
			}
		});
	} ///////////////////////// Depending on whether the node stays selected after clicking I was considering using a press event 
	//which would need continued pressure on the button
	
	}//end of main method
*/
}//end class NodeTest