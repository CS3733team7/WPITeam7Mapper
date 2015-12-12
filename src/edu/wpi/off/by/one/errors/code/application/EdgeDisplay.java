package edu.wpi.off.by.one.errors.code.application;

import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Edge;
import edu.wpi.off.by.one.errors.code.model.Id;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeDisplay extends Line implements IDisplayItem {
	Display display;
	EdgeDisplay self = this;
	Line parent;
	Id edge;
	Id nodeA, nodeB;
	float length;
	int selectedStrokeLength = 8;
	int strokeLength = 5; //default
	Color strokeColor = Color.AQUA;
	
	
	public boolean isSelected = false;
	
	public EdgeDisplay(Display display, Id edge, Coordinate start, Coordinate end){
		this.display = display;
		this.edge = edge;
		Edge temp = display.getGraph().returnEdgeById(edge);
		this.nodeA = temp.getNodeA();
		this.nodeB = temp.getNodeB();
		this.length = temp.getLength();

		
		setHandlers();
	}
	
	public EdgeDisplay(Display display, Id nodeA, Id nodeB, Coordinate start, Coordinate end){
		this.display = display;
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.edge = display.getGraph().addEdgeRint(nodeA, nodeB);
		this.length = display.getGraph().returnEdgeById(this.edge).getLength();
		
		setHandlers();
	}
	
	public void setEdge(Id edge) { this.edge = edge; }
	public Id getEdge() { return this.edge; }
	public float getLength() {return this.length; }
	
	@Override
	public IDisplayItem getItemInfo() { return this; }

	@Override
	public boolean updateItemInfo(IDisplayItem item) {
		// TODO Auto-generated method stub
		// Right now there's not much to edit on an edge
		return false;
	}
	
	public void selectEdge() {
		this.isSelected = true;
		super.setStroke(Color.PURPLE);
		super.setStrokeWidth(this.selectedStrokeLength);
	}
	
	
	public void setStroke(Color c){
		super.setStroke(c);
		this.strokeColor = c;
	}
	
	/* Event handling */
	
	private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		
		public void handle(MouseEvent e){
			
			if(e.getButton() == MouseButton.PRIMARY){
				if(!isSelected){
					SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.EDGE_SELECTED);
					self.fireEvent(selectNodeEvent);
				} else {
					SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.EDGE_DESELECTED);
					self.fireEvent(selectNodeEvent);
				}
			}
			else if(e.getButton() == MouseButton.SECONDARY){
				EditorEvent deleteEdgeEvent = new EditorEvent(EditorEvent.DELETE_EDGE);
				self.fireEvent(deleteEdgeEvent);
			}
		}
	};
	
	private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent e){
			if(!isSelected) {
				self.setStroke(Color.YELLOW);
				self.setStrokeWidth(self.selectedStrokeLength);
			}
		}
	};
	
	private EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {
		
		public void handle(MouseEvent e){
			if(!isSelected){
				self.setStroke(Color.AQUA);
				self.setStrokeWidth(self.strokeLength);
			}
		}
	};
	
	private void onDeselectEventHandler(){
		this.addEventFilter(SelectEvent.EDGE_DESELECTED, event -> {
			super.setStroke(Color.AQUA);
			super.setStrokeWidth(this.strokeLength);
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
