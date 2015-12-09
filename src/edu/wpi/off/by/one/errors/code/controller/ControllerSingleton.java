package edu.wpi.off.by.one.errors.code.controller;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.IDisplayItem;
import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes.MapDevToolPane;

public class ControllerSingleton {
	
	private MainPane mainPaneController;
	private MapRootPane mapRootPaneController;
	private MenuPane menuPaneController;
	private NavigationPane navigationController;
	
	private MapDevToolPane mapDevToolController;
	
	public void registerMainPane(MainPane m) { mainPaneController = m; }
	public void registerMapRootPane(MapRootPane m) { mapRootPaneController = m; }
	public void registerMenuPane(MenuPane m) { menuPaneController = m; }
	public void registerNavigationPane(NavigationPane m) { navigationController = m; }
	
	public void registerMapDevToolPane(MapDevToolPane m) { mapDevToolController = m; }
	
	public MainPane getMainPane() { return mainPaneController; }
	public MapRootPane getMapRootPane() { return mapRootPaneController; }
	public MenuPane getMenuPane() { return menuPaneController; }
	public NavigationPane getNavigationPane() { return navigationController; }
	
	public MapDevToolPane getMapDevToolPane() { return mapDevToolController; }
	
	public void displayInDev(IDisplayItem d){
		if(d.getClass() == NodeDisplay.class) menuPaneController.devToolsMenuPane.getNodeDevToolPane().displayNodeInfo((NodeDisplay) d);
		else if(d.getClass() == EdgeDisplay.class) menuPaneController.devToolsMenuPane.getEdgeDevToolPane().displayEdgeInfo((EdgeDisplay) d);
	}
	
	
	
	private ControllerSingleton() {}
	
	public static ControllerSingleton getInstance() {
		return ControllerSingletonHolder.INSTANCE;
	}
	
	private static class ControllerSingletonHolder {
		private static final ControllerSingleton INSTANCE = new ControllerSingleton();
	}
}
