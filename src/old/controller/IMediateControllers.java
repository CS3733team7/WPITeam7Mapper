package old.controller;

import old.controller.menucontrollers.menupanecontrollers.devToolsController;

/**
 * Interface for the controller mediator
 *
 */
public interface IMediateControllers {
	void registerEditorController(devToolsController ec);
	void registerMainController(MainController mc);
}
