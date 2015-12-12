package edu.wpi.off.by.one.errors.code.model;

import java.util.ArrayList;
import java.util.Vector;

public class Node {
    private Coordinate coord;
    private Vector<Id> edges;//list of indexes of edges
    private Id id;
    private ArrayList<String> tags;//list of tags the node has
    private TagMap tagMap;
    private boolean accessible = true;
    private boolean food = false;
    private boolean mens = false;
    private boolean womens = false;
    private boolean genderNeutral = false;
    private String name = "";
    
    /**
     *
     * @param coordIn: The given coordinate
     */
    public Node(Coordinate coordIn) {
        this.edges = new Vector<Id>(); // we can totally use arraylist here, dont have to use a vector
        this.coord = coordIn;
        this.id = new Id();//default, set when added
        tags = new ArrayList<String>();
        tagMap = TagMap.getTagMap();
    }
    /**
     *
     * @param coordIn: The given coordinate
     * @param nid: the  for the Node
     * Dont use this unless you know what you are doing
     */
    public Node(Coordinate coordIn, Id nid) {
        this.edges = new Vector<Id>(); // we can totally use arraylist here, dont have to use a vector
        this.coord = coordIn;
        this.id = nid;//default, set when added
        tags = new ArrayList<String>();
        tagMap = TagMap.getTagMap();
    }
    
	/**
     * get the coordinate for the node
     * @return the Node's coordinate
     */
    public Coordinate getCoordinate() {
        return this.coord;
    }
    
    /**
     * set the coordinate for the Node
     * @param newCoord: The new Coordinate
     */
    public void setCoordinate(Coordinate newCoord) {
        this.coord = newCoord;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public boolean addEdgeId(Id id){
        return this.edges.add(id);
    }

    public void removeEdge(Id id){
        while(edges.remove(id));
    }
    
    /**
     * get the list of connected edges for the node
     * @return edges: the list of connected edges
     */
    
    public Vector<Id> getEdgelist() {
        
        return edges;
    }
    
    /**
     * set the edges to the passed in list
     * @param newEdgeList: The new list of edges
     */
    public void setEdgeList(Vector<Id> newEdgeList) {
        this.edges = newEdgeList;
    }
    /**
     *  get the Id for the node
     * @return id: the Node's ID
     */
    public Id getId() {
        return id;
    }
    
    /**
     * setter for the Node's id
     * @param i: the indice of the id
     * @param u: the unique of the id
     */
    public void setId(int i, int u) {
        id.indice = i;
        id.unique = u;
    }
    
    /**
     * Getter for tag list
     * @return tags: the Node's tag list
     */
    public ArrayList<String> GetTags(){
    	return tags;
    }
    
    /**
     * Given an index, returns the tag at that location in the array
     * @param i: index of the tag
     * @return the tag stored at index i
     */
    public String GetTagAtIndex(int i){
    	return tags.get(i);
    }
    
    /**
     * returns the index of a given tag
     * @param tag: the tag to search for
     * @return the index of tag
     */
    public int GetIndexOfTag(String tag){
    	return tags.indexOf(tag);
    }
    
    /**
     * adds a new tag to the Node's tag list
     * @param newTag: the new tag to add to the list
     */
    public void addTag(String newTag){
    	tags.add(newTag);
    	tagMap.add(newTag, id);
    }
    
    /**
     * Removes a given tag from the array
     * @param tag: tag to remove
     */
    public void removeTag(String tag){
    	tags.remove(tag);
    	tagMap.remove(tag, id);
    }
    
    /**
     * Removes tag at given index from the array
     * @param i: index to remove tag from
     */
    public void removeTagAtIndex(int i){
    	tagMap.remove(tags.get(i), id);
    	tags.remove(i);
    }
    
    /**
     * replaces an existing tag with a new one to allow for editing tags
     * @param oldTag: the old tag to change
     * @param newTag: the new tag to replace it with
     */
    public void modifyTag(String oldTag, String newTag){
    	tags.set(tags.indexOf(oldTag), newTag);
    	tagMap.modify(oldTag, newTag);
    }
    
    /**
     * replaces existing tag at a given index with a new tag for editing tags
     * @param i: the index of the old tag
     * @param newTag: the new tag to replace it with
     */
    public void modifyTagAtIndex(int i, String newTag){
    	tagMap.modify(tags.get(i), newTag);
    	tags.set(i, newTag);
    }


    public float getDistanceSq(Coordinate c){
        float mx = coord.getX() - c.getX();
        float my = coord.getY() - c.getY();
        return mx * mx + my * my;
    }
    public float getDistance(Coordinate c){
        return (float)Math.sqrt((double)getDistanceSq(c));
    }
    
    public boolean isAccessible(){
    	return accessible;
    }
    
    public void setAccessible(boolean accessibility){
    	accessible = accessibility;
    }
    public boolean isFood(){
    	return food;
    }
    
    public void setFood(boolean foodIn){
    	food = foodIn;
    }
    public boolean isMens(){
    	return mens;
    }
    
    public void setMens(boolean mensIn){
    	mens = mensIn;
    }
    public boolean isWomens(){
    	return womens;
    }
    
    public void setWomens(boolean womensIn){
    	womens = womensIn;
    }
    public boolean isGenderNeutral(){
    	return genderNeutral;
    }
    
    public void setGenderNeutral(boolean genNeutIn){
    	genderNeutral = genNeutIn;
    }
}