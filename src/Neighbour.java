// This class represents the list of all points that are adjacent to another point
//
public class Neighbour {
	
	private int pointIndex;
	private Neighbour nextAdj;
	private double distance;
	
	//constructor for the Neighbour class
	//takes in an int which represents the index of the point in the array of locations
	//and a double that represents the distance between the two points 
	public Neighbour(int pI, Neighbour nextNeigh, double dist){
		
		setPointIndex(pI);
		setNextAdj(nextNeigh);
		setDistance(dist);
	}// end of Neighbour constructor
	
	//sets the pointIndex of the location in question
	public void setPointIndex(int pI){
		pointIndex = pI;
	}//end of method setPointIndex
	
	//returns the point index number
	public int getPointIndex(){
		return pointIndex;
	}//end of method getPointIndex
	
	//sets the list of adjacent locations
	public void setNextAdj(Neighbour nextNeigh){
		nextAdj = nextNeigh;
	}//end of method setNextAdj
	
	//returns the list of adjacent locations
	public Neighbour getNextAdj(){
		return nextAdj;
	} //end of method getNextAdj
	
	//sets the distance between the two points
	public void setDistance(double dist){
		distance = dist;
	}//end of method setDistance
	
	//returns the distance between the two points
	public double getDistance(){
		return distance;
	}//end of method getDistance

}
