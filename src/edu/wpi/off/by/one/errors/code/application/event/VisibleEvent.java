package edu.wpi.off.by.one.errors.code.application.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
/**
 * Event class that holds all the events to be fired that show/hides display objects
 * TODO Currently only has events for nodes and edges, hopefully can extend to show/hide
 * icons on map.
 *
 */
public class VisibleEvent extends Event{

	/**
	 * Generated Event class UID
	 */
	private static final long serialVersionUID = 2420130780412817007L;
	public static final EventType<VisibleEvent> VisibleEvent = new EventType<>(ANY, "SELECTED");
	public static final EventType<VisibleEvent> NODE_SHOW = new EventType<>(ANY, "NODE_SHOW");
	public static final EventType<VisibleEvent> NODE_HIDE = new EventType<>(ANY, "NODE_HIDE");
	public static final EventType<VisibleEvent> EDGE_SHOW = new EventType<>(ANY, "EDGE_SHOW");
	public static final EventType<VisibleEvent> EDGE_HIDE = new EventType<>(ANY, "EDGE_HIDE");
	
	public VisibleEvent() { this(VisibleEvent); }

	public VisibleEvent(EventType<VisibleEvent> selected) { super(selected); }

	public VisibleEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
        super(arg0, arg1, arg2);
    }
	
}
