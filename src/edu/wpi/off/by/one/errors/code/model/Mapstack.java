package edu.wpi.off.by.one.errors.code.model;

import java.util.ArrayList;

/**
 * Created by roboman2444 on 12/15/15.
 */
public class Mapstack {
    public ArrayList<Integer> meps;
    public ArrayList<Id> nodes;
    public String name;

    public Mapstack(String inname){
        meps = new ArrayList<Integer>();
        nodes = new ArrayList<Id>();
        name = inname;
    }
    public void addm(int i){
        if(meps.contains(i))return;
        meps.add(i);
    }
    public void addn(Id i){
        if(nodes.contains(i))return;
        nodes.add(i);
    }
    public void remn(Id i){
        nodes.remove(i);
    }
}
