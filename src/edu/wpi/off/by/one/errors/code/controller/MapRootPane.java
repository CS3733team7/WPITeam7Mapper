package edu.wpi.off.by.one.errors.code.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.controller.menupanes.devtoolspanes.MapDevToolPane;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Edge;
import edu.wpi.off.by.one.errors.code.model.FileIO;
import edu.wpi.off.by.one.errors.code.model.Graph;
import edu.wpi.off.by.one.errors.code.model.Id;
import edu.wpi.off.by.one.errors.code.model.Map;
import edu.wpi.off.by.one.errors.code.model.Node;
import edu.wpi.off.by.one.errors.code.model.Path;

/**
 * Created by jules on 11/28/2015.
 * Edited by Kelly on 11/30/2015.
 */
public class MapRootPane extends AnchorPane{

	@FXML MainPane mainPane;
	
	@FXML StackPane mapPane;
	@FXML ImageView mapView;
	@FXML Button zoomInButton;
	@FXML Button zoomOutButton;
	@FXML VBox editorPane;
	@FXML Button drawPathDisplayButton;
	StackPane pathPane = new StackPane();

	//Where all the images and txt files should be
	String resourceDir = "/edu/wpi/off/by/one/errors/code/resources/";
	Bounds localBounds;
	Display display;												//Current display
	HashMap<String, Display> displayList;
    Queue<NodeDisplay> nodeQueue = new LinkedList<NodeDisplay>();	//Selected node queue
    Queue<EdgeDisplay> edgeQueue = new LinkedList<EdgeDisplay>();
    
    boolean isMapEditor = false;
    boolean isNodeEditor = false;
    boolean isEdgeEditor = false;
    boolean isEditMode = false;
    boolean isAddMode = false;		//Is editor currently adding nodes?
    boolean isDeleteMode = false;	//Is editor currently deleting nodes?

	
    public MapRootPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MapRootPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            initialize();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
    }
    
    public void setMainPane(MainPane m) { this.mainPane = m; }
    public MapRootPane getMapRootPane() { return this; }
    public HashMap<String, Display> getAllDisplays() {return displayList; }
    
    private void initialize(){
    	System.out.print("Main Controller Initialized.");
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
        
        //Setup event listeners for map
        setListeners();
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
			//Update the current map image
			updateMap(newdisplay.getMap());
            mapPane.getChildren().addAll(pathPane, mapView);
            localBounds = mapView.getBoundsInLocal();
		}
		String mapName = newdisplay.getMap().getName();
		if(mapName == null) mapName = newdisplay.getMap().getImgUrl();
		//Put the new display into the display list. Replace current if it already exists
		displayList.put(mapName, newdisplay);
		//Current display is now the new one
		this.display = newdisplay;
		Graph g = newdisplay.getGraph();
		//Draw new points onto map
		updateDisplay(g);
	}
	/**
	 * Internal updater/Helper function
	 * Draw all nodes and edges on map of a graph
	 * @param g 
	 */
	private void updateDisplay(Graph g){
		addEdgeDisplayFromList(g, g.getEdges());
		addNodeDisplayFromList(g.getNodes());
	}

	/**
	 * Updates the map and the current map view
	 * TODO Figure out a cleaner way to manage maps
	 * @param newmap
	 */
	private void updateMap(Map newmap){
		if(newmap.getImgUrl() != display.getMap().getImgUrl()){
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
			if(mapName == null) mapName = d.getMap().getImgUrl();
			displayList.put(mapName, d);
		}
	}
	
    /**
     * "Zoom" into map by pressing the "+" button
     * TODO scaling cuts off map at a certain point.
     * TODO Make it so that this works with scrolling on map
     * @param e event
     */
    /*
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
    /*
    private void zoomOutAction(ActionEvent e){
    	//mapPane.setPrefSize(mapPane.getWidth() * 0.8, mapPane.getHeight() * 0.8);
    	mapScrollPane.setVmax(mapScrollPane.getHeight() * 0.9);
    	mapScrollPane.setHmax(mapScrollPane.getWidth() * 0.9);
    	mapPane.setScaleY(mapPane.getScaleY() * 0.9);
    	mapPane.setScaleX(mapPane.getScaleX() * 0.9);
    }
    */
    /**
     * Sets up event listener functions for whenever user does something on the mapPane/mapView
     * 
     * TODO If user is in developer mode
     * 		* NODE: left click to add, right click to delete
     * 		* EDGE: right click to delete
     * 
     */
    private void setListeners(){
    	// Listen to when the user clicks on the map
    	mapView.setOnMouseClicked(e -> {
    		//If user did not click-drag on map
    		if(e.isStillSincePress()){
    			//TODO Add marker on map
    			
    			if (isAddMode && isNodeEditor && e.getButton() == MouseButton.PRIMARY) {
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
    	mapPane.addEventFilter(EditorEvent.EDIT_ELEMENT, e -> {
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
    	mapPane.addEventFilter(EditorEvent.DISPLAY_ITEM, e -> {
    		String eventName = e.getEventType().getName();
    		if(eventName == "MAP") isMapEditor = true;
    		else isMapEditor = false;
    		if(eventName == "NODE") isNodeEditor = true;
    		else isNodeEditor = false;
    		if(eventName == "EDGE") isEdgeEditor = true;
    		else isEdgeEditor = false;

    	});

    	mapPane.addEventFilter(EditorEvent.DRAW_EDGES, e -> {
    		if(isEdgeEditor) addEdgeDisplayFromQueue();
    	});
    }

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

	/**
	 * Add a NodeDisplay using existing Node
	 * @param nodes
	 */
	void addNodeDisplayFromList(Collection<Node> nodes){
		Node[] nodeArr = new Node[nodes.size()];
		nodes.toArray(nodeArr); // To avoid ConcurrentModificationException
		for(Node n : nodeArr){
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
	           System.out.println("Node Selected");
		       newNode.selectNode();
			   nodeQueue.add(newNode);
			   // Add selected node to selected node queue
			   mainPane.getNodeTool().displayNodeInfo(newNode);
	        });
	        
	        newNode.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
	            nodeQueue.remove(newNode);
	        });
	        
	        newNode.addEventFilter(EditorEvent.DELETE_NODE, event -> {
	        	System.out.println("Node deleted");
	        	Graph g = display.getGraph();
	        	Id id = newNode.getNode();
	        	Vector<Id> edges = g.returnNodeById(id).getEdgelist();
	        	for(int i = 0; i < edges.size(); i++)  g.deleteEdge(edges.get(i));
	        	g.deleteNode(id);
	        	mapPane.getChildren().remove(newNode);
	        	//remove edge display as well
	        	//right now this throws nullpointerexception.
	        	//updateDisplay(this.display, "NEW");
	        	//mapPane.getChildren().remove();
	        });
	        mapPane.getChildren().add(newNode);
	    }
	}

	/**
	 * Add a NodeDisplay using coordinates
	 * Use to add a non-existing NodeDisplay and Node to the display
	 * @param x
	 * @param y
	 */
	void addNodeDisplay(double x, double y){
		System.out.println("Added Node");
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
		        mainPane.getNodeTool().displayNodeInfo(newNode);
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
		            	//TODO @jules find a way to display edge information using new controller
		            	//ControllerMediator cm = ControllerMediator.getInstance();
		            	//cm.viewDisplayItem(e);
	            	}
	            });
	            e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
	            	//do sth;
	            	edgeQueue.remove(e);
	            });
	            
	            e.addEventFilter(EditorEvent.DELETE_EDGE, ev -> {
	            	mapPane.getChildren().remove(e);
	            	display.getGraph().deleteEdge(e.getEdge());
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
	    	Id aID, bID;
	    	try{
	    		aID = edge.getNodeA();
		    	bID = edge.getNodeB();
	    	} catch (NullPointerException e){
	    		graph.deleteEdge(edge.getId());
	    		continue;
	    	}
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
            	//TODO @jules same thing about viewing selected edge
            	//ControllerMediator cm = ControllerMediator.getInstance();
            	//cm.viewDisplayItem(e);
            });

            e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
            	//do sth;
            	edgeQueue.remove(e);
            });
            
            e.addEventFilter(EditorEvent.DELETE_EDGE, ev -> {
            	mapPane.getChildren().remove(e);
            	display.getGraph().deleteEdge(e.getEdge());
            });
     
	    }
	}
    /**
     * Draws a path from the last two selected nodes
     */
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
                l.setTranslateX((aLoc.getX() + bLoc.getX()) / 2);
                l.setTranslateY((aLoc.getY() + bLoc.getY()) / 2);
       
                move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);

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
