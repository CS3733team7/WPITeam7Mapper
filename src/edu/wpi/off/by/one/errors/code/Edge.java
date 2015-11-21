package edu.wpi.off.by.one.errors.code;

public class Edge {
    private int nodeA;//ID of Node A
    private int nodeB;//ID of Node B
    private int id;
    private float length;
    
    /**
     * the default constructor for Edge
     * @param nodeAIn: The given id of Node A
     * @param nodeBIn: The given id of Node B
     */
    public Edge(int nodeAIn, int nodeBIn) {
        nodeA = nodeAIn;
        nodeB = nodeBIn;
        id = -1;//default, set when added
        // can't set length until we can access the nodes through the ID system
        float length;
        
    }
    /**
     * getter for the first Node in the edge
     * @return nodeA: the first node
     */
    public int getNodeA() {
        return this.nodeA;
    }
    /**
     * getter for the second Node in the edge
     * @return nodeB: the second node
     */
    public int getNodeB() {
        return this.nodeB;
    }
    /**
     * gets the ID of the Edge
     * @return id: the id of the edge
     */
    public int getId() {
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
    public void setNodeA(int nodeAIn) {
        nodeA = nodeAIn;
    }
    
    /**
     * set the second Node
     * @param nodeBIn: The new id of NodeB
     */
    public void setNodeB(int nodeBIn) {
        nodeB = nodeBIn;
    }
    
    /**
     * set the ID of the edge
     * @param idIn: The new id of edge
     */
    public void setId(int idIn) {
        id = idIn;
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
    
}