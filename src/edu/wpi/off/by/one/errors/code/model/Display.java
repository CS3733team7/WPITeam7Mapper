package edu.wpi.off.by.one.errors.code.model;

import java.util.ArrayList;
import java.util.Vector;

import edu.wpi.off.by.one.errors.code.application.NodeDisplay;
import edu.wpi.off.by.one.errors.code.application.event.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Display extends Pane{
	
	Map currentMap;
	Graph currentGraph;

	/**
	 * Display Constructor
	 * @param currentMap
	 * @param currentGraph
	 */
	public Display(Map currentMap, Graph currentGraph){
		this.currentMap = currentMap == null ? new Map() : currentMap;
		this.currentGraph = currentGraph == null ? new Graph() : currentGraph;
	}
	
	/**
	 * Default constructor
	 * 
	 */
	public Display(){
		this.currentMap = new Map();
		this.currentGraph = new Graph();
	}
	
	public void setMap(Map m){ this.currentMap = m; }
	public void setGraph(Graph g) { this.currentGraph = g; }
	public Map getMap() { return currentMap; }
	public Graph getGraph() { return currentGraph; }
	
	/**
	 * Draws a graphical path between two nodes on the map
	 * @param a First node
	 * @param b Second node
	 */
	public void drawPath(Id start, Id end) {
		int idx = 0;
		Vector<Node> nodes = currentGraph.getNodes();
		Path p = new Path(start, end);
		p.runAStar(currentGraph); //Change this later??
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
	
	private ImageView GetMapView() {
		currentMap.getImgUrl();
		Image map = new Image("campusmap.png");
		ImageView mapIV = new ImageView();
		mapIV.setImage(map);
		mapIV.setPreserveRatio(true);
		mapIV.setSmooth(true);
		mapIV.setCache(true);
		return mapIV;
	}
	
}
