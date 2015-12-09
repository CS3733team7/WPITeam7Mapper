package edu.wpi.off.by.one.errors.code.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import edu.wpi.off.by.one.errors.code.application.EdgeDisplay;
import edu.wpi.off.by.one.errors.code.application.MarkerDisplay;
import edu.wpi.off.by.one.errors.code.application.MarkerDisplay.Marker;
import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.EditorEvent;
import edu.wpi.off.by.one.errors.code.application.event.SelectEvent;
import edu.wpi.off.by.one.errors.code.model.*;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

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
	@FXML Pane pathPane;
	@FXML Pane markerPane;
	
	public Coordinate translate = new Coordinate(0.0f, 0.0f, 1.0f);;
	Coordinate release = new Coordinate(0, 0, 0);
	public float rot = 0.0f;
	public float zoom = 2.0f;
	public Matrix view;
	Matrix invview;
	Matrix lastview;
	
	@FXML public Canvas canvas;
	public int currentLevel = 1;
	private Path p;
	private MarkerDisplay startMarker = null;
	private MarkerDisplay endMarker = null;
	//Where all the images and txt files should be
	String resourceDir = "/edu/wpi/off/by/one/errors/code/resources/";
	private String filePath = "src" + resourceDir + "maps/txtfiles/fullCampusMap.txt";
	
	Display display;												//Current display
	
	Queue<NodeDisplay> nodeQueue = new LinkedList<NodeDisplay>();	//Selected node queue
    Queue<EdgeDisplay> edgeQueue = new LinkedList<EdgeDisplay>();
    ArrayList<Map> selectedMaps = new ArrayList<Map>();
    ArrayList<Id> currentRoute = new ArrayList<Id>();
    public NodeDisplay currentPivot = null;
    boolean isMapEditor = false;
    public boolean isNodeEditor = false;
    public boolean isEdgeEditor = false;
    public boolean isEditMode = false;
    public boolean isAddMode = false;		//Is editor currently adding nodes?
    public boolean isDeleteMode = false;	//Is editor currently deleting nodes?
    public boolean isMultiSelectNodes = false;
    public boolean isPirateMode = false;
    public boolean isAccessibleMode = false;
    public boolean isEyedrop = false;
    boolean isZooming = false;
	Image pirateX = null;
    
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
        //canvas.widthProperty().bind(this.widthProperty());
        //canvas.heightProperty().bind(this.heightProperty());
        initialize();

        //ControllerSingleton.getInstance().getMenuPane().searchMenuPane.spinnyZoom(1);
    }
    
    public MapRootPane getMapRootPane() { return this; }
    public ArrayList<Map> getSelectedMaps() { return this.selectedMaps; }
    public void setSelectedMaps(ArrayList<Map> maps) { this.selectedMaps = maps; }
    public Queue<NodeDisplay> getSelectedNodes() { return this.nodeQueue; }
    public Queue<EdgeDisplay> getSelectedEdges() { return this.edgeQueue; }
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
		// Put all these sets into fxml
        pathPane.setMouseTransparent(true);
        markerPane.setMouseTransparent(false);
        markerPane.setPickOnBounds(true);
        edgeLayer.setPickOnBounds(false);
        nodeLayer.setPickOnBounds(false);
		nodeLayer.setAlignment(Pos.TOP_LEFT);
		//Set map image
		Coordinate lastdragged = new Coordinate(0);
		Coordinate mydragged = new Coordinate(0);
		updateDisplay(display.getGraph());
        
        //Setup event listeners for map
        setListeners();
        ControllerSingleton.getInstance().getMenuPane().searchMenuPane.updateMapList(display.getMaps());

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
			}
			else if (e.isAltDown()){
				rot += (0.4*(sin.getX() - mydragged.getX()));
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


	double renderavg1 = 0.0;
	double renderavg2 = 0.0;
	double renderavg3 = 0.0;
	double renderavg4 = 0.0;
	double renderavg5 = 0.0;


	int renderavgcount = 0;
	/**
	 * Handles all the zoom/rotation/translation of objects on the map
	 * and draws them onto map
	 */
	public void render(){
		long time1 = System.nanoTime();
		if(zoom < 0.4f) zoom = Math.abs(zoom);
		if(zoom < 0.4f) zoom = 0.4f;
		else if(zoom > 11.4f) zoom = 11.4f;
		view = new Matrix().translate(new Coordinate((float)canvas.getWidth()/2.0f, (float)canvas.getHeight()/2.0f)).rotate(rot, 0.0f, 0.0f, 1.0f).scale(zoom).translate(new Coordinate(translate.getX(), translate.getY(), translate.getZ()));
		invview = new Matrix(new Coordinate(-1.0f * translate.getX(), -1.0f *translate.getY(), -1.0f * translate.getZ())).scale(1.0/zoom).rotate(-rot, 0.0, 0.0, 1.0).translate(new Coordinate((float)canvas.getWidth()/-2.0f, (float)canvas.getHeight()/-2.0f));
		long time2 = System.nanoTime();
		//grab graphics context
		GraphicsContext mygc = canvas.getGraphicsContext2D();
		mygc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		mygc.setFill(Color.rgb(173, 221, 116));
		mygc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
		long time3 = System.nanoTime();
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
			Rotate r = new Rotate(m.getRotation() + rot, 0, 0);
			mygc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx() + c.getX(), r.getTy() + c.getY());
			mygc.scale(zoom * m.getScale(), zoom * m.getScale());
			
			if(selectedMaps.contains(m) && isEditMode){
				mygc.setGlobalAlpha(1);
				DropShadow ds = new DropShadow();
				ds.setColor(Color.RED);
				ds.setRadius(50 / m.getScale());
				ds.setSpread(0.5);
				mygc.setEffect(ds);
			}
			mygc.drawImage(m.getImage(), 0, 0);
			mygc.restore();
		}
		long time4 = System.nanoTime();
		/*
		if(startMarker != null) {
			Coordinate c = view.transform(new Coordinate((float)startMarker.x, (float)startMarker.y, (float)startMarker.z));
			startMarker.setTranslateX(c.getX() - (startMarker.getImage().getWidth()/2));
			startMarker.setTranslateY(c.getY() - startMarker.getImage().getHeight());
		}
		*/



		if(isEditMode){
			markerPane.setMouseTransparent(false);
			markerPane.setVisible(false);
			edgeLayer.setVisible(true);
			nodeLayer.setVisible(true);
			nodeLayer.setMouseTransparent(false);
			mygc.save();
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


			Set<EdgeDisplay> toRemove = new HashSet<>();
			for(javafx.scene.Node ep: edgeLayer.getChildren()){
				EdgeDisplay ed = (EdgeDisplay)ep;
				if(ed == null) continue;
				Edge e = display.getGraph().returnEdgeById(ed.getEdge());
				if(e == null){ toRemove.add((EdgeDisplay) ep); continue; }
				Node A = display.getGraph().returnNodeById(e.getNodeA());
				Node B = display.getGraph().returnNodeById(e.getNodeB());
				if(A == null || B == null){
					display.getGraph().deleteEdge(e.getId());
					toRemove.add((EdgeDisplay) ep); 
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
			mygc.restore();
		} else { 
			markerPane.setVisible(true);
			markerPane.setMouseTransparent(false);
			edgeLayer.setVisible(false); 
			nodeLayer.setVisible(false); 
		
			Node last = null;

			if(currentRoute != null){
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
	                if(isPirateMode) {
	                    mygc.setFill(Color.RED);
	                    mygc.setStroke(Color.RED);
	                    mygc.setLineDashes(10);
	                } else {
	                    mygc.setFill(Color.BLUE);
	                    mygc.setStroke(Color.BLUE);
	                    mygc.setLineDashes(null);
	                }
					mygc.strokeLine(ac.getX(), ac.getY(), bc.getX(), bc.getY());
					last = A;
					mygc.restore();
				}
			}

			for(javafx.scene.Node mp: markerPane.getChildren()){
				MarkerDisplay md = (MarkerDisplay)mp;
				if(mp == null) continue;
				if(translate.getZ() > md.z + 0.1 || translate.getZ() < md.z - 0.1){
					mp.setVisible(false);
					mp.setMouseTransparent(true);
					continue;
				} else {
					mp.setVisible(true);
					mp.setMouseTransparent(false);
					Coordinate c = view.transform(new Coordinate((float)md.x, (float)md.y, (float)md.z));
					mp.setTranslateX(c.getX() - (md.getImage().getWidth()/2));
					mp.setTranslateY(c.getY() - md.getImage().getHeight());
				}
			}


			//render big red X
			if(currentRoute != null){
				Node mest = null;
				int i = currentRoute.size()-1;
				if(i >= 0)for(mest = display.getGraph().returnNodeById(currentRoute.get(i)); mest == null && i >= 0; i--);
				if(mest != null) {
					Coordinate c = view.transform(mest.getCoordinate());
					mygc.save();
					//Rotate r = new Rotate(rot, 0, 0);
					//mygc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx() + c.getX(), r.getTy() + c.getY());
					if (pirateX == null) pirateX = new Image(MarkerDisplay.pirate_endImg);
					mygc.drawImage(pirateX, c.getX() - pirateX.getWidth() / 2.0, c.getY() - pirateX.getHeight() / 2.0);
					mygc.restore();
				}
			}
		}
		long time10 = System.nanoTime();
		renderavg1 += (double)(time10 - time1);
		renderavg2 += (double)(time2 - time1);
		renderavg3 += (double)(time3 - time2);
		renderavg4 += (double)(time4 - time3);
		renderavg5 += (double)(time10 - time4);

		renderavgcount++;
		if(renderavgcount >= 100){
			System.out.println("Render averages in ms: ");
			System.out.println("Avg1 " + (renderavg1 * 0.001) / (double) renderavgcount);
			System.out.println("Avg2 " + (renderavg2 * 0.001) / (double) renderavgcount);
			System.out.println("Avg3 " + (renderavg3 * 0.001) / (double) renderavgcount);
			System.out.println("Avg4 " + (renderavg4 * 0.001) / (double) renderavgcount);
			System.out.println("Avg5 " + (renderavg5 * 0.001) / (double) renderavgcount);

			renderavg1 = 0.0;
			renderavg2 = 0.0;
			renderavg3 = 0.0;
			renderavg4 = 0.0;
			renderavg5 = 0.0;

			renderavgcount = 0;
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

	public void placeMarker(Node n){
		markerPane.getChildren().clear();
		currentRoute = null;
		Coordinate nc = n.getCoordinate();
		markerPane.getChildren().add(new MarkerDisplay(nc.getX(), nc.getY(), nc.getZ(), Marker.SELECT));
		render();
	}
	
	public void placeStartMarker(Node n){
		markerPane.getChildren().clear();
		currentRoute = null;
		Coordinate nc = n.getCoordinate();
		startMarker = new MarkerDisplay(nc.getX(), nc.getY(), nc.getZ(), Marker.START);
		startMarker.setNodePoint(n.getId());
		markerPane.getChildren().add(startMarker);
		render();
	}
	
	public void placeEndMarker(Node n){
//		markerPane.getChildren().clear();
//		currentRoute = null;
//		Coordinate nc = n.getCoordinate();
//		startMarker = new MarkerDisplay(nc.getX(), nc.getY(), nc.getZ(), Marker.START);
//		startMarker.setNodePoint(n.getId());
//		markerPane.getChildren().add(startMarker);
		drawPath(startMarker.getNodePoint(), n.getId());
		render();
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
    	Coordinate lastdragged = new Coordinate(0);
		Coordinate mydragged = new Coordinate(0);
    	canvas.setOnMousePressed(e -> {
    		Map nearestMap = null;
    		if(!selectedMaps.isEmpty() && ControllerSingleton.getInstance().getMapDevToolPane().isVisible()) nearestMap = selectedMaps.get(0);
    		else selectedMaps.clear();
    		Coordinate click = invview.transform(new Coordinate((float)e.getX(), (float)e.getY()));
			//nearestMap = display.getNearestMap(click, currentLevel);
			if(e.getButton() == MouseButton.PRIMARY && isEditMode && ControllerSingleton.getInstance().getMapDevToolPane().isVisible()){
				//select map
				lastview = invview;
				if(!e.isShiftDown()) selectedMaps.clear();
				if(nearestMap == null) return;
				if(isEyedrop){
					System.out.println("EYEDROP");
					int eyedroppedColor = nearestMap.getColor(click);
					ControllerSingleton.getInstance().getMapDevToolPane().setEyedroppedColor(eyedroppedColor);
				} else {
					
	    			if(selectedMaps.contains(nearestMap)) selectedMaps.remove(nearestMap);
	    			else if(nearestMap != null) selectedMaps.add(nearestMap);
	    			ControllerSingleton.getInstance().getMenuPane().getDevToolsMenuPane().getMapDevToolPane().setMap(nearestMap);
	    			
		   			 Coordinate in = new Coordinate((float)e.getX(), (float)e.getY());
		   			 Coordinate sin = lastview.transform(in);
		   			 mydragged.setAll(in.getX(), in.getY(), 0);
		   			 lastdragged.setAll(sin.getX(), sin.getY(), 0);
				}
    			
			}
    		
	     });
		canvas.setOnMouseDragged(e -> {
			if(e.getButton() == MouseButton.PRIMARY && isEditMode && ControllerSingleton.getInstance().getMapDevToolPane().isVisible()){
				Coordinate sin = new Coordinate((float)e.getX(), (float)e.getY());
				Coordinate in = lastview.transform(sin);
				Coordinate delta = new Coordinate(0);
				float deltaZoom = 0;
				float deltaRot = 0;
				if(e.isControlDown()){
					deltaZoom += (0.001*(sin.getY() - mydragged.getY()));
					render();
					lastview = invview;
				}
				else if (e.isAltDown()){
					deltaRot += (0.4*(sin.getX() - mydragged.getX()));
					render();
					lastview = invview;
				} else {
					delta = new Coordinate(in.getX() - lastdragged.getX(), in.getY() - lastdragged.getY());
					
				}
				for(Map m : selectedMaps){
					Coordinate c = m.getCenter();
					m.setRotation(m.getRotation() + deltaRot);
					m.setScale(m.getScale() + deltaZoom);
					m.getCenter().setAll((float) c.getX() + delta.getX(), (float)c.getY() + delta.getY(), c.getZ());
					render();
				}
				lastdragged.setAll(in.getX(), in.getY(), 0);
				mydragged.setAll(sin.getX(), sin.getY(), 0);
			}

		});
		
		canvas.setOnMouseReleased(e -> {
			if(e.getButton() == MouseButton.PRIMARY && isEditMode){
				release.setAll((float)e.getX(), (float)e.getY(), 0);
			}
	     });
    	canvas.setOnMouseClicked(e -> {
    		//If user did not click-drag on map
    		if(e.isStillSincePress()){
       			if (isEditMode && e.getButton() == MouseButton.PRIMARY && !ControllerSingleton.getInstance().getMapDevToolPane().isVisible()) {
    				addNodeDisplay(e.getX(), e.getY());
	            }
       			/* Single click event always fires so it does that and then the zoom
	    		else if(e.getClickCount() == 2){
	    			e.consume();
	    			//TODO if on building -> zoomyspin onto building
	    			//else, standard zoom in
	    			isZooming = true;
	    			nodeQueue.clear();
	    			ControllerSingleton.getInstance().getMainPane().zitl.setCycleCount(10);
	    			ControllerSingleton.getInstance().getMainPane().zitl.setOnFinished(ev -> {
	    				currentRoute = null;
	    				isZooming = false;
	    			});
	    			ControllerSingleton.getInstance().getMainPane().zitl.play();
	    			ControllerSingleton.getInstance().getMainPane().zitl.setCycleCount(Timeline.INDEFINITE);
	    			
	    			render();
	    		}
	    		*/
    		}
    	});
    	
    	markerPane.setOnMouseClicked(e -> {
    		if (e.getClickCount() == 2) e.consume();
    		else if (!isEditMode && e.getButton() == MouseButton.PRIMARY) {
    			//Select nearest node on map
    			Coordinate click = invview.transform(new Coordinate((float)e.getX(), (float)e.getY()));
    			Id nearestNodeId = display.getGraph().GetNearestNode(click, currentLevel);
    			Node nearestNode = display.getGraph().returnNodeById(nearestNodeId);
    			if (endMarker != null && startMarker != null){
    				startMarker = null;
    				endMarker = null;
    				currentRoute.clear();
    				markerPane.getChildren().clear();
    			}
    			if(startMarker != null && endMarker == null) {
    				endMarker = new MarkerDisplay(nearestNode.getCoordinate().getX(), nearestNode.getCoordinate().getY(), currentLevel, Marker.END);
    				endMarker.setNodePoint(nearestNodeId);
    				drawPath(startMarker.getNodePoint(), endMarker.getNodePoint());
    				//markerPane.getChildren().add(endMarker);
    			}
    			if(startMarker == null && nodeQueue.size() == 0) {
    				//snap to nearest available node
    				markerPane.getChildren().clear();
    				startMarker = new MarkerDisplay(nearestNode.getCoordinate().getX(), nearestNode.getCoordinate().getY(), currentLevel, Marker.START);
    				startMarker.setNodePoint(nearestNodeId);
    				markerPane.getChildren().add(startMarker);
    				
    			} else { nodeQueue.clear(); }
    			/*
    			List<javafx.scene.Node> nearestList = nodeLayer.getChildren().stream()
    					.filter((Predicate<? super javafx.scene.Node>) nd -> ((NodeDisplay) nd).getNode() == nearestNodeId)
    					.collect(Collectors.toList());
    					*/
    			/*
    			NodeDisplay nearest = (NodeDisplay) nearestList.get(0);
    			if(nodeQueue.size() > 0) {
    				if(nearest == nodeQueue.peek()) {
    					nodeQueue.clear();
    					return;
    				}
    			}
    			nearest.fireEvent(new SelectEvent(SelectEvent.NODE_SELECTED));
    			*/
    			render();
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
		Coordinate lastdragged = new Coordinate(0);
		Coordinate mydragged = new Coordinate(0);
		
		nd.centerXProperty().addListener(e -> {
	    	nd.setTranslateX(nd.getCenterX());
	    });
	    nd.centerYProperty().addListener(e -> {
	    	nd.setTranslateY(nd.getCenterY());
	    });
	    
	    nd.setOnMousePressed(e -> {
	    	if(e.getButton() == MouseButton.PRIMARY && isEditMode){
	    		lastview = invview;
	   			 Coordinate in = new Coordinate((float)e.getX(), (float)e.getY());
	   			 Coordinate sin = lastview.transform(in);
	   			 mydragged.setAll(in.getX(), in.getY(), 0);
	   			 lastdragged.setAll(sin.getX(), sin.getY(), 0);
    		}
	    });
	    
	    nd.setOnMouseDragged(e -> {
	    	if(e.getButton() == MouseButton.PRIMARY && isEditMode){
				Coordinate sin = new Coordinate((float)e.getX(), (float)e.getY());
				Coordinate in = invview.transform(sin);
				Graph g = display.getGraph();
				
				NodeDisplay[] list = new NodeDisplay[nodeQueue.size()];
				nodeQueue.toArray(list);
				for(NodeDisplay n : list){
					if(n == null) continue;
					if(n.getNode() == null) continue;
					Node node = g.returnNodeById(nd.getNode());
					if(node == null) continue;
					Coordinate c = node.getCoordinate();
					node.setCoordinate(new Coordinate(in.getX(), in.getY(), c.getZ()));
				}
				render();
				lastdragged.setAll(in.getX(), in.getY(), 0);
				mydragged.setAll(sin.getX(), sin.getY(), 0);
			}
	    });
	    
	    nd.setOnMouseReleased(e -> {
			if(e.getButton() == MouseButton.PRIMARY && isEditMode){
				release.setAll((float)e.getX(), (float)e.getY(), 0);
			}
			render();
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
            if(newEdge == null) {
            	continue;
            }
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
    		setEdgeDisplayListeners(e);
		    edgeLayer.getChildren().add(e);
		    
	    }
	   render();
	}
	
	private void setEdgeDisplayListeners(EdgeDisplay e){
		e.setStrokeWidth(5.0f);
		e.setStroke(Color.AQUA);
		e.addEventFilter(SelectEvent.EDGE_SELECTED, ev -> {
        	e.selectEdge();
        	EdgeDisplay[] edList = new EdgeDisplay[edgeQueue.size()];
        	edgeQueue.toArray(edList);
        	for(EdgeDisplay ed : edList) {
        		ed.fireEvent(new SelectEvent(SelectEvent.EDGE_DESELECTED));
        	}
        	edgeQueue.clear();
        	edgeQueue.add(e);
        	//TODO display edge data on select
        	ControllerSingleton.getInstance().displayInDev(e);
        });

        e.addEventFilter(SelectEvent.EDGE_DESELECTED, ev -> {
        	edgeQueue.remove(e);
        });
        
        e.addEventFilter(EditorEvent.DELETE_EDGE, ev -> {
        	display.getGraph().deleteEdge(e.getEdge());
        	edgeLayer.getChildren().remove(e);
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
        //if(startNode != null && endNode != null && isZooming){
            
    	//int idx = 0;
        //Vector<Node> nodes = display.getGraph().getNodes();
    	
        p = new Path(startNode.getNode(), endNode.getNode());
        Graph g = display.getGraph();
        if(ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode){
        	p.runAccessibleAStar(g);
        }
        else p.runAStar(g); //Change this later??
        currentRoute = p.getRoute();
        render();
        SelectEvent selectNodeEvent = new SelectEvent(SelectEvent.NODE_DESELECTED);
        startNode.fireEvent(selectNodeEvent);
        endNode.fireEvent(selectNodeEvent);
        showDirections();
	}

	public void drawPath(Id nodeAId, Id nodeBId){
		 p = new Path(nodeAId, nodeBId);
	        Graph g = display.getGraph();
	        if(ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode){
	        	p.runAccessibleAStar(g);
	        }
	        else p.runAStar(g); //Change this later??
	        currentRoute = p.getRoute();
	        render();
	        showDirections();
	}
	
    @SuppressWarnings("unchecked")
	public void showDirections(){
    	if(p != null && p.getTextual() != null){
    		ControllerSingleton.getInstance().getMenuPane().showDirections();
    		ObservableList<String> pathList = FXCollections.observableList(p.getTextual());
            ControllerSingleton.getInstance().getMenuPane().getDirectionsMenuPane().getdirectionsListView().setItems(pathList);
    	}
    }
	public Path getPath(){
		return this.p;
	}
}
