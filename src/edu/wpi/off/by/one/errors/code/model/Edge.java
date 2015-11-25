package edu.wpi.off.by.one.errors.code.model;

public class Edge {
    private Id nodeA;//ID of Node A
    private Id nodeB;//ID of Node B
    private Id id;
    private float length;
    
    /**
     * the default constructor for Edge
     * @param nodeAIn: The given id of Node A
     * @param nodeBIn: The given id of Node B
     */
    public Edge(Id nodeAIn, Id nodeBIn) {
        nodeA = nodeAIn;
        nodeB = nodeBIn;
        id = new Id();//default, set when added
        length = -1.0f;
        
    }
    public Edge(Id nodeAIn, Id nodeBIn, Id eid) {
        nodeA = nodeAIn;
        nodeB = nodeBIn;
        id = eid;//default, set when added
	}
	/**
     * getter for the first Node in the edge
     * @return nodeA: the first node
     */
    public Id getNodeA() {
        return this.nodeA;
    }
    /**
     * getter for the second Node in the edge
     * @return nodeB: the second node
     */
    public Id getNodeB() {
        return this.nodeB;
    }
    /**
     * gets the ID of the Edge
     * @return id: the id of the edge
     */
    public Id getId() {
        return this.id;
    }
    /**
     * return the length of the edge between A and B
     * @return length: the linear distance between the nodes
     */
    public float getLength() {
        return this.length;
    }
    
    /**
     * set the first Node
     * @param nodeAIn: The new id of NodeA
     */
    public void setNodeA(Id nodeAIn) {
        nodeA = nodeAIn;
    }
    
    /**
     * set the second Node
     * @param nodeBIn: The new id of NodeB
     */
    public void setNodeB(Id nodeBIn) {
        nodeB = nodeBIn;
    }
    
    /**
     * set the ID of the edge
     * @param i: the indice of the id
     * @param u: the unique of the id
     */
    public void setId(int i, int u) {
        id.indice = i;
        id.unique = u;
    }
    
    /**
     * #TODO
     * update the length of this edge
     */
    public void updateLength(float len) {
        length = len;
        // We need the ID system implemented before we can get nodes properly
        // for edge length updating
    }
    //just triggers a len update
    public void updateLength(Graph g){
        Node A = g.returnNodeById(nodeA);
        Node B = g.returnNodeById(nodeB);
        if(A == null || B == null) g.deleteEdge(id);

        else {
    		float xDist = A.getCoordinate().getX()-B.getCoordinate().getX();
    		float yDist = A.getCoordinate().getY()-B.getCoordinate().getY();
        	length = ((float) Math.sqrt((xDist*xDist)+(yDist*yDist)));
        }
    }
    
}