package models;

import java.util.ArrayList;
import java.util.List;

public class Walker {

    private String walkerName;
    private boolean completed;
    private int dogId;
    private int id;

    public Walker(String walkerName) {
        this.walkerName = walkerName;
        this.completed = false;
        this.dogId = dogId;
    }

    public String getWalkerName() {
        return walkerName;
    }

    public static ArrayList<Walker> getAll(){
        return instances;
    }

    public static void clearAllWalker() {
        instances.clear();
    }

    public int getId(){
        return id;
    }

    public static Walker findById(int id){
        return instances.get(id-1);
    }

    public void update (String walkerName){
        this.walkerName = walkerName;
    }

    public void deleteWalker (){
        instances.remove(id-1);
    }

}
