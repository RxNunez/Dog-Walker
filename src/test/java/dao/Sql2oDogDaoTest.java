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

    @Test
    public void noDogReturnsEmptyList() throws Exception {
        Assert.assertEquals(0, dogDao.getAll().size());
    }

    @Test
    public void deleteByIdDeletesCorrectDog() throws Exception {
        Dog dog = new Dog("Pudgy","Pitbull","Brindle",1);
        int originalDogId = dog.getId();
        dogDao.add(dog);
        dogDao.deleteDogById(dog.getId());
        Assert.assertEquals(0, dogDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Dog dog = setupNewDog();
        Dog otherDog = new Dog("Pheonix", "German Sheppard", "BlackBrown", 2);
        dogDao.add(dog);
        dogDao.add(otherDog);
        int daoSize = dogDao.getAll().size();
        dogDao.clearAllDog();
        assertTrue(daoSize > 0 && daoSize > dogDao.getAll().size());
    }

    @Test
    public void updateChangesDogContent() throws Exception {
        String initialBreed = "Lab";
        Dog dog = new Dog ("Boss",initialBreed,"Black",1);
        dogDao.add(dog);

        dogDao.update(1, "", "", "", 1);
        Dog updatedDog = dogDao.findById(dog.getId());
        assertNotEquals(initialBreed, updatedDog.getBreed());
    }

        //helper methods
    public Dog setupNewDog() {
        return new Dog("Pudgy","Pitbull","Brindle",1);


    }

}