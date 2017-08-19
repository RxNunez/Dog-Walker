package dao;

import models.Walker;
import models.Dog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

public class Sql2oWalkerDaoTest {

    private Sql2oWalkerDao walkerDao;
    private Sql2oDogDao dogDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        walkerDao = new Sql2oWalkerDao(sql2o);
        dogDao = new Sql2oDogDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Walker walker = new Walker("Ryan");
        int originalWalkerId = walker.getId();
        walkerDao.add(walker);
        assertNotEquals(originalWalkerId, walker.getId());
    }


    @Test
    public void existingWalkersCanBeFoundById() throws Exception {
        Walker walker = new Walker("Ryan");
        walkerDao.add(walker);
        Walker foundWalker = walkerDao.findById(walker.getId());
        assertEquals(walker, foundWalker);
    }

    @Test
    public void addedWalkersAreReturnedFromGetAll() throws Exception {
        Walker walker = new Walker("Ryan");
        walkerDao.add(walker);
        assertEquals(1, walkerDao.getAll().size());
    }

    @Test
    public void noWalkerReturnsEmptyList() throws Exception {
        assertEquals(0, walkerDao.getAll().size());
    }

    @Test
    public void deleteByIdDeletesCorrectWalker() throws Exception {
        Walker walker = new Walker("Ryan");
        walkerDao.add(walker);
        walkerDao.deleteWalkerById(walker.getId());
        assertEquals(0, walkerDao.getAll().size());
    }

    public void clearAllClearsAll() throws Exception {
        Walker walker = setupNewWalker();
        Walker otherWalker = new Walker("Hogs");
        walkerDao.add(walker);
        walkerDao.add(otherWalker);
        int daoSize = walkerDao.getAll().size();
        walkerDao.clearAllWalker();
        assertTrue(daoSize > 0 && daoSize > walkerDao.getAll().size());
    }

    @Test
    public void getAllDogByWalkerReturnsDogsCorrectly() throws Exception {
        Walker walker = setupNewWalker();
        walkerDao.add(walker);
        int walkerId = walker.getId();
        Dog newDog = new Dog("Boss","","", walkerId);
        Dog otherDog = new Dog("Jedi ","","", walkerId);
        Dog thirdDog = new Dog("Cinderella","","", walkerId);
        dogDao.add(newDog);
        dogDao.add(otherDog);
        assertTrue(walkerDao.getAllDogByWalker(walkerId).size() == 2);
        assertTrue(walkerDao.getAllDogByWalker(walkerId).contains(newDog));
        assertTrue(walkerDao.getAllDogByWalker(walkerId).contains(otherDog));
        assertFalse(walkerDao.getAllDogByWalker(walkerId).contains(thirdDog));
    }



    //helper methods
    public Walker setupNewWalker() {
        return new Walker("Ryan");
    }
}