package edu.wpi.off.by.one.errors.code.model;
import javafx.scene.image.Image;

public class Map {
	
	String name;
	String imagePath;
	Coordinate center;
	float rotation;
	float scale;
	private Image myimg;

	private void updateImg(){
		myimg = new Image("/edu/wpi/off/by/one/errors/code/resources/maps/images/" + imagePath);
	}
	
	public Map(String path, Coordinate coordinate, float rotation, float scale){
		this.imagePath = path;
		this.center = coordinate;
		this.rotation = rotation;
		this.scale = scale;
		System.out.println(path);
		updateImg();
	}
	
	public Map(){
		this.imagePath = "";
		this.center = new Coordinate(0);
		this.rotation = 0;
		this.scale = 0;
		//updateImg();
	}
	
	public Map (String name, String imagePath, float rotation, float scale){
		this.name = name;
		this.imagePath = imagePath;
		this.center = new Coordinate(0, 0, 0);
		this.rotation = rotation;
		this.scale = scale;
		updateImg();
	}
	
	public Map (String name, String imagePath, Coordinate center, float rotation, float scale){
		this.name = name;
		this.imagePath = imagePath;
		this.center = center;
		this.rotation = rotation;
		this.scale = scale;
		updateImg();
	}
	
	public void setName(String name) { this.name = name; }
	public void setImgUrl(String path) { this.imagePath = path; updateImg();}
	public void setCenter(Coordinate coordinate) { this.center = coordinate; }
	public void setRotation(float rotationIn) { this.rotation = rotationIn; }
	public void setScale(float scale) { this.scale = scale; }
	public String getName() { return this.name; }
	public String getImgUrl() { return this.imagePath; }
	public Coordinate getCenter() { return this.center;}
	public float getRotation() { return this.rotation;  }
	public float getScale() { return this.scale; }
	public Image getImage() { return this.myimg; }

}
