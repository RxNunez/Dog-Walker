package models;

import java.util.ArrayList;
import java.util.List;

public class Walker {

    private String walkerName;
    private static ArrayList<Walker> instances = new ArrayList<>();
//    private List<WalkerDog> walkerDogs;
    private int id;

    public Walker(String walkerName) {
        this.walkerName = walkerName;
        instances.add(this);
//        walkerDogs = new ArrayList<WalkerDog>();
        this.id = instances.size();
    }

    public String getWalkerName() {
        return walkerName;
    }

}