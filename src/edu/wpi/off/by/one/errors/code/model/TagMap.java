package edu.wpi.off.by.one.errors.code.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TagMap {
	private static TagMap tagMap = new TagMap();
	private HashMap<String, ArrayList<Id>> tmap;
	
	private TagMap(){
		tmap = new HashMap<String, ArrayList<Id>>();
	}
	
	public static TagMap getTagMap(){
		return tagMap;
	}
	
	public Set<String> getTags(){
		return tmap.keySet();
	}
	
	public void add(String newTag, Id n){
		if(tmap.containsKey(newTag)){
			ArrayList<Id> tempList = tmap.get(newTag);
			if(!tempList.contains(n)){
				tempList.add(n);
				tmap.put(newTag, tempList);
			}
		} else{
			ArrayList<Id> tempList = new ArrayList<Id>();
			tempList.add(n);
			tmap.put(newTag, tempList);
		}
	}
	
	public ArrayList<Id> find(String searchTerm){
		return tmap.get(searchTerm);
	}
	
	public void remove(String tag, Id n){
		ArrayList<Id> tempList = tmap.get(tag);
		if(tempList != null){
			tempList.remove(n);
			tmap.put(tag, tempList);
		}
	}
	
	public void modify(String oldKey, String newKey){
		ArrayList<Id> temp = tmap.get(oldKey);
		tmap.remove(oldKey);
		tmap.put(newKey, temp);
	}
}
