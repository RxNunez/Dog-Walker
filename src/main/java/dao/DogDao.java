package dao;

import models.Dog;
import models.Walker;

import java.util.List;

public interface DogDao {


    public interface DogDao {

        //create
        void add(Dog walker);

        //read
        List<Dog> getAll();

        //find
        Dog findById(int id);

        //update
        void update(int id, String dogName, String breed, String color, int walkerId);

        //delete
        void deleteDogById(int id);

        //clearAll
        void clearAllDog();
    }
}
