package dao;

import models.Walker;
import models.Dog;

import java.util.List;


public interface WalkerDao {

    //create
    void add(Walker walker);

    //read
    List<Walker> getAll();
    List<Dog> getAllDogByWalker(int walkerId);

    //find
    Walker findById(int id);

    //update
    void update(int id, String walkerName);

    //delete
    void deleteWalkerById(int id);

    //clearAll
    void clearAllWalker();
}