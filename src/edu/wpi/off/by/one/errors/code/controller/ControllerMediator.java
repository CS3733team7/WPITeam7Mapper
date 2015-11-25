package edu.wpi.off.by.one.errors.code.controller;

import edu.wpi.off.by.one.errors.code.model.Display;
import javafx.stage.Stage;
/**
 * 
 * Connects all the controller classes together (and additional fx items) and safely calls one from another
 * This is so that no more than one instance of each controller is running
 * on the application
 * 
 * If we have more controllers, then add another register[NAME]Controller method
 * and variable
 *
 */
public class ControllerMediator implements IMediateControllers{
	private Stage window;
	private EditorController ec;
	private MainController mc;
	@Override
	public void registerEditorController(EditorController ec) {
		this.ec = ec;
	}

	@Override
	public void registerMainController(MainController mc) {
		this.mc = mc;
	}
	
	public void registerWindow(Stage window) {
		this.window = window;
	}
	/**
	 * Gets window of application view
	 * @return application window 
	 */
	Stage getWindow(){
		return this.window;
	}
	/**
	 * Gets current display from MainController
	 * @return
	 */
	Display getDisplay(){
		return this.mc.getDisplay();
	}
	/**
	 * Updates current display from MainController
	 * @param d	new display
	 * @param o options to clear/append
	 */
	void updateDisplay(Display d, String o){
		this.mc.updateDisplay(d, o);
	}
	
	private ControllerMediator(){}
	/**
	 * IMPORTANT: Always call this when you need to communicate with another controller
	 * @return the instance of the mediator
	 */
	public static ControllerMediator getInstance(){
		return ControllerMediatorHolder.INSTANCE;
	}
	/**
	 * Holds the instance of the mediator.
	 */
	private static class ControllerMediatorHolder{
		private static final ControllerMediator INSTANCE = new ControllerMediator();
	}

}
