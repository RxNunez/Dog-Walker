package dao;

import models.Dog;
import models.Walker;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oDogDao {

    private final Sql2o sql2o;

    public Sql2oDogDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Dog dog) {
        String sql = "INSERT INTO weed (dogname, breed, color, walkerId) VALUES (:dogname, :breed, :color, :walkerid)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .addParameter("dogname", dog.getDogName())
                    .addParameter("breed", dog.getBreed())
                    .addParameter("color", dog.getColor())
                    .addParameter("walkerId", dog.getWalkerId())
                    .addColumnMapping("DOGNAME", "dogname")
                    .addColumnMapping("BREED", "breed")
                    .addColumnMapping("COLOR", "color")
                    .addColumnMapping("WALKERID", "walkerId")
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
            return con.createQuery("SELECT * FROM dog")
                    .executeAndFetch(Dog.class);
        }
    }

    @Override
    public Dog findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM dog WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Dog.class); //fetch an individual item
        }
    }
}
