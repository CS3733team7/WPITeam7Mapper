package edu.wpi.off.by.one.errors.code.model;

import java.util.*;

//graph class manages edges and nodes
public class Graph {
	private int node_count = 0;
	private int node_arrayfirstopen= 0;
	private int node_arraylasttaken = -1;
	private int node_arraysize = 0;
	private Vector<Node> listOfNodes;
	private int edge_count = 0;
	private int edge_arrayfirstopen= 0;
	private int edge_arraylasttaken = -1;
	private int edge_arraysize = 0;
	private Vector<Edge> listOfEdges;

	public Graph(){
		listOfNodes = new Vector<Node>(); //array list of nodes
		listOfEdges = new Vector<Edge>(); //array list of edges
	}
	
	/**
	 * Add Node by coordinates
	 * @param coordIn
	 * @return node
	 */

	public Node addNode(Coordinate coordIn){//adds a node to the list
		node_count++;
		for(; node_arrayfirstopen < node_arraysize && listOfNodes.get(node_arrayfirstopen) != null; node_arrayfirstopen++);
		if(node_arrayfirstopen >= node_arraysize){	//resize
			node_arraysize = node_arrayfirstopen+1;
			listOfNodes.ensureCapacity(node_arraysize);
			listOfNodes.setSize(node_arraysize);
		}
		Id nid = new Id(node_arrayfirstopen, node_count);
		Node n = new Node(coordIn, nid);
		listOfNodes.set(node_arrayfirstopen, n);
		
		if(node_arraylasttaken < node_arrayfirstopen) node_arraylasttaken = node_arrayfirstopen; //todo redo
		return n;
	}
	
	/**
	 * Add Node and return its id 
	 * @param coordIn
	 * @return node's id 
	 */
	public Id addNodeRint(Coordinate coordIn){//adds a node to the list, returns ID instead of node
		node_count++;
		for(; node_arrayfirstopen < node_arraysize && listOfNodes.get(node_arrayfirstopen) != null; node_arrayfirstopen++);
		if(node_arrayfirstopen >= node_arraysize){	//resize
			node_arraysize = node_arrayfirstopen+1;
			listOfNodes.ensureCapacity(node_arraysize);
			listOfNodes.setSize(node_arraysize);
		}
		Id nid = new Id(node_arrayfirstopen, node_count);
		Node n = new Node(coordIn, nid);
		listOfNodes.set(node_arrayfirstopen, n);
		
		if(node_arraylasttaken < node_arrayfirstopen) node_arraylasttaken = node_arrayfirstopen; //todo redo
		return nid;
	}
	
	/**
	 * return Node by Id
	 * @param id
	 * @return node that matches the id; null if couldn't find it
	 */
	public Node returnNodeById(Id id){
		Node n = listOfNodes.elementAt(id.indice);
		if(n == null) return null;
		Id nid = n.getId();
		if(nid.indice != id.indice || nid.unique != id.unique) return null; 
		return n;
	}
	
	/**
	 * add edge by two nodes
	 * @param nodeAIn
	 * @param nodeBIn
	 * @return added edge
	 */
	public Edge addEdge(Id nodeAIn, Id nodeBIn){//adds an edge to the list
		Node A = returnNodeById(nodeAIn);
		Node B = returnNodeById(nodeBIn);
		if(A == null || B == null) return null;
		edge_count++;
		for(; edge_arrayfirstopen < edge_arraysize && listOfEdges.get(edge_arrayfirstopen) != null; edge_arrayfirstopen++);
		if(edge_arrayfirstopen >= edge_arraysize){	//resize
			edge_arraysize = edge_arrayfirstopen+1;
			listOfEdges.ensureCapacity(edge_arraysize);
			listOfEdges.setSize(edge_arraysize);
		}
		Id eid = new Id(edge_arrayfirstopen, edge_count);
		Edge e = new Edge(nodeAIn, nodeBIn, eid);
		listOfEdges.set(edge_arrayfirstopen, e);
		
		if(edge_arraylasttaken < edge_arrayfirstopen) edge_arraylasttaken = edge_arrayfirstopen; //todo redo

		A.addEdgeId(eid);
		B.addEdgeId(eid);
		e.updateLength(this);
		return e;
	}
	/**
	 * add edge and return its id
	 * @param nodeAIn
	 * @param nodeBIn
	 * @return id of added edge
	 */
	public Id addEdgeRint(Id nodeAIn, Id nodeBIn){//adds an edge to the list, returns ID instead of edge
		Node A = returnNodeById(nodeAIn);
		Node B = returnNodeById(nodeBIn);
		if(A == null || B == null) return null;
		edge_count++;
		for(; edge_arrayfirstopen < edge_arraysize && listOfEdges.get(edge_arrayfirstopen) != null; edge_arrayfirstopen++);
		if(edge_arrayfirstopen >= edge_arraysize){	//resize
			edge_arraysize = edge_arrayfirstopen +1;
			listOfEdges.ensureCapacity(edge_arraysize);
			listOfEdges.setSize(edge_arraysize);
		}
		Id eid = new Id(edge_arrayfirstopen, edge_count);
		Edge e = new Edge(nodeAIn, nodeBIn, eid);
		listOfEdges.set(edge_arrayfirstopen, e);
		
		if(edge_arraylasttaken < edge_arrayfirstopen) edge_arraylasttaken = edge_arrayfirstopen; //todo redo

		A.addEdgeId(eid);
		B.addEdgeId(eid);
		e.updateLength(this);
		return eid;
	}
	
	/**
	 * return edge by id
	 * @param id
	 * @return edge that matches the id; null if couldn't find it
	 */
	public Edge returnEdgeById(Id id){
		Edge e = listOfEdges.elementAt(id.indice);
		if(e == null) return null;
		Id eid = e.getId();
		if(eid.indice != id.indice || eid.unique != id.unique) return null; 
		return e;
	}
	
	public Vector<Node> getNodes(){//gets the list of nodes
		return this.listOfNodes;
	}
	
	public Vector<Edge> getEdges(){//gets the list of edges
		return this.listOfEdges;
	}
	
	/**
	 * delete node
	 * @param target
	 */
	public boolean deleteNode(Id target){//deletes a node from the list (replaces it with null)
		Node n = returnNodeById(target);
		if(n == null) return false;
		listOfNodes.set(target.indice, null);
		if(target.indice< node_arrayfirstopen) node_arrayfirstopen = target.indice;
		for(; node_arraylasttaken > 0 && listOfNodes.get(node_arraylasttaken) == null; node_arraylasttaken--);
		return true;
	}
	
	/**
	 * delete edge
	 * @param target
	 */
	public boolean deleteEdge(Id target){//deletes an edge from the list (replaces it with null)
		Edge e = returnEdgeById(target);
		if(e == null) return false;
		Node a = returnNodeById(e.getNodeA());
		Node b = returnNodeById(e.getNodeB());
		if(a != null) a.removeEdge(target);
		if(b != null) b.removeEdge(target);
		listOfEdges.set(target.indice, null);
		if(target.indice< edge_arrayfirstopen) edge_arrayfirstopen = target.indice;
		for(; edge_arraylasttaken > 0 && listOfEdges.get(edge_arraylasttaken) == null; edge_arraylasttaken--);
		return true;
	}
	
	/**
	 * edit node 
	 * @param target
	 * @param newCoord
	 */
	public void editNode(Id target, Coordinate newCoord){//edits a node on the list
		Node n = returnNodeById(target);
		if(n != null){
			n.setCoordinate(newCoord);
			listOfNodes.set(target.indice, n);
		}
	}
	
	public void editEdge(){//edits an edge on the list
		//TODO: add functionality after we decide exactly what this should do
	}
}
