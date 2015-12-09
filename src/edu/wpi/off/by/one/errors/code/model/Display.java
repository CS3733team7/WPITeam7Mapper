package edu.wpi.off.by.one.errors.code.model;

import java.util.ArrayList;
import java.util.Vector;

import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Display extends Pane{
	
	ArrayList<Map> Maps;
	Graph currentGraph;

	/**
	 * Display Constructor
	 * @param currentMap
	 * @param currentGraph
	 */
	public Display(Map currentMap, Graph currentGraph){
		this.Maps = Maps == null ? new ArrayList<Map>() : Maps;
		if(Maps.isEmpty()) Maps.add(new Map());
		this.currentGraph = currentGraph == null ? new Graph() : currentGraph;
	}
	
	/**
	 * Default constructor
	 * 
	 */
	public Display(){
		this.Maps = new ArrayList<Map>();
		Maps.add(new Map());
		this.currentGraph = new Graph();
	}
	
	public void addMap(Map m){ Maps.add(m);}
	public void setGraph(Graph g) { this.currentGraph = g; }
	public ArrayList<Map> getMaps() { return Maps; }
	public Graph getGraph() { return currentGraph; }
	
	public Map getNearestMap(Coordinate coord, int cz){
		float distsq = Float.MAX_VALUE;
		float cx = coord.getX();
		float cy = coord.getY();
		Map nearest = null;
		for(Map m : Maps){
			if(m == null) continue;
			float mx = m.getCenter().getX() - cx;
			float my = m.getCenter().getY() - cy;
			float mz = m.getCenter().getZ();
			float mydistsq = mx * mx + my * my;
			if(mydistsq < distsq && mz == cz){
				distsq = mydistsq;
				nearest = m;
			}
		}
		return nearest;
	}
	
	/**
	 * Draws a graphical path between two nodes on the map
	 * @param start First node
	 * @param end Second node
	 */
	public void drawPath(Id start, Id end) {
		int idx = 0;
		Vector<Node> nodes = currentGraph.getNodes();
		Path p = new Path(start, end);
        if(ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode){
        	p.runAccessibleAStar(currentGraph);
        }
        else p.runAStar(currentGraph); //Change this later??
		ArrayList<Id> idList = p.getRoute();
		while(idx < idList.size()){
			Node a = nodes.get(idx);
			Node b = nodes.get(idx++);
			Coordinate aLoc = a.getCoordinate();
			Coordinate bLoc = b.getCoordinate();
			Line l = new Line(aLoc.getX(), aLoc.getY(), 
					bLoc.getX(), bLoc.getY());
			//move(l, (aLoc.getX() + bLoc.getX())/2, (aLoc.getY() + bLoc.getY())/2);

			//mapPane.getChildren().add(l);
		}
		//TODO: Add code to actually draw the line on the map
	}
	/*
	private ImageView GetMapView() {
		Maps.getImgUrl();
		Image map = new Image("campusmap.png");
		ImageView mapIV = new ImageView();
		mapIV.setImage(map);
		mapIV.setPreserveRatio(true);
		mapIV.setSmooth(true);
		mapIV.setCache(true);
		return mapIV;
	}
	*/
}
