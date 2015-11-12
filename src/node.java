
public class node {
  
  	private String name;
  	private Neighbour adjList;
	
	//constructor for the Node(location)
	//takes in a String representing the locations name and a List of Adjacent locations
	public Node(String nme, Neighbour list){
		
		setName(nme);
		setAdjList(list);
	}//end of constructor
	
	//sets the Name of the location
	public void setName(String Name){
		name = Name;
	}//end of method setName
	
	//returns the Name of the location
	public String getName(){
		return name;
	}//end of method getName
	
	//sets the list of the location's Neighbours.
	public void setAdjList(Neighbour list){
		adjList = list;
	}//end of method setAdjList
	
	//returns the list of adjacent points
	public Neighbour getAdjList(){
		return adjList;
	}//end of method getAdjList
	
	

}
