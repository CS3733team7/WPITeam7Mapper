package edu.wpi.off.by.one.errors.code.application;

import java.util.ArrayList;

import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Id;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 * GUI "overlay" of the node
 * Contains the node itself + events
 *
 */
public class NodeDisplay extends Circle implements IDisplayItem{
	
	Display display;
	NodeDisplay self = this;
	Id node;
	Coordinate nodeCoords;
	int edgeNum;
	ArrayList<String> tags;
	
	int size = 5; // in px format
	Color color = Color.BLUE;
	public boolean isSelected = false;
	public boolean isPivot = false;
	
	DoubleProperty x, y, z;
	
	public NodeDisplay(Display display, Id node, DoubleProperty x, DoubleProperty y, DoubleProperty z){
		super(x.get(), y.get(), 5);
		setFill(Color.BLUE);
		x.bind(centerXProperty());
		y.bind(centerYProperty());
		this.x = x;
		this.y = y;
		this.z = z;
		this.display = display;
		this.node = node;
		setHandlers();
	}
	
	public NodeDisplay(Display disp, DoubleProperty x, DoubleProperty y, DoubleProperty z){
		super(x.get(), y.get(), 5);
		setFill(Color.BLUE);
		x.bind(centerXProperty());
		y.bind(centerYProperty());
		this.x = x;
		this.y = y;
		this.z = z;
		this.display = disp;
		this.node = disp.getGraph().addNodeRint(
				new Coordinate(x.floatValue(), y.floatValue(), z.floatValue()));
		setHandlers();
	}
	
	public void setNode(Id node) { this.node = node; }
	public Id getNode() { return this.node; }
	
	public NodeDisplay getItemInfo(){ return self; }
	@Override
	public boolean updateItemInfo(IDisplayItem newNodeDisplay){
		//TODO
		return false;
	}
	
	public void deselectNode() {
		this.isPivot = false;
		this.isSelected = false;
		SelectEvent selectNodeEvent;
		setFill(Color.BLUE);
		selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
		self.fireEvent(selectNodeEvent);
	}
	
	public void selectNode() {
		this.isSelected = true;
		setFill(Color.PURPLE);
	}
	
	public void selectPivot() {
		this.isSelected = true;
		this.isPivot = true;
		setFill(Color.SPRINGGREEN);
	}
	
	/* Event handling */
	
	private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		
		public void handle(MouseEvent e){
			
			//If double-click and selected on a building node
			if(e.isStillSincePress()){
				//TODO zoom and rotate onto location
				
			}
			if(e.getButton() == MouseButton.SECONDARY){
				EditorEvent deleteNodeEvent = new EditorEvent(EditorEvent.DELETE_NODE);
				self.fireEvent(deleteNodeEvent);
			} 
			
			else if(e.getButton() == MouseButton.PRIMARY) {
				SelectEvent selectNodeEvent;
				if(isSelected) deselectNode();
				else{
					if(e.isAltDown()){
						selectNodeEvent = new SelectEvent(SelectEvent.PIVOT_NODE_SELECTED);
						self.fireEvent(selectNodeEvent);
					}
					else {
						if(e.isShiftDown()) ControllerSingleton.getInstance().getMapRootPane().isMultiSelectNodes = true;
						else ControllerSingleton.getInstance().getMapRootPane().isMultiSelectNodes = false;
						selectNodeEvent = new SelectEvent(SelectEvent.NODE_SELECTED);
						self.fireEvent(selectNodeEvent);
					}
				}
			}
			
		}
	};
	
	private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
		
		public void handle(MouseEvent e){
			if(!isSelected) {
				setFill(Color.YELLOW);
				setStroke(color.deriveColor(1, 1, 1, 0.5));
				setStrokeWidth(2);
				setStrokeType(StrokeType.OUTSIDE);
			}
		}
	};
	
	private EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {
		
		public void handle(MouseEvent e){
			if(!isSelected){
				setFill(self.color);
				setStroke(Color.TRANSPARENT);
				setStrokeWidth(0);
			}
		}
	};
	
	private void onDeselectEventHandler(){
		this.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
			setFill(self.color);
			setStroke(Color.TRANSPARENT);
			setStrokeWidth(0);
			this.isSelected = false;
		});
	}
	
	private void setHandlers() {
		this.setOnMouseEntered(onMouseEnteredEventHandler);
		this.setOnMouseExited(onMouseExitedEventHandler);
		this.setOnMouseClicked(onMouseClickedEventHandler);
		this.onDeselectEventHandler();
	}
}
