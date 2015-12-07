package edu.wpi.off.by.one.errors.code.application;

import javafx.scene.image.ImageView;

public class MarkerDisplay extends ImageView{
	
	public double x, y;
	boolean isOnNode = false;
	
	static String imgPath = "marker.png";
	
	public MarkerDisplay(double x, double y){
		super(imgPath);
		this.x = x;
		this.y = y;
	}
}
