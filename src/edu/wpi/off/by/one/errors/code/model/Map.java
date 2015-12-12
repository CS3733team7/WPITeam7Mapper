package edu.wpi.off.by.one.errors.code.model;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Map {
	public ArrayList<Integer> goodcolors;
	
	String name;
	String imagePath;
	Coordinate center;
	float rotation;
	float scale;
	private Image myimg;

	private void updateImg(){
		myimg = new Image("/edu/wpi/off/by/one/errors/code/resources/maps/images/" + imagePath);
	}
	public void addColor(int color){
		if(goodcolors == null) goodcolors = new ArrayList<Integer>();
		if(goodcolors.contains(Integer.valueOf(color))) return;
		goodcolors.add(Integer.valueOf(color));
	}
	public int getColor(Coordinate c){
		Matrix mat = new Matrix(new Coordinate(-center.getX(), -center.getY())).scale(1.0/scale).rotate(-rotation, 0.0, 0.0, 1.0);// may not be proper inverse
		Coordinate sc = mat.transform(c);
		int sx = Math.round(sc.getX());
		int sy = Math.round(sc.getY());
		System.out.printf(" %s Eyedrop X %d Eyedrop Y %d, coord X %f coord Y %f, center X %f, center Y %f\n", name, sx, sy, c.getX(), c.getY(), center.getX(), center.getY());
		if(sx < 0 || sx >= myimg.getWidth()) return 0;
		if(sy < 0 || sy >= myimg.getHeight()) return 0;
		return myimg.getPixelReader().getArgb(sx, sy);
	}
	public boolean checkLines(Coordinate start, Coordinate finish){
		//get start/end x and y ints
		Matrix mat = new Matrix(new Coordinate(-center.getX(), -center.getY())).scale(1.0/scale).rotate(-rotation, 0.0, 0.0, 1.0);// may not be proper inverse
		Coordinate sc = mat.transform(start);
		Coordinate ec = mat.transform(finish);
		int sx = Math.round(sc.getX());
		int sy = Math.round(sc.getY());
		int ex = Math.round(ec.getX());
		int ey = Math.round(ec.getY());






		if(sy > ey){
			int t = sy;
			sy = ey;
			ey = t;
			t = sx;
			sx = ex;
			ex = t;
		}
		//todo walk the tree
		float dx = sx - ex;
		if(dx != 0.0){
			float dy = sy - ey;
			float err = 0;
			float derr = Math.abs(dy / dx);
			int x, y = sy;
			for(x = sx; x < ex; x++){
				if(x >= 0 && x < myimg.getWidth() && y >= 0 && y < myimg.getHeight()){
					int color = myimg.getPixelReader().getArgb(x, y);
					int cal;
					for(cal = 0; cal < goodcolors.size(); cal++){
						int clp = goodcolors.get(cal).intValue();
						if(color == clp) break;
					}
					if(cal == goodcolors.size()) return false;
				}
				err += derr;
				while(err >= 0.5f){
					if(x >= 0 && x < myimg.getWidth() && y >= 0 && y < myimg.getHeight()){
						//test all colors
						int color = myimg.getPixelReader().getArgb(x, y);
						int cal;
						for(cal = 0; cal < goodcolors.size(); cal++){
							int clp = goodcolors.get(cal).intValue();
							if(color == clp) break;
						}
						if(cal == goodcolors.size()) return false;
					}
					y++;
					err-=1.0;
				}
			}
		} else { //vertical
			int y;
			for(y = sy; y < ey; y++){
				if(y >= 0 && y < myimg.getHeight()){
					int color = myimg.getPixelReader().getArgb(sx, y);
					int cal;
					for(cal = 0; cal < goodcolors.size(); cal++){
						int clp = goodcolors.get(cal).intValue();
						if(color == clp) break;
					}
					if(cal == goodcolors.size()) return false;
				}
			}
		}
		return true;
	}
	
	public Map(String path, Coordinate coordinate, float rotation, float scale){
		this.imagePath = path;
		this.center = coordinate;
		this.rotation = rotation;
		this.scale = scale;
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
