package edu.wpi.off.by.one.errors.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class Path {
	private int startNode;
	private int endNode;
	private ArrayList<Integer> route;
	private Vector<Node> nodes;
	private Vector<Edge> edges;
	
	public Path(int startNodeIn, int endNodeIn){
		startNode = startNodeIn;
		endNode = endNodeIn;
		route = new ArrayList<Integer>();
	}
	
	/**
	 * this method is the bulk of the A* algorithm, it iterates through a list of nodes and chooses the best path from them
	 * @param nodesIn
	 * @param edgesIn
	 */
	public void runAStar(Vector<Node> nodesIn, Vector<Edge> edgesIn){
		System.out.println("started a*");
		ArrayList<Integer> visited = new ArrayList<Integer>();	//These nodes we have already seen
		ArrayList<Integer> open = new ArrayList<Integer>();		//These are the next nodes we will visit

		open.add(startNode);
		this.nodes = nodesIn;	//the list of Node objects to search
		this.edges = edgesIn;	//the list of the associated edges for the Nodes

		int current; 		//the ID for the node we are currently examining
		HashMap<Integer, Integer> cameFrom = new HashMap<Integer, Integer>();		//The map of current optimum path to a node
		HashMap<Integer, Float> gScore = new HashMap<Integer, Float>();	//The map of the nodeID to its path finding score, the lower the better
		for (Node elem : nodesIn){	//sets each node that we could examine, place its ID and the maximum value into the #map
			gScore.put(elem.getId(), Float.MAX_VALUE);
		}
		gScore.put(startNode, (float) 0.0);	//start the algorithm at the first node
		HashMap fScore = new HashMap<Integer, Float>();	//create a new #map for the f score of the node
		for (Node elem : nodesIn){	//for each node that we could examine, place its ID and the maximum value into the map
			fScore.put(elem.getId(), Float.MAX_VALUE);
		}
		fScore.put(startNode, calcHeuristic(startNode,endNode));	//initialize the fscore with the heuristic of length
		if (open.isEmpty()){System.out.println("open is empty you fool");}
		while(!open.isEmpty()){	//while we still have nodes to visit
			current = findLowestFInOpen(open, fScore);	//find the closest node to the end in the open list 
			int toRemove = open.indexOf(current);
			open.remove(toRemove);
			System.out.println("Start printing open" + open.toString() + "finished printing open");
			if (current == endNode){	//we made it!
				System.out.println("ever found a path");
				route = reconstructPath(cameFrom, current);	//run the helper that goes through and puts the path in order
			}
			//int toRemove = open.indexOf(current);
			//open.remove(toRemove);	//take the current node out of nodes to visit
			visited.add(current);	//then add it to the list of already visited nodes
			/*System.out.println("start of edgelist");
			System.out.println(nodes.get(current).getEdgelist().toString());
			System.out.println("end of edgelist");*/
			System.out.println("nodes.get(current) "+nodes.get(current)+" end");
			System.out.println("EdgeListBeforeFor" + nodes.get(current).getEdgelist()+"EdgeListBeforeForDone");
			for(int elem : nodes.get(current).getEdgelist()){	//find all the edges attached to the node
				System.out.println("in the for loop");
				Edge neighborEdge = edges.get(elem);	//pull one edge from the list at a time
				int neighborId;	//the ID of the node at the other end of the edge
				if (neighborEdge.getNodeA() == current){	//if we try and get the ID for the node and it's the same ID
					neighborId = neighborEdge.getNodeB();	//then it must be the other node in the edge
				}
				else {
					neighborId = neighborEdge.getNodeA();	//otherwise this one must be the neighbor
				}	
				float tentativeGScore = gScore.get(current)+edges.get(elem).getLength();	//calculate the distance needed to get to the current point	
				if(visited.contains(neighborId)){				//if we have already been to this neighbor
					if(gScore.get(neighborId)>tentativeGScore){	//and this route is better than the existing one
						gScore.put(neighborId, tentativeGScore);//then make this the current best route to this point
					}
					else continue;	//otherwise we need to do the while loop again
				}
				if(!open.contains(neighborId)){	//if we haven't been to the neighbor yet
					System.out.println("adding to open");
					System.out.println("Start printing open" + open.toString() + "finished printing open");
					open.add(neighborId);		//add the neighbor to the list of places to go
				}
				else if(tentativeGScore>gScore.get(neighborId)){	//if this path to this node isn't better than the existing one
					continue;										//loop again
				}
				cameFrom.put(neighborId, current); //if we made it to here it means we found a new best path to this node
				gScore.put(neighborId, tentativeGScore);   //now we just make it say that
				fScore.put(neighborId, gScore.get(neighborId)+calcHeuristic(neighborId  , endNode));
			}
		}
		
		
	}
	/**
	 * this calculates the heuristic (length) between the two nodes
	 * @param current current node
	 * @param destination destination node
	 * @return the distance between the nodes
	 */
	private float calcHeuristic(int current, int destination){
		Coordinate coordA = null; //initialize the coordinates
		Coordinate coordB = null;
		for(Node elem: nodes){	//for each node in the list of nodes
			if (elem.getId() == current){	//we want to get the IDs of the passed nodes and then get their coordinates
				coordA = elem.getCoordinate();
			}
			if (elem.getId() == destination){
				coordB = elem.getCoordinate();
			}
		}

		float xDist = coordA.getX()-coordB.getX();
		float yDist = coordA.getY()-coordB.getY();
		float zDist = coordA.getZ()-coordB.getZ();
		return (float) Math.sqrt(xDist*xDist+yDist*yDist+zDist*zDist); //return the pythagorean length
	}
	
	/**
	 * find the lowest F score in the list of nodes we haven't visited
	 * @param openList
	 * @param fScores
	 * @return	the index of the node with the lowest f score
	 */
	private int findLowestFInOpen(ArrayList<Integer> openList, HashMap<Integer, Float> fScores){
		float lowest = Float.MAX_VALUE;
		int iDLowest = -1;
		for(int elem : openList){	//for each element in the list of unexplored nodes see which has the lowest score
			if( fScores.get(elem)<lowest){
				lowest = fScores.get(elem);
				iDLowest = elem;
			}
		}
		return iDLowest;
	}	
	
	/**
	 * Helper for A* that backtracks and finds the path we took through the nodes
	 * @param parentList
	 * @param currentNode
	 * @return the list of nodes that we visited in order from first to last
	 */
	private ArrayList<Integer> reconstructPath(HashMap<Integer, Integer> parentList, int currentNode){
		int currNode = currentNode;
		ArrayList<Integer> totalPath = new ArrayList<Integer>();	//initialize a variable that will hold the path as we back through it
		System.out.println(currNode);
		totalPath.add(currNode); //start with the node we ended at
		while(currNode != startNode){	//while we haven't gotten back to the start
			System.out.println(currNode);

			System.out.println(Math.round(parentList.get(currNode)));
			currNode= Math.round(parentList.get(currNode));
			
			//currNode = Integer.parseInt(jank); //keep adding the parent node of the current node to the path
			totalPath.add(currNode);
		}
		System.out.println("PreReverse" + totalPath + "prereverse printed");
		Collections.reverse(totalPath);
		System.out.println("startPath");
		System.out.println(totalPath.toString());
		System.out.println("endPath");
		return totalPath;
	}
	
	/**
	 * sets the start Node
	 * @param startNodeIn
	 */
	public void setStartNode(int startNodeIn){
		startNode = startNodeIn;
	}
	/**
	 * sets the end Node
	 * @param endNodeIn
	 */
	public void setEndNode(int endNodeIn){
		endNode = endNodeIn;
	}
	/**
	 * gets the start Node
	 * @return starting Node
	 */
	public int getStartNode(){
		return startNode;
	}
	/**
	 * gets the end Node
	 * @return ending Node
	 */
	public int getEndNode(){
		return endNode;
	}
	/**
	 * gets the route 
	 * @return the route
	 */
	public ArrayList<Integer> getRoute(){
		System.out.println("start of route");
		System.out.println(route.toString());
		System.out.println("end of route");
		return route;
	}

}
