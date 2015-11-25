package edu.wpi.off.by.one.errors.code.application.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
/**
 * Event class that holds all the events to be fired from the Editor pane/controller
 *
 */
public class EditorEvent extends Event{
	
	
	/**
	 * Generated Event class UID
	 */
	private static final long serialVersionUID = 3289028169068051934L;
	public static final EventType<EditorEvent> DISPLAY_ITEM = new EventType<>(ANY, "DISPLAY_ITEM");
	public static final EventType<EditorEvent> NODE = new EventType<>(DISPLAY_ITEM, "NODE");
	public static final EventType<EditorEvent> EDGE = new EventType<>(DISPLAY_ITEM, "EDGE");
	public static final EventType<EditorEvent> MAP = new EventType<>(DISPLAY_ITEM, "MAP");
	public static final EventType<EditorEvent> EDIT_ELEMENT = new EventType<>(ANY, "EDIT_ELEMENT");
	public static final EventType<EditorEvent> NONE = new EventType<>(EDIT_ELEMENT, "NONE");
	public static final EventType<EditorEvent> ADD = new EventType<>(EDIT_ELEMENT, "ADD");
	public static final EventType<EditorEvent> EDIT = new EventType<>(EDIT_ELEMENT, "EDIT");
	public static final EventType<EditorEvent> DELETE = new EventType<>(EDIT_ELEMENT, "DELETE");
	
	public EditorEvent() { this(NONE); }

	public EditorEvent(EventType<EditorEvent> selected) { super(selected); }

	public EditorEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2) {
        super(arg0, arg1, arg2);
    } 
}
