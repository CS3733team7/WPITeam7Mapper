<<<<<<< HEAD
package old.controller;
//package edu.wpi.off.by.one.errors.code.controller;
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
package edu.wpi.off.by.one.errors.code.controller;
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Vector;
<<<<<<< HEAD
//package old.controller;
=======
=======
package old.controller;
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.model.*;
<<<<<<< HEAD
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.*;
import edu.wpi.off.by.one.errors.code.model.Map;
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
=======
import edu.wpi.off.by.one.errors.code.model.Map;
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import old.controller.menucontrollers.menupanecontrollers.ControllerMediator;

import java.net.URL;
import java.util.*;

/**
 * The controller that manages the logic for most of the application view:
 * 		- Map
 * 		- Menu
 *		- Directions
 * 
 */
public class MainController implements Initializable{

	@FXML BorderPane root;

	@FXML StackPane mapPane;
	@FXML ImageView mapView;
	@FXML ScrollPane mapScrollPane;
	@FXML Button zoomInButton;
	@FXML Button zoomOutButton;
	@FXML VBox editorPane;
	@FXML Button drawPathDisplayButton;
	StackPane pathPane = new StackPane();

	//Where all the images and txt files should be
	String resourceDir = "/edu/wpi/off/by/one/errors/code/resources/";
<<<<<<< HEAD
	Bounds localBounds;
	Display display;												//Current display
	HashMap<String, Display> displayList;
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
	Bounds localBounds;
	Display display;												//Current display
	HashMap<String, Display> displayList;
=======

	Display display;												//Current display

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
    Queue<NodeDisplay> nodeQueue = new LinkedList<NodeDisplay>();	//Selected node queue
    Queue<EdgeDisplay> edgeQueue = new LinkedList<EdgeDisplay>();
    String editItem = "";
    boolean isMapEditor = false;
    boolean isNodeEditor = false;
    boolean isEdgeEditor = false;
    boolean isEditMode = false;
    boolean isAddMode = false;		//Is editor currently adding nodes?
    boolean isDeleteMode = false;	//Is editor currently deleting nodes?

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Register this controller to the mediator
		ControllerMediator.getInstance().registerMainController(this);
		System.out.print("Main Controller Initialized.");
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
		//Load all displays into application
		loadDisplays();
		//Load campus map from display list
        display = displayList.get("Campus Map");
		mapPane.getChildren().add(0, pathPane);
		//Set map image
        mapView.setImage(new Image(resourceDir + "maps/images/" + display.getMap().getImgUrl()));
		mapView.preserveRatioProperty().set(true);
		//Update local bounds of the map view
		localBounds = mapView.getBoundsInLocal();
		//Updates display with nodes/edges
		updateDisplay(display.getGraph());
		
		// center the mapScrollPane contents.
        mapScrollPane.setHvalue(mapScrollPane.getHmin() + (mapScrollPane.getHmax() - mapScrollPane.getHmin()) / 2);
        mapScrollPane.setVvalue(mapScrollPane.getVmin() + (mapScrollPane.getVmax() - mapScrollPane.getVmin()) / 2);
		
        //Setup event listeners for map
        setListeners();
<<<<<<< HEAD
=======
        
=======

		//Set up new display
		//TODO Make it so that the map preloads a display
		display = new Display();

        mapPane.getChildren().add(0, pathPane);

		// center the mapScrollPane contents.
		mapScrollPane.setHvalue(mapScrollPane.getHmin() + (mapScrollPane.getHmax() - mapScrollPane.getHmin()) / 2);
		mapScrollPane.setVvalue(mapScrollPane.getVmin() + (mapScrollPane.getVmax() - mapScrollPane.getVmin()) / 2);

		Image map = new Image(resourceDir + "campusmap.png");
		mapView.setImage(map);
		mapView.preserveRatioProperty().set(true);
		setListeners();

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	}

	/**
	 * Gets current display from MainPane
	 * @return Current Display
	 */
	public Display getDisplay(){
		return this.display;
	}

	/**
	 * External updater
	 * Updates current display to show or append a new graph/map
	 * @param newdisplay New/Updated Display
	 * @param option Additional options to clear first or append onto current
	 */
	public void updateDisplay(Display newdisplay, String option){
		// If the option is to clear the map first, do so
		// otherwise, keep current content and just append
		if(option.equals("NEW")){
			mapPane.getChildren().clear();
<<<<<<< HEAD
            mapPane.getChildren().addAll(pathPane, mapView);
            localBounds = mapView.getBoundsInLocal();
			mapPane.getChildren().addAll(mapView);
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
            mapPane.getChildren().addAll(pathPane, mapView);
            localBounds = mapView.getBoundsInLocal();
=======
			mapPane.getChildren().addAll(mapView);
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
		}
		String mapName = newdisplay.getMap().getName();
		if(mapName == null) mapName = newdisplay.getMap().getImgUrl();
		//Put the new display into the display list. Replace current if it already exists
		displayList.put(mapName, newdisplay);
		//Current display is now the new one
		this.display = newdisplay;
		Graph g = newdisplay.getGraph();
		//Update the current map image
		updateMap(newdisplay.getMap());
		//Draw new points onto map
		updateDisplay(g);
	}
	/**
	 * Internal updater/Helper function
	 * Draw all nodes and edges on map of a graph
	 * @param g 
	 */
	private void updateDisplay(Graph g){
		addNodeDisplayFromList(g.getNodes());
		addEdgeDisplayFromList(g, g.getEdges());
	}

	/**
	 * Updates the map and the current map view
	 * TODO Figure out a cleaner way to manage maps
	 * @param newmap
	 */
	private void updateMap(Map newmap){
		mapView.setImage(new Image(resourceDir + newmap.getImgUrl()));
		if(newmap.getImgUrl() != display.getMap().getImgUrl()){
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
            mapView.setImage(new Image(resourceDir + "maps/images/" +  newmap.getImgUrl()));
        }
	}
	
	/**
	 * Preloads all the txt files in resources package and
	 * stores them as display objects
	 */
	private void loadDisplays(){
		
		displayList = new HashMap<String, Display>();
		// TODO the best way to do this is to look at a folder in the resources directory that holds all the txt files
		// get each file name, and then store them in a string array. Then begin to load the display objects into a Hash
		String[] fileNames = {"fullCampusMap", "projCenterFloorOne", "projCenterFloorTwo3"};
		//This should be done in FileIO...?
		File resources = new File("src" + resourceDir + "maps/txtfiles");
		String[] lof = resources.list();
		System.out.println(lof);
		for(String file : lof){
			String path = "src" + resourceDir + "maps/txtfiles/" + file;
			Display d = FileIO.load(path, null);
			String mapName = d.getMap().getName();
			if(mapName == null) mapName = file;
			displayList.put(mapName, d);
		}
	}
<<<<<<< HEAD

=======
	
=======
			mapView.setImage(new Image(resourceDir + newmap.getImgUrl()));
		}
	}

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
    /**
     * "Zoom" into map by pressing the "+" button
     * TODO scaling cuts off map at a certain point.
     * TODO Make it so that this works with scrolling on map
     * @param e event
     */
    @FXML
    private void zoomInAction(ActionEvent e){
    	//mapPane.setPrefSize(mapPane.getWidth() * 1.2, mapPane.getHeight() * 1.2);
    	mapScrollPane.setVmax(mapScrollPane.getHeight() * 1.1);
    	mapScrollPane.setHmax(mapScrollPane.getWidth() * 1.1);
    	mapPane.setScaleY(mapPane.getScaleY() * 1.1);
    	mapPane.setScaleX(mapPane.getScaleX() * 1.1);
    }
    /**
     * "Zoom" out of map by pressing the "-" button
     * TODO scaling cuts off map at a certain point.
     * TODO Make it so that this works with scrolling on map
     * @param e event
     */
    @FXML
    private void zoomOutAction(ActionEvent e){
    	//mapPane.setPrefSize(mapPane.getWidth() * 0.8, mapPane.getHeight() * 0.8);
    	mapScrollPane.setVmax(mapScrollPane.getHeight() * 0.9);
    	mapScrollPane.setHmax(mapScrollPane.getWidth() * 0.9);
    	mapPane.setScaleY(mapPane.getScaleY() * 0.9);
    	mapPane.setScaleX(mapPane.getScaleX() * 0.9);
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
    
    /**
     * Sets up event listener functions for whenever user does something on the mapPane/mapView
     * 
     * TODO If user is in developer mode
     * 		* NODE: left click to add, right click to delete
     * 		* EDGE: right click to delete
     * 
     */
<<<<<<< HEAD
=======
=======

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
    private void setListeners(){
    	// Listen to when the user clicks on the map
    	mapView.setOnMouseClicked(e -> {
    		//If user did not click-drag on map
    		if(e.isStillSincePress()){
    			//TODO Add marker on map
    			if (isAddMode && isNodeEditor) {
	                addNodeDisplay(e.getX(), e.getY());
	            }
	    		else if (isEditMode && isNodeEditor){
	    			if(!nodeQueue.isEmpty()){
	    				System.out.println("Editing node");
	    				NodeDisplay n = nodeQueue.poll();
	    				Graph g = display.getGraph();
	    				//Coordinate currentCoord = g.returnNodeById(n.getNode()).getCoordinate();
	    				//Matrix transform = new Matrix(currentCoord);
	    				//Coordinate newCoord = transform.transform(new Coordinate((float) e.getX(), (float) e.getY()));
	    				//n.setCenterX(newCoord.getX());
	    				//n.setCenterY(newCoord.getY());
	    				n.setCenterX(e.getX() - localBounds.getMaxX()/2);
	    				n.setCenterY(e.getY() - localBounds.getMaxY()/2);
	    				display.getGraph().editNode(n.getNode(),
	    						new Coordinate((float) e.getX(), (float)e.getY()));
	    				SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
	    				n.fireEvent(selectNodeEvent);
	    			}
	    		}
    		}
    		//If user double-click
    		else{

    		}
    	});
    	//Listen if editor pane sent out an Add/Edit/Delete event
    	//TODO remove later to work with new way
    	root.addEventFilter(EditorEvent.EDIT_ELEMENT, e -> {
    		String eventName = e.getEventType().getName();
    		if(eventName == "ADD") isAddMode = true;
    		else isAddMode = false;
    		if(eventName == "EDIT") isEditMode = true;
    		else isEditMode = false;
    		if(eventName == "DELETE") isDeleteMode = true;
    		else isDeleteMode = false;
    		System.out.println(e.getEventType());
    	});
    	//Listen if editor pane sent out an Map/Node/Edge event
    	//TODO do something with it. right now it only gets the name
    	//		remove later to work with new way
    	root.addEventFilter(EditorEvent.DISPLAY_ITEM, e -> {
    		String eventName = e.getEventType().getName();
    		if(eventName == "MAP") isMapEditor = true;
    		else isMapEditor = false;
    		if(eventName == "NODE") isNodeEditor = true;
    		else isNodeEditor = false;
    		if(eventName == "EDGE") isEdgeEditor = true;
    		else isEdgeEditor = false;

    	});

    	root.addEventFilter(EditorEvent.DRAW_EDGES, e -> {
    		if(isEdgeEditor) addEdgeDisplayFromQueue();
    	});
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
    
=======
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a

    /**
     * Re-translates whatever object to it's intended place on the map
     * @param x
     * @param y
     */
    public void move(javafx.scene.Node obj, double x, double y){
        Bounds localBounds = mapView.getBoundsInLocal();
        System.out.println("MOVING");
        System.out.println(localBounds.getMinX() + " " + localBounds.getMinY());
        System.out.println(localBounds.getMaxX() + " " + localBounds.getMaxY());
        obj.setTranslateX(x - localBounds.getMaxX() / 2);
        obj.setTranslateY(y - localBounds.getMaxY() / 2);

    }

<<<<<<< HEAD
=======
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	/**
	 * Add a NodeDisplay using existing Node
	 * @param nodes
	 */
	void addNodeDisplayFromList(Collection<Node> nodes){
		Node[] nodeArr = new Node[nodes.size()];
		nodes.toArray(nodeArr); // To avoid ConcurrentModificationException
		for(Node n : nodeArr){
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
			Coordinate c = n.getCoordinate();
			//addNodeDisplay(c.getX(), c.getY());
			
			double tx = c.getX() - localBounds.getMaxX()/2;
			double ty = c.getY() - localBounds.getMaxY()/2;
			
			NodeDisplay newNode = new NodeDisplay(display, n.getId(),
					new SimpleDoubleProperty(tx), 
					new SimpleDoubleProperty(ty),
					new SimpleDoubleProperty(0));
			newNode.setTranslateX(tx);
			newNode.setTranslateY(ty);
		    newNode.centerXProperty().addListener(e -> {
		    	newNode.setTranslateX(newNode.getCenterX());
		    });
		    newNode.centerYProperty().addListener(e -> {
		    	newNode.setTranslateY(newNode.getCenterY());
		    });
	        newNode.addEventFilter(SelectEvent.NODE_SELECTED, event -> {
	            if(isDeleteMode && isNodeEditor){
		        	System.out.println("Node deleted");
		        	Id id = newNode.getNode();
		        	display.getGraph().deleteNode(id);
		        	mapPane.getChildren().remove(newNode);
		        	System.out.println(display.getGraph().getNodes().size());
		        } else {
		        	System.out.println("Node Selected");
		        	newNode.selectNode();
			        nodeQueue.add(newNode);
			        // Add selected node to selected node queue
		        }
	        });
	        
	        newNode.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
	            nodeQueue.remove(newNode);
	        });
	        mapPane.getChildren().add(newNode);
	    }
<<<<<<< HEAD
=======
=======
			NodeDisplay nd = new NodeDisplay(display, n.getId());
			Coordinate c = n.getCoordinate();
			move(nd, c.getX(), c.getY());

			nd.addEventFilter(SelectEvent.NODE_SELECTED, event -> {
				System.out.println("Node Selected");
				nd.selectNode();
				nodeQueue.add(nd);
			});

			nd.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
				nodeQueue.remove(nd);
			});
			mapPane.getChildren().add(nd);
		}
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	}

	/**
	 * Add a NodeDisplay using coordinates
	 * Use to add a non-existing NodeDisplay and Node to the display
	 * @param x
	 * @param y
	 */
	void addNodeDisplay(double x, double y){
		System.out.println("Added Node");
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
		double tx = x - (localBounds.getMaxX() / 2);
        double ty = y - (localBounds.getMaxY() / 2);
		
		NodeDisplay newNode = new NodeDisplay(display, 
				new SimpleDoubleProperty(tx), 
				new SimpleDoubleProperty(ty),
				new SimpleDoubleProperty(0));
		newNode.setTranslateX(tx);
		newNode.setTranslateY(ty);
	    newNode.centerXProperty().addListener(e -> {
	    	newNode.setTranslateX(newNode.getCenterX());
	    });
	    newNode.centerYProperty().addListener(e -> {
	    	newNode.setTranslateY(newNode.getCenterY());
	    });
<<<<<<< HEAD

=======
=======

		NodeDisplay newNode = new NodeDisplay(display, x, y, 0);

	    move(newNode, x, y);

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	    newNode.addEventFilter(SelectEvent.NODE_SELECTED, event -> {

	        if(isDeleteMode && isNodeEditor){
	        	System.out.println("Node deleted");
	        	Id id = newNode.getNode();
	        	display.getGraph().deleteNode(id);
	        	mapPane.getChildren().remove(newNode);
	        	System.out.println(display.getGraph().getNodes().size());
	        } else {
	        	System.out.println("Node Selected");
	        	newNode.selectNode();
		        nodeQueue.add(newNode);
		        System.out.println(newNode.getCenterX() + " " + newNode.getCenterY());
		        // Add selected node to selected node queue
	        }

	        //TODO stuff regarding info about the node clicked
	        //if double-clicked
	        //if in edit mode
	        //if in add edge mode
	        //	if 2 nodes are already selected when add edge is pressed,
	        //	then create an edge between those two nodes
	        //	if >2 nodes are selected, then edges will be added in order
	        //	of selection
	    });

	    newNode.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
	        nodeQueue.remove(newNode);
	    });

	    //newNode.visibleProperty().bind(showNodes.selectedProperty());

	    // Add to the scene
	    mapPane.getChildren().add(newNode);
	}

	/**
	 * Add EdgeDisplays from selected NodeQueue
	 * Use to add a non-existing EdgeDisplay and Edge to the display
	 */
	void addEdgeDisplayFromQueue(){
		SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
	    if(!nodeQueue.isEmpty()){
	        //System.out.println(nodeQueue.size());
	        while(nodeQueue.size() > 1){
	            NodeDisplay aND = nodeQueue.poll();
	            NodeDisplay bND = nodeQueue.peek();
	            Graph g = display.getGraph();
	            Node a = g.returnNodeById(aND.getNode());
	            Node b = g.returnNodeById(bND.getNode());
	            DoubleProperty aLocX, aLocY, bLocX, bLocY;
	            try{
	            	aLocX = aND.centerXProperty();
	                aLocY = aND.centerYProperty();
	                bLocX = bND.centerXProperty();
	                bLocY = bND.centerYProperty();
	            }
	            catch(NullPointerException exception){
	                System.out.println("a thing broke");
	                break;
	            }


	            Id newEdge = g.addEdgeRint(a.getId(), b.getId());
	            EdgeDisplay e = new EdgeDisplay(display, newEdge,
	        			aLocX, aLocY,
	                    bLocX, bLocY);
	            e.setStroke(Color.BLUE);
	            e.setTranslateX((aLocX.get() + bLocX.get())/2);
	            e.setTranslateY((aLocY.get() + bLocY.get())/2);
	            e.startXProperty().addListener(ev -> {
	            	e.setTranslateX((aLocX.get() + bLocX.get())/2);
	            });
	            e.startYProperty().addListener(ev -> {
	            	e.setTranslateY((aLocY.get() + bLocY.get())/2);
	            });
	            e.endXProperty().addListener(ev -> {
	            	e.setTranslateX((aLocX.get() + bLocX.get())/2);
	            });
	            e.endYProperty().addListener(ev -> {
	            	e.setTranslateY((aLocY.get() + bLocY.get())/2);
	            });
	            mapPane.getChildren().add(e);
<<<<<<< HEAD
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	            aND.fireEvent(selectNodeEvent);
	            
	            e.addEventFilter(SelectEvent.EDGE_SELECTED, ev -> {
	            	if(isDeleteMode && isEdgeEditor){
	            		System.out.println("Edge deleted");
	    	        	Id id = e.getEdge();
	    	        	display.getGraph().deleteEdge(id);
	    	        	mapPane.getChildren().remove(e);
	            	} else {
	            		System.out.println("Edge selected");
		            	e.selectEdge();
		            	edgeQueue.clear();
		            	edgeQueue.add(e);
		            	ControllerMediator cm = ControllerMediator.getInstance();
		            	cm.viewDisplayItem(e);
	            	}
<<<<<<< HEAD
=======
=======
	            n.fireEvent(selectNodeEvent);

	            e.addEventFilter(SelectEvent.EDGE_SELECTED, ev -> {
	            	System.out.println("Edge selected");
	            	e.selectEdge();
	            	ControllerMediator cm = ControllerMediator.getInstance();
	            	//cm.viewDisplayItem(e);
>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	            });
	            e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
	            	//do sth;
<<<<<<< HEAD
	            	edgeQueue.remove(e);
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
	            	edgeQueue.remove(e);
=======

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	            });
	        }
	        nodeQueue.remove().fireEvent(selectNodeEvent);;
	    } else {
	    	//TODO: Display message in editor that says no nodes are being selected
	    }
	}

	/**
	 * Add an EdgeDisplay using an existing Edge list and Graph
	 * @param graph
	 * @param edges
	 */
	void addEdgeDisplayFromList(Graph graph, Vector<Edge> edges){
		Edge[] edgeArr = new Edge[edges.size()];
		edges.toArray(edgeArr); // To avoid ConcurrentModificationException
	    for(Edge edge : edgeArr){
	    	Id aID = edge.getNodeA();
	    	Id bID = edge.getNodeB();
	    	Node a = graph.returnNodeById(aID);
	        Node b = graph.returnNodeById(bID);
	        Coordinate aLoc = a.getCoordinate();
	        Coordinate bLoc = b.getCoordinate();
	        DoubleProperty aLocX, aLocY, bLocX, bLocY;
	        aLocX = new SimpleDoubleProperty(aLoc.getX() - localBounds.getMaxX()/2);
	        aLocY = new SimpleDoubleProperty(aLoc.getY() - localBounds.getMaxY()/2);
	        bLocX = new SimpleDoubleProperty(bLoc.getX() - localBounds.getMaxX()/2);
	        bLocY = new SimpleDoubleProperty(bLoc.getY() - localBounds.getMaxY()/2);
	        //System.out.println("Edge size" + g.getEdges().size());
	        EdgeDisplay e = new EdgeDisplay(display, aID, bID,
	        		aLocX, aLocY, bLocX, bLocY);
            
            e.setStroke(Color.BLUE);
            e.setTranslateX((aLocX.get() + bLocX.get())/2);
            e.setTranslateY((aLocY.get() + bLocY.get())/2);
            e.startXProperty().addListener(ev -> {
            	e.setTranslateX((aLocX.get() + bLocX.get())/2);
            });
            e.startYProperty().addListener(ev -> {
            	e.setTranslateY((aLocY.get() + bLocY.get())/2);
            });
            e.endXProperty().addListener(ev -> {
            	e.setTranslateX((aLocX.get() + bLocX.get())/2);
            });
            e.endYProperty().addListener(ev -> {
            	e.setTranslateY((aLocY.get() + bLocY.get())/2);
            });
            mapPane.getChildren().add(e);

            e.addEventFilter(SelectEvent.EDGE_SELECTED, ev -> {
            	System.out.println("Edge selected");
            	e.selectEdge();
            	edgeQueue.clear();
            	edgeQueue.add(e);
            	ControllerMediator cm = ControllerMediator.getInstance();
            	cm.viewDisplayItem(e);
            });

            e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
            	//do sth;
            	edgeQueue.remove(e);
            });
     
	    }
	}
<<<<<<< HEAD
    /**
     * Draws a path from the last two selected nodes
     */
=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
    /**
     * Draws a path from the last two selected nodes
     */
=======

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
	public void drawPath(){
		pathPane.getChildren().clear();
        NodeDisplay startNode = nodeQueue.poll();
        NodeDisplay endNode = nodeQueue.poll();
        if(startNode != null && endNode != null){
            //display.drawPath(startNode.node.getId(), endNode.node.getId());
            int idx = 0;
            Vector<Node> nodes = display.getGraph().getNodes();
            //System.out.println(nodes.size());

            Path p = new Path(startNode.getNode(), endNode.getNode());
            Graph g = display.getGraph();
            //System.out.println("Size graph " + g.getEdges().size());
            p.runAStar(g); //Change this later??
            ArrayList<Id> idList = p.getRoute();
            System.out.println("Route size " + idList.size());
            while(idx < idList.size() - 1){
            	Node a = g.returnNodeById(idList.get(idx));
                Node b = g.returnNodeById(idList.get(++idx));
                Coordinate aLoc = a.getCoordinate();
                Coordinate bLoc = b.getCoordinate();
                Line l = new Line(aLoc.getX(), aLoc.getY(),
                                  bLoc.getX(), bLoc.getY());
                l.setStrokeWidth(3.0);
<<<<<<< HEAD
                l.setTranslateX((aLoc.getX() + bLoc.getX()) / 2);
                l.setTranslateY((aLoc.getY() + bLoc.getY()) / 2);
       
                move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);

=======
<<<<<<< HEAD:src/edu/wpi/off/by/one/errors/code/controller/MainController.java
                l.setTranslateX((aLoc.getX() + bLoc.getX()) / 2);
                l.setTranslateY((aLoc.getY() + bLoc.getY()) / 2);
                
=======
                move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);

>>>>>>> 887e93653077ab7a1558473fa8bdf42a3c8a1045:src/old/controller/MainController.java
>>>>>>> 47f75507625dfb7d7b4faefdb9cd965584256d6a
                pathPane.getChildren().add(l);
                pathPane.toFront();
                pathPane.setMouseTransparent(true);
            }
            SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
            startNode.fireEvent(selectNodeEvent);
            endNode.fireEvent(selectNodeEvent);
        }
	}
}
