package dao;

import models.Walker;

import java.util.List;


public interface WalkerDao {

    //create
    void add(Walker walker);

    //read
    List<Walker> getAll();

    //find
    Walker findById(int id);

    //update
    void update(int id, String walkerName);

    //delete
    void deleteById(int id);

    //clearAll
    void clearAllWalker();
}