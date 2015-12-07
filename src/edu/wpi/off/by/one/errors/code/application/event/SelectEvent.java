package edu.wpi.off.by.one.errors.code.application.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
/**
 * Event class that holds all events to be fired from Node/Edge displays
 *
 */
public class SelectEvent extends Event{

	/**
	 * Generated Event class UID
	 */
	private static final long serialVersionUID = -2781253555711376301L;
	public static final EventType<SelectEvent> SELECTED = new EventType<>(ANY, "SELECTED");
	public static final EventType<SelectEvent> NODE_SELECTED = new EventType<>(ANY, "NODE_SELECTED");
	public static final EventType<SelectEvent> NODE_DESELECTED = new EventType<>(ANY, "NODE_DESELECTED");
	public static final EventType<SelectEvent> EDGE_SELECTED = new EventType<>(ANY, "EDGE_SELECTED");
	public static final EventType<SelectEvent> EDGE_DESELECTED = new EventType<>(ANY, "EDGE_DESELECTED");
	
	public SelectEvent() { this(SELECTED); }

	public SelectEvent(EventType<SelectEvent> selected) { super(selected); }

	public SelectEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
        super(arg0, arg1, arg2);
    } 
	
}

