package edu.wpi.off.by.one.errors.code.application;

import java.awt.Paint;
import java.io.File;
import java.util.*;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import edu.wpi.off.by.one.errors.code.*;
import edu.wpi.off.by.one.errors.code.Map;
import edu.wpi.off.by.one.errors.code.application.event.*;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * This Main class launches the application and sets up all the GUI interactions
 * Mainly talks to the Display class to send manipulate data in some way to the
 * underlying data.
 * 
 * p.s. whoever is going to be working on GUI feel free to add your name to this?
 * @author kezhang
 *
 */
public class Main extends Application {
	
	private Display display = new Display();
	private Queue<NodeDisplay> nodeQueue = new LinkedList<NodeDisplay>();
	
	/* root BorderPane
	 * TOP: MenuBar
	 * BOTTOM: Editor logger (Maybe?????)
	 * CENTER: Inner BorderPane
	 * 
	 * Inner BorderPane will contain
	 * CENTER: Map component
	 * RIGHT: Editor section/Logo + Directions
	 */
	
	// Main application elements
	private Stage window;
	Scene scene;
	BorderPane root = new BorderPane();
	BorderPane innerBorderPane = new BorderPane();
	
	// Editor Logger Box
	VBox infoBox = new VBox(2);
	Text infoText = new Text();
	
	//Logo Pane Elements
	Pane logoPane = new Pane();
	ImageView wpiLogo;
	
	// Editor Pane Elements
	/* IN PROGRESS PLIS WAIT */
	VBox editorBox = new VBox();
	VBox editPane = new VBox(5); //Change this so that buttons layout better
	Label nodeLabel = new Label("Editor");
	Button addNode = new Button("Add Node");
	Button editNode = new Button("Edit Node");
	Button addEdge = new Button("Add Edge");
	Button editEdge = new Button("Edit Edge");
	Button saveNodes = new Button("Save All");
	Button clearNodes = new Button("Clear All");
	Label show = new Label("Show");
	CheckBox showNodes = new CheckBox("Nodes");
	CheckBox showEdges = new CheckBox("Edges");
	
	// Directions Pane Elements
	VBox directionsBox = new VBox(10);
	Label settingsLabel = new Label("Directions");
	Button drawPathButton = new Button("Draw Path");
	
	// Menu Bar Elements
	MenuBar menuBar = new MenuBar();
	Menu fileMenu = new Menu("File");
	Menu viewMenu = new Menu("View");
	Menu helpMenu = new Menu("Help");
	MenuItem loadMenuItem = new MenuItem("Load");
	MenuItem saveMenuItem = new MenuItem("Save");
	CheckMenuItem setEditorMode = new CheckMenuItem("Editor Mode");

	// Map Display Elements
	ScrollPane scroll;
	ImageView mapView;
	StackPane mapPane;
	private MarkerDisplay marker = null;
	final SimpleDoubleProperty zoomProperty = new SimpleDoubleProperty(200);

	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * Last Edited 11/9/15 I apologize in advance if this code is extremely
	 * messy This is just a starting point for now I'll get a better version of
	 * the map for later - Kelly
	 * 
	 * 11/17/15 
	 * Code has been organized so that all element declarations
	 * and/or instantiations are done up there. Any property adjustments to said
	 * elements (i.e. appearance, parenting them) will be in the start method.
	 * Event handlers should be made in their respective functions. So events
	 * related to the map tool/editor buttons will be in setEditorButtons().
	 * 
	 * Feel free to shoot a message about how any of this works in #gui.
	 * 
	 * Additionally feel free to suggest alternative ways of organizing this.
	 * Jules has mentioned that FXML is easier to work with so we will most likely
	 * switch over to that format after everything is set in stone for the 1st iteration. 
	 * 
	 *  Kelly
	 * 
	 */

	@Override
	public void start(Stage primaryStage) {
		
		window = primaryStage; //Main window
		scene = new Scene(root, 1600, 1000);
		innerBorderPane.setPadding(new Insets(20, 20, 20, 20));

		// START: BORDERPANE RIGHT COMPONENTS --------------------------------------------
		editorBox.setPrefSize(scene.getWidth() * 0.3, 600);
		editorBox.setPadding(new Insets(0, 0, 0, 10));
		editPane.setPadding(new Insets(0, 0, 100, 0));				
		editPane.getChildren().addAll(nodeLabel, new Separator(), addNode, editNode, addEdge, editEdge, 
				saveNodes, clearNodes, show,						
				showNodes, showEdges);
		editorBox.getChildren().addAll(editPane, settingsLabel, 
				new Separator(), drawPathButton);
		// END: BORDERPANE RIGHT COMPONENTS --------------------------------------------
		
		// START: BORDERPANE CENTER COMPONENTS -----------------------------------------
		// AKA - Map stuff		
		mapPane = new StackPane();
		mapView = GetMapView();
		
		/* SOMEWHERE AROUND HERE DO STUFF WITH DISPLAY CLASS */
		/* OR! Leave map empty and wait for file loading */
		
		//mapPane.scaleXProperty().bind(zoomProperty);
		//mapPane.scaleYProperty().bind(zoomProperty);
		scroll = createScrollPane(mapPane);
		scroll.setCenterShape(true);
		scroll.prefWidthProperty().bind(scene.widthProperty());
		scroll.prefHeightProperty().bind(scene.widthProperty());
		// center the scroll contents.
		scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
		scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);
		mapPane.getChildren().add(mapView);
		// END: BORDERPANE CENTER COMPONENTS -------------------------------------------
		
		// START: BORDERPANE TOP COMPONENTS --------------------------------------------
		// MENU
		fileMenu.getItems().addAll(loadMenuItem, saveMenuItem);
		menuBar.autosize();
		setEditorMode.setSelected(true);
		viewMenu.getItems().add(setEditorMode);
		editPane.visibleProperty().bind(setEditorMode.selectedProperty());
		menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
		// END: BORDERPANE TOP COMPONENTS ----------------------------------------------
		
		innerBorderPane.setCenter(scroll);
		innerBorderPane.setRight(editorBox);
		
		root.setTop(menuBar);
		root.setCenter(innerBorderPane);
		root.setBottom(infoBox);

		/* ADD EVENT LISTENERS AND HANDLERS HERE */
		AddSceneListeners();
		setEditorButtons();
		setMenuButtons();
		
		window.setTitle("WPI Map Application");
		window.setScene(scene);
		window.show();
	} 
	
	// Gets the map image, sets properties, returns a usable node by JavaFX
	// Should be getting it from Display instead (?)
	// TODO delegate this to Map????? or even Display???????????
	private ImageView GetMapView() {
		Map m = display.getMap();
		m.setName("Campus Map");
		m.setCenter(new Coordinate(0));
		m.setImgUrl("campusmap.png");
		m.setRotation(0);
		m.setScale(0); //CHANGE THIS LATER
		Image map = new Image("campusmap.png");
		ImageView mapIV = new ImageView();
		mapIV.setImage(map);
		mapIV.setPreserveRatio(true);
		mapIV.setSmooth(true);
		mapIV.setCache(true);
		return mapIV;
	}

	
	private ScrollPane createScrollPane(Pane layout) {
		ScrollPane scroll = new ScrollPane();
		scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroll.setPannable(true);
		scroll.setPrefSize(600, 600);
		scroll.setContent(layout);
		return scroll;
	}
	
	/**
	 * Listens for MouseEvents to allow user to add/move/select
	 * items on the map
	 */
	private void AddSceneListeners() {
		
		/*
		 * Single-Click		:	Places marker on point TODO
		 * Double-click 	: 	Create node on map
		 * Click-Drag 		:	Pans the map
		 * Click on node 	: 	(De)Select node + add selection to NodeQueue
		 * Scroll			: 	Pans map for now. Ideally should zoom map. TODO
		 */
		mapView.setOnMouseClicked(e -> {
			if(e.isStillSincePress()) {
				// If user did not click-drag the map 
				// Data to show coordinates of where user has clicked on map, delete later
				String msg = "(x: " + e.getX() + ", y: " + e.getY() + ") -- " + "(sceneX: " + e.getSceneX() + ", sceneY: "
						+ e.getSceneY() + ") -- " + "(screenX: " + e.getScreenX() + ", screenY: " + e.getScreenY() + ")";
				System.out.println(msg);
				/*
				if(marker == null){
					marker = new MarkerDisplay(e.getX(), e.getY());
					Bounds localBounds = marker.localToScene(mapView.getBoundsInLocal());
					marker.setTranslateX(e.getX() - localBounds.getMaxX() / 2);
					marker.setTranslateY(e.getY() - localBounds.getMaxY() / 2);
					mapPane.getChildren().add(marker);
				} else {
					double x = marker.getTranslateX();
					double y = marker.getTranslateY();
					Bounds localBounds = marker.localToScene(mapView.getBoundsInLocal());
					marker.setTranslateX(e.getX() - x - localBounds.getMaxX() / 2);
					marker.setTranslateY(e.getY() - y - localBounds.getMaxY() / 2);
				}
				*/		
			}
			
			// If double-click
			if (e.getClickCount() == 2) {
				// Creates a NodeDisplay that contains the node object
				NodeDisplay newNode = new NodeDisplay(display, e.getX(), e.getY(), 0);
				move(newNode, e.getX(),e.getY());
				
				newNode.addEventFilter(SelectNode.NODE_SELECTED, event -> {
					System.out.println("Node Selected");
					newNode.selectNode();
					nodeQueue.add(newNode);
					// Add selected node to selected ndoe queue
					
					//TODO stuff regarding info about the node clicked
					//if double-clicked
					//if in edit mode
					//if in add edge mode
					//	if 2 nodes are already selected when add edge is pressed,
					//	then create an edge between those two nodes
					//	if >2 nodes are selected, then edges will be added in order
					//	of selection
				});
				
				newNode.addEventFilter(SelectNode.NODE_DESELECTED, event -> {
					nodeQueue.remove(newNode);
				});
				
				// Add to the scene
				mapPane.getChildren().add(newNode);
			}
		});
		
		// TODO oh my god please i need to clean this up
		//increases the value of the zoomProperty to be added to scroll pane for magnification
		/*
		 * TODO Fix this up so that nodes scale based on zoom and that
		 * node coordinates don't mess up
		zoomProperty.addListener(new InvalidationListener(){
			@Override
			public void invalidated(Observable arg){
				mapView.setFitWidth(zoomProperty.get()*4);
				mapView.setFitHeight(zoomProperty.get()*3);
			}
		});
		
		//adds the scroll pane event filter which increases the magnification
		scroll.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>(){
			@Override
			public void handle(ScrollEvent e){
				if(e.getDeltaY() > 0){
					zoomProperty.set(zoomProperty.get()* 1.1);
				}else if(e.getDeltaY() < 0){
					zoomProperty.set(zoomProperty.get()/1.1);
				}
			}
		});
		*/
	}
	
	/**
	 * Re-translates whatever object to it's intended place on the map
	 * @param x
	 * @param y
	 */
	private void move(javafx.scene.Node obj, double x, double y){
		Bounds localBounds = obj.localToScene(mapView.getBoundsInLocal());
		obj.setTranslateX(x - localBounds.getMaxX() / 2);
		obj.setTranslateY(y - localBounds.getMaxY() / 2);
		
	}
	
	private void setMenuButtons(){
		loadMenuItem.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Map File");
			fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Text Files", "*.txt"),
			         new ExtensionFilter("All Files", "*.*"));
			 File selectedFile = fileChooser.showOpenDialog(window);
			 if (selectedFile != null) {
				 String inpath = selectedFile.getPath();
				 System.out.println(inpath);
				 FileIO.load(inpath, display);
				 //window.display(selectedFile);
			 }
			//Redisplay to appropriate map
			//Add nodes and edges to the map
			System.out.println(display.getGraph().returnEdgeById(0));
		});
		saveMenuItem.setOnAction(e -> {
			String path;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Map File");
			fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Text Files", "*.txt"),
			         new ExtensionFilter("All Files", "*.*"));
			File selectedFile = fileChooser.showSaveDialog(window);
			//selectedFile.getAbsolutePath();
			FileIO.save(selectedFile.getAbsolutePath(), display);
		});
	}
	
	/**
	 * Setup listeners/event handlers for editor buttons
	 */
	private void setEditorButtons(){
		/**
		 * Adds edges to selected nodes on map
		 */
		addEdge.setOnAction(e -> {
			SelectNode selectNodeEvent = new SelectNode(SelectNode.NODE_DESELECTED);
			if(!nodeQueue.isEmpty()){
				//System.out.println(nodeQueue.size());
				while(nodeQueue.size() > 1){
					NodeDisplay n = nodeQueue.poll();
					Node a = n.getNode();
					Node b = nodeQueue.peek().getNode();
					Coordinate aLoc = a.getCoordinate();
					Coordinate bLoc = b.getCoordinate();
					Graph g = display.getGraph();
					g.addEdge(a.getId(), b.getId());
					System.out.println("Edge size" + g.getEdges().size());
					Line l = new Line(aLoc.getX(), aLoc.getY(), 
							bLoc.getX(), bLoc.getY());
					l.setStroke(Color.POWDERBLUE);
					move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);;
					mapPane.getChildren().add(l);
					n.fireEvent(selectNodeEvent);
				}
				nodeQueue.remove().fireEvent(selectNodeEvent);;
			}
		});
		
		addNode.setOnAction(e -> {
			
		});
		
		editNode.setOnAction(e ->{
			Coordinate testing = new Coordinate(1, 2, 3);
			CoordinateDialogBox box = new CoordinateDialogBox(testing);
			box.display("Edit Node");
			System.out.println(String.valueOf(testing.getX()));
		});
		
		drawPathButton.setOnAction(e -> {
			NodeDisplay startNode = nodeQueue.poll();
			NodeDisplay endNode = nodeQueue.poll();
			//display.drawPath(startNode.node.getId(), endNode.node.getId());
			int idx = 0;
			Vector<Node> nodes = display.getGraph().getNodes();
			//System.out.println(nodes.size());
			Path p = new Path(startNode.node.getId(), endNode.node.getId());
			Graph g = display.getGraph();
			//System.out.println("Size graph " + g.getEdges().size());
			p.runAStar(nodes, g.getEdges()); //Change this later??
			ArrayList<Integer> idList = p.getRoute();
			System.out.println("Route size " + idList);
			while(idx < idList.size() - 1){
				Node a = nodes.get(idx);
				Node b = nodes.get(++idx);
				Coordinate aLoc = a.getCoordinate();
				Coordinate bLoc = b.getCoordinate();
				Line l = new Line(aLoc.getX(), aLoc.getY(), 
						bLoc.getX(), bLoc.getY());
				move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);

				mapPane.getChildren().add(l);
			}
			SelectNode selectNodeEvent = new SelectNode(SelectNode.NODE_DESELECTED);
			startNode.fireEvent(selectNodeEvent);
			endNode.fireEvent(selectNodeEvent);
		});

	}
}
