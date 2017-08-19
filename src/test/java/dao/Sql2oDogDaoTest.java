package dao;


import models.Dog;
import models.Walker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class Sql2oDogDaoTest {

    private Sql2oDogDao dogDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dogDao = new Sql2oDogDao(sql2o);

        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Dog dog = new Dog("Pudgy","Pitbull","Brindle",1);
        int originalDogId = dog.getId();
        dogDao.add(dog);
        assertNotEquals(originalDogId, dog.getId());
    }

    @Test
    public void existingDogsCanBeFoundById() throws Exception {
        Dog dog = new Dog("Pudgy","Pitbull","Brindle",1);
        int originalDogId = dog.getId();
        dogDao.add(dog);
        Dog foundDog = dogDao.findById(dog.getId());
        Assert.assertEquals(dog, foundDog);
    }

    @Test
    public void addedDogsAreReturnedFromGetAll() throws Exception {
        Dog dog = new Dog("Pudgy","Pitbull","Brindle",1);
        int originalDogId = dog.getId();
        dogDao.add(dog);
        Assert.assertEquals(1, dogDao.getAll().size());
    }

    //helper methods
    public Dog setupNewDog() {
        return new Dog("Pudgy","Pitbull","Brindle",1);


    }

}