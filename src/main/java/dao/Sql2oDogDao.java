package dao;

import models.Dog;
import models.Walker;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oDogDao implements DogDao{

    private final Sql2o sql2o;

    public Sql2oDogDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Dog dog) {
        String sql = "INSERT INTO dogs (dogname, breed, color) VALUES (:dogname, :breed, :color)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .addParameter("dogname", dog.getDogName())
                    .addParameter("breed", dog.getBreed())
                    .addParameter("color", dog.getColor())
                    .addColumnMapping("DOGNAME", "dogname")
                    .addColumnMapping("BREED", "breed")
                    .addColumnMapping("COLOR", "color")
                    .bind(dog)
                    .executeUpdate()
                    .getKey();
            dog.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Dog> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM dogs")
                    .executeAndFetch(Dog.class);
        }
    }

    @Override
    public Dog findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM dogs WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Dog.class);
        }
    }

    @Override
    public void update(int id, String newDogName, String newBreed, String newColor){
        String sql = "UPDATE dogs SET (dogname, breed, color) = (:dogname, :breed, :color) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("dogname", newDogName)
                    .addParameter("breed", newBreed)
                    .addParameter("color", newColor)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteDogById(int id) {
        String sql = "DELETE from dogs WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllDog() {
        String sql = "DELETE from dogs";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Walker> getAllWalkerByDog(int dogId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM walkers WHERE dogId = :dogid")
                    .addParameter("dogid", dogId)
                    .executeAndFetch(Walker.class);
        }
    }
}
