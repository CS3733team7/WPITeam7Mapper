package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.model.Coordinate;
import edu.wpi.off.by.one.errors.code.model.Display;
import edu.wpi.off.by.one.errors.code.model.Edge;
import edu.wpi.off.by.one.errors.code.model.FileIO;
import edu.wpi.off.by.one.errors.code.model.Graph;
import edu.wpi.off.by.one.errors.code.model.Id;
import edu.wpi.off.by.one.errors.code.model.Map;
import edu.wpi.off.by.one.errors.code.model.Matrix;
import edu.wpi.off.by.one.errors.code.model.Node;
import edu.wpi.off.by.one.errors.code.model.Path;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Created by jules on 11/28/2015.
 * Edited by Kelly on 11/30/2015.
 */
public class MapRootPane extends AnchorPane{

	@FXML Button zoomInButton;
	@FXML Button zoomOutButton;
	@FXML Button rotateLeftButton;
	@FXML Button rotateRightButton;
	
	@FXML StackPane mapPane;
	@FXML StackPane nodeLayer;
	@FXML Pane edgeLayer;
	@FXML StackPane pathPane;
	
	public Coordinate translate = new Coordinate(0.0f, 0.0f, 1.0f);;
	Coordinate release = new Coordinate(0, 0, 0);
	public float rot = 0.0f;
	public float zoom = 2.0f;
	Matrix view;
	Matrix invview;
	Matrix lastview;
	
	@FXML public Canvas canvas;
	public int currentLevel = 1;
	private Path p;
	
	//Where all the images and txt files should be
	String resourceDir = "/edu/wpi/off/by/one/errors/code/resources/";
	private String filePath = "src" + resourceDir + "maps/txtfiles/fullCampusMap.txt";
	
	Display display;												//Current display
	
	Queue<NodeDisplay> nodeQueue = new LinkedList<NodeDisplay>();	//Selected node queue
    Queue<EdgeDisplay> edgeQueue = new LinkedList<EdgeDisplay>();
    ArrayList<Id> currentRoute = new ArrayList<Id>();
    public NodeDisplay currentPivot = null;
    boolean isMapEditor = false;
    public boolean isNodeEditor = false;
    public boolean isEdgeEditor = false;
    public boolean isEditMode = false;
    public boolean isAddMode = false;		//Is editor currently adding nodes?
    public boolean isDeleteMode = false;	//Is editor currently deleting nodes?
    public boolean isMultiSelectNodes = false;
    
    boolean isctrl = false;

    
    public MapRootPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MapRootPane.fxml"));
        
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
        ControllerSingleton.getInstance().registerMapRootPane(this);
        initialize();
    }
    
    public MapRootPane getMapRootPane() { return this; }
    public String getFilePath() {return this.filePath; }
    
    public void updateCanvasSize(double width, double height){
    	//mapPane.setPrefSize(width, height);
    	canvas.setHeight(height);
    	canvas.setWidth(width);
    	render();
    }
    
    private void initialize(){
    	//Load campus map from display list
		display = FileIO.load("src" + resourceDir + "maps/txtfiles/fullCampusMap.txt", display);
        pathPane.setMouseTransparent(true);
        nodeLayer.setMouseTransparent(false);
        edgeLayer.setPickOnBounds(false);
        nodeLayer.setPickOnBounds(false);
		nodeLayer.setAlignment(Pos.TOP_LEFT);
		//Set map image
		Coordinate lastdragged = new Coordinate(0);
		Coordinate mydragged = new Coordinate(0);
		updateDisplay(display.getGraph());
        
        //Setup event listeners for map
        setListeners();
		mapPane.setOnMousePressed(e -> {
			if(!e.isSecondaryButtonDown()) return;
			 lastview = invview;
			 Coordinate in = new Coordinate((float)e.getX(), (float)e.getY());
			 Coordinate sin = lastview.transform(in);
			 mydragged.setAll(in.getX(), in.getY(), 0);
			 lastdragged.setAll(sin.getX(), sin.getY(), 0);
	     });
		mapPane.setOnMouseDragged(e -> {
			if(!e.isSecondaryButtonDown()) return;
			Coordinate sin = new Coordinate((float)e.getX(), (float)e.getY());
			Coordinate in = lastview.transform(sin);
			if(e.isControlDown()){
				zoom *= (1.0f - 0.01*(sin.getY() - mydragged.getY()));
				render();
				lastview = invview;
			} else {
				Coordinate delta = new Coordinate(in.getX() - lastdragged.getX(), in.getY() - lastdragged.getY());
				translate.setAll((float) translate.getX() + delta.getX(), (float)translate.getY() + delta.getY(), translate.getZ());
				render();
			}
			lastdragged.setAll(in.getX(), in.getY(), 0);
			mydragged.setAll(sin.getX(), sin.getY(), 0);
			
		});
		
		mapPane.setOnMouseReleased(e -> {
	    	 release.setAll((float)e.getX(), (float)e.getY(), 0);
	     });
		
		mapPane.setOnScroll(v -> {
			if(v.getDeltaY() > 0) {
				zoom*=1.1; render();
			}
			else { zoom*=0.9; render(); }
		});
 
		view = new Matrix();
		invview = new Matrix();
        render();
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
		updateDisplay(this.display.getGraph());
	}
	/**
	 * Handles all the zoom/rotation/translation of objects on the map
	 * and draws them onto map
	 */
	public void render(){
		if(zoom < 0.4f) zoom = Math.abs(zoom);
		if(zoom < 0.4f) zoom = 0.4f;
		else if(zoom > 11.4f) zoom = 11.4f;
		view = new Matrix().translate(new Coordinate((float)canvas.getWidth()/2.0f, (float)canvas.getHeight()/2.0f)).rotate(rot, 0.0f, 0.0f, 1.0f).scale(zoom).translate(new Coordinate(translate.getX(), translate.getY(), translate.getZ()));
		invview = new Matrix(new Coordinate(-1.0f * translate.getX(), -1.0f *translate.getY(), -1.0f * translate.getZ())).scale(1.0/zoom).rotate(-rot, 0.0, 0.0, 1.0).translate(new Coordinate((float)canvas.getWidth()/-2.0f, (float)canvas.getHeight()/-2.0f));
		//grab graphics context
		GraphicsContext mygc = canvas.getGraphicsContext2D();
		mygc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		ArrayList<Map> mlist = display.getMaps();
		
		for(Map m : mlist){
			mygc.save();
			if(m == null) continue;

			if(m.getImage() == null) continue;
			if(currentLevel != 1 && m.getName().equals("Campus Map")) mygc.setGlobalAlpha(0.4);
			else mygc.setGlobalAlpha(1);
			if(translate.getZ() > m.getCenter().getZ() + 0.1 || translate.getZ() < m.getCenter().getZ() - 0.1){
				if(!m.getName().equals("Campus Map")) continue;
			}
			Coordinate c = view.transform(m.getCenter());
			//mygc.translate(c.getX(), c.getY());
			Rotate r = new Rotate(m.getRotation() + rot, 0, 0);
	        mygc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx() + c.getX(), r.getTy() + c.getY());
	        mygc.scale(zoom * m.getScale(), zoom * m.getScale());
			
			mygc.drawImage(m.getImage(), 0, 0);
			mygc.restore();
		}
		for(javafx.scene.Node np: nodeLayer.getChildren()){
			NodeDisplay nd = (NodeDisplay)np;
			if(nd == null) continue;
			Node n = display.getGraph().returnNodeById(nd.getNode());
			if(n == null) continue;
			if(translate.getZ() > n.getCoordinate().getZ() + 0.1 || translate.getZ() < n.getCoordinate().getZ() - 0.1){
				np.setVisible(false);
				np.setMouseTransparent(true);
				continue;
			} else {
				np.setVisible(true);
				np.setMouseTransparent(false);
				if(n == null){ nodeLayer.getChildren().remove(np); continue; }
				Coordinate nc = view.transform(n.getCoordinate());
				nd.setCenterX(nc.getX()- 5.0f);
				nd.setCenterY(nc.getY()- 5.0f);
			}
		}
		if(isEditMode){
			edgeLayer.setVisible(true);
			mygc.save();
			Set<javafx.scene.Node> toRemove = new HashSet<>();
			for(javafx.scene.Node ep: edgeLayer.getChildren()){
				EdgeDisplay ed = (EdgeDisplay)ep;
				if(ed == null) continue;
				Edge e = display.getGraph().returnEdgeById(ed.getEdge());
				if(e == null){ 
					toRemove.add(ep); 
					continue; 
				}
				Node A = display.getGraph().returnNodeById(e.getNodeA());
				Node B = display.getGraph().returnNodeById(e.getNodeB());
				if(A == null || B == null){
					display.getGraph().deleteEdge(e.getId());
					toRemove.add(ep); 
					continue; 
				}
				if((translate.getZ() > A.getCoordinate().getZ() + 0.1 || translate.getZ() < A.getCoordinate().getZ() - 0.1) && (translate.getZ() > B.getCoordinate().getZ() + 0.1 || translate.getZ() < B.getCoordinate().getZ() - 0.1)){
					ep.setVisible(false);
					ep.setMouseTransparent(true);
					continue;
				} else {
					ep.setVisible(true);
					ep.setMouseTransparent(false);
					Coordinate ac = view.transform(A.getCoordinate());
					Coordinate bc = view.transform(B.getCoordinate());
					ed.setStartX(ac.getX());
					ed.setStartY(ac.getY());
					ed.setEndX(bc.getX());
					ed.setEndY(bc.getY());
				}
			}
			edgeLayer.getChildren().removeAll(toRemove);
			
//			for(Edge e : elist){
//				if(e == null) continue;
//				Node A = display.getGraph().returnNodeById(e.getNodeA());
//				Node B = display.getGraph().returnNodeById(e.getNodeB());
//				if(A == null || B == null){
//					display.getGraph().deleteEdge(e.getId());
//					continue; 
//				}
//				if((translate.getZ() > A.getCoordinate().getZ() + 0.1 || translate.getZ() < A.getCoordinate().getZ() - 0.1) && (translate.getZ() > B.getCoordinate().getZ() + 0.1 || translate.getZ() < B.getCoordinate().getZ() - 0.1)){
//					continue;
//				}
//				Coordinate ac = view.transform(A.getCoordinate());
//				Coordinate bc = view.transform(B.getCoordinate());
//				mygc.setLineWidth(5.0f);
//				mygc.setFill(Color.AQUA);
//				mygc.setStroke(Color.AQUA);
//				mygc.strokeLine(ac.getX(), ac.getY(), bc.getX(), bc.getY());
//			} 
			mygc.restore();
		} else edgeLayer.setVisible(false);
		Node last = null;
		for(Id id : currentRoute){
			mygc.save();
			Node A = display.getGraph().returnNodeById(id);
			if(A == null) continue;
			if(last == null){
				last = A;
				continue;
			}
			if((translate.getZ() > A.getCoordinate().getZ() + 0.1 || translate.getZ() < A.getCoordinate().getZ() - 0.1) && (translate.getZ() > last.getCoordinate().getZ() + 0.1 || translate.getZ() < last.getCoordinate().getZ() - 0.1)){
				last = A;
				continue;
			}

			Coordinate ac = view.transform(A.getCoordinate());
			Coordinate bc = view.transform(last.getCoordinate());
			mygc.setLineWidth(5.0f);
			mygc.setFill(Color.RED);
			mygc.setStroke(Color.RED);
			mygc.strokeLine(ac.getX(), ac.getY(), bc.getX(), bc.getY());
			last = A;
			mygc.restore();
		}

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
     * Sets up event listener functions for whenever user does something on the mapPane/mapView
     * 
     * TODO If user is in developer mode
     * 		* NODE: left click to add, right click to delete
     * 		* EDGE: right click to delete
     * 
     */
    private void setListeners(){
    	// Listen to when the user clicks on the map
    	canvas.setOnMouseClicked(e -> {
    		//If user did not click-drag on map
    		if(e.isStillSincePress()){
    			//TODO Add marker on map
    			if (isEditMode && e.getButton() == MouseButton.PRIMARY) {
    				addNodeDisplay(e.getX(), e.getY());
	            }
	    		else if (isNodeEditor){
	    			// TODO MAKE THE CLICK DRAGGY THINGN
	    			if(!nodeQueue.isEmpty()){
	    				////System.out.println("Editing node");
	    				NodeDisplay n = nodeQueue.poll();
	    				Graph g = display.getGraph();
	    				//Coordinate currentCoord = g.returnNodeById(n.getNode()).getCoordinate();
	    				//Matrix transform = new Matrix(currentCoord);
	    				//Coordinate newCoord = transform.transform(new Coordinate((float) e.getX(), (float) e.getY()));
	    				//n.setCenterX(newCoord.getX());
	    				//n.setCenterY(newCoord.getY());
	    				n.setCenterX(e.getX());
	    				n.setCenterY(e.getY());
	    				display.getGraph().editNode(n.getNode(),
	    						new Coordinate((float) e.getX(), (float)e.getY(), (float) currentLevel));
	    				SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
	    				n.fireEvent(selectNodeEvent);
	    			}
	    		}
    		}
    	});
    	edgeLayer.addEventFilter(EditorEvent.DRAW_EDGES, e -> {
    		if(isEdgeEditor) addEdgeDisplayFromQueue();
    	});
    }

	/**
	 * Add a NodeDisplay using existing Node
	 * @param nodes
	 */
	void addNodeDisplayFromList(Collection<Node> nodes){
		Node[] nodeArr = new Node[nodes.size()];
		nodes.toArray(nodeArr); // To avoid ConcurrentModificationException
		for(Node n : nodeArr){
			if(n == null) continue;
			Coordinate c = n.getCoordinate();
			NodeDisplay newNode = new NodeDisplay(display, n.getId(),
					new SimpleDoubleProperty(c.getX()), 
					new SimpleDoubleProperty(c.getY()),
					new SimpleDoubleProperty(c.getZ()));
			addNodeDisplayListeners(newNode);
	        nodeLayer.getChildren().add(newNode);
	        render();
	    }
	}

	/**
	 * Add a NodeDisplay using coordinates
	 * Use to add a non-existing NodeDisplay and Node to the display
	 * @param x
	 * @param y
	 */
	void addNodeDisplay(double x, double y){
		float tx = (float) x;
        float ty = (float) y;
		Coordinate c = invview.transform(new Coordinate(tx, ty));
		NodeDisplay newNode = new NodeDisplay(display, 
				new SimpleDoubleProperty(c.getX()), 
				new SimpleDoubleProperty(c.getY()),
				new SimpleDoubleProperty(currentLevel));
		addNodeDisplayListeners(newNode);
	    nodeLayer.getChildren().add(newNode);
	    render();
	}

	private void addNodeDisplayListeners(NodeDisplay nd){
		nd.centerXProperty().addListener(e -> {
	    	nd.setTranslateX(nd.getCenterX());
	    });
	    nd.centerYProperty().addListener(e -> {
	    	nd.setTranslateY(nd.getCenterY());
	    });
	    nd.addEventFilter(SelectEvent.NODE, event -> {
	    	
	    	if(event.getEventType() == SelectEvent.PIVOT_NODE_SELECTED) {
	    		if(currentPivot != null) currentPivot.deselectNode();
	    		nd.selectPivot();
	    		currentPivot = nd;
	    	}
	    	else if (event.getEventType() == SelectEvent.NODE_SELECTED){
	    		nd.selectNode();
		    	if(!isMultiSelectNodes && isEditMode){
		    		NodeDisplay[] ndList = new NodeDisplay[nodeQueue.size()];
		    		nodeQueue.toArray(ndList);
		    		for(NodeDisplay n : ndList) {
		    			n.fireEvent(new SelectEvent(SelectEvent.NODE_DESELECTED));
		    		}
		    		nodeQueue.clear();
		    	}
		    	
			    nodeQueue.add(nd);
			    if(nodeQueue.size() == 2 && !isEditMode){
			    	drawPath();
			    	nodeQueue.clear();
			    	nd.fireEvent(new SelectEvent(SelectEvent.NODE_DESELECTED));
			    }
	    	}
		   
		    ControllerSingleton.getInstance().displayInDev(nd);
	    });
	    
	    nd.addEventFilter(SelectEvent.NODE_DESELECTED, event -> {
	        nodeQueue.remove(nd);
	    });

	    nd.addEventFilter(EditorEvent.DELETE_NODE, event -> {
        	if(isEditMode){
	        	Graph g = display.getGraph();
	        	Id id = nd.getNode();
	        	g.deleteNode(id);
	        	nodeLayer.getChildren().remove(nd);
	        	render();
        	}
        });
	}
	
	/**
	 * Add EdgeDisplays from selected NodeQueue
	 * Use to add a non-existing EdgeDisplay and Edge to the display
	 */
	public boolean addEdgeDisplayFromQueue(){
		SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
		boolean addFromPivot = false;
		int pollSize = 1;
		if(currentPivot != null) { addFromPivot = true; pollSize = 0; }
		else if(nodeQueue.isEmpty()) return false;
        while(nodeQueue.size() > pollSize){
        	NodeDisplay aND = nodeQueue.poll();
    		NodeDisplay bND = nodeQueue.peek();
        	if(addFromPivot) bND = currentPivot;
            Graph g = display.getGraph();
            Node a = g.returnNodeById(aND.getNode());
            Node b = g.returnNodeById(bND.getNode());

            Id newEdge = g.addEdgeRint(a.getId(), b.getId());
            Coordinate start = view.transform(a.getCoordinate());
	        Coordinate end = view.transform(b.getCoordinate());
	        
            EdgeDisplay e = new EdgeDisplay(display, newEdge, start, end);
            e.setStartX(start.getX());
    		e.setStartY(start.getY());
    		e.setEndX(end.getX());
    		e.setEndY(end.getY());
    		
            setEdgeDisplayListeners(e);
            edgeLayer.getChildren().add(e);
            aND.fireEvent(selectNodeEvent);
        }
        if(!addFromPivot) { nodeQueue.remove().fireEvent(selectNodeEvent); }
        else currentPivot.deselectNode();
        currentPivot = null;
	    render();
	    return true;
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
	    	if(edge == null) continue;
	    	Id aID = edge.getNodeA();
		    Id bID = edge.getNodeB();
		    Node a = graph.returnNodeById(aID);
		    Node b = graph.returnNodeById(bID);
		    if(a == null || b == null){
		    	graph.deleteEdge(edge.getId());
		    	continue;
		    }
	        Coordinate start = a.getCoordinate();
	        Coordinate end = b.getCoordinate();
		    EdgeDisplay e = new EdgeDisplay(display, aID, bID, start, end);
		    e.setStartX(start.getX());
    		e.setStartY(start.getY());
    		e.setEndX(end.getX());
    		e.setEndY(end.getY());
		    edgeLayer.getChildren().add(e);
		    setEdgeDisplayListeners(e);
		    render();
	    }
	   
	}
	
	private void setEdgeDisplayListeners(EdgeDisplay e){
		e.setStrokeWidth(5.0f);
		e.setStroke(Color.AQUA);
		e.addEventFilter(SelectEvent.EDGE_SELECTED, ev -> {
        	e.selectEdge();
        	edgeQueue.clear();
        	edgeQueue.add(e);
        	//TODO display edge data on select
        	ControllerSingleton.getInstance().displayInDev(e);
        });

        e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
        	//do sth;
        	edgeQueue.remove(e);
        });
        
        e.addEventFilter(EditorEvent.DELETE_EDGE, ev -> {
        	edgeLayer.getChildren().remove(e);
        	display.getGraph().deleteEdge(e.getEdge());
        	render();
        });
	}
    /**
     * Draws a path from the last two selected nodes
     */
	public void drawPath(){
		pathPane.getChildren().clear();
        NodeDisplay startNode = nodeQueue.poll();
        NodeDisplay endNode = nodeQueue.poll();
        if(startNode != null && endNode != null){
            //int idx = 0;
            //Vector<Node> nodes = display.getGraph().getNodes();

            p = new Path(startNode.getNode(), endNode.getNode());
            Graph g = display.getGraph();
            p.runAStar(g); //Change this later??
            currentRoute = p.getRoute();
            
            render();
            SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
            startNode.fireEvent(selectNodeEvent);
            endNode.fireEvent(selectNodeEvent);
            showDirections();
        }
	}

    @SuppressWarnings("unchecked")
	public void showDirections(){
        ObservableList<String> pathList = FXCollections.observableList(p.getTextual());
        ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().getdirectionsListView().setItems(pathList);
    }

}
