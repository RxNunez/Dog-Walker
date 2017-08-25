package dao;

import models.Walker;
import models.Dog;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oWalkerDao implements WalkerDao {

    private final Sql2o sql2o;

    public Sql2oWalkerDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Walker walker) {
        String sql = "INSERT INTO walker (walkerName, dogId) VALUES (:walkername, :dogid)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .addParameter("walkername", walker.getWalkerName())
                    .addParameter("dogId", walker.getDogId())
                    .addColumnMapping("WALKERNAME", "walkerName")
                    .addColumnMapping("DOGID", "dogId")
                    .executeUpdate()
                    .getKey();
            walker.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Walker> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM walker")
                    .executeAndFetch(Walker.class);
        }
    }

    @Override
    public Walker findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM walker WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Walker.class);
        }
    }

    @Override
    public void update(int id, String newWalkerName) {
        String sql = "UPDATE walker SET (walkerName) = (:walkerName) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("walkerName", newWalkerName)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteWalkerById(int id) {
        String sql = "DELETE from walker WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllWalker() {
        String sql = "DELETE from walker";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Dog> getAllDogByWalker(int walkerId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM dog WHERE walkerId = :walkerid")
                    .addParameter("walkerid", walkerId)
                    .executeAndFetch(Dog.class);
        }

    }
}