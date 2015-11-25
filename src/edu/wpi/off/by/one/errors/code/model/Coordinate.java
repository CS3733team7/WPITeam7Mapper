package edu.wpi.off.by.one.errors.code.model;
/**
 * This class represents a 3 dimensional coordinate point
 */
public class Coordinate {
	/** The X part of the coordinate */
	private float x;

	/** The Y part of the coordinate */
	private float y;

	/** The Z part of the coordinate */
	private float z;

	/**
	 * @param x The given x-coordinate
	 * 
	 * @param y The given y-coordinate
	 * 
	 * @param z The given z-coordinate
	 */
	public Coordinate(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Coordinate(float x, float y){
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	/**
	 * Constructor 3 This constructor takes in 1 float value and initializes the
	 * x, y and z coordinates with the given value
	 * 
	 * @param value Single float number to initialize x, y and z
	 */
	public Coordinate(float value) {
		x = value;
		y = value;
		z = value;
	}
	
	/**
	 * Gets the x coordinate
	 * @return The x coordinate.
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * Gets the y coordinate
	 * @return The y coordinate.
	 */
	public float getY(){
		return y;
	}
	
	/**
	 * Gets the z coordinate
	 * @return The z coordinate.
	 */
	public float getZ(){
		return z;
	}
	
	public void setAll(float newX, float newY, float newZ){
		x = newX;
		y = newY;
		z = newZ;
	}
}
