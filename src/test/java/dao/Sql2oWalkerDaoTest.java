package dao;

import models.Walker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oWalkerDaoTest {

    private Sql2oWalkerDao walkerDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        walkerDao = new Sql2oWalkerDao(sql2o);

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

    @Test
    public void clearAllClearsAll() throws Exception {
        Walker walker = setupNewWalker();
        Walker otherWalker = new Walker("Hogs");
        walkerDao.add(walker);
        walkerDao.add(otherWalker);
        int daoSize = walkerDao.getAll().size();
        walkerDao.clearAllWalker();
        assertTrue(daoSize > 0 && daoSize > walkerDao.getAll().size()); //this is a little overcomplicated, but illustrates well how we might use `assertTrue` in a different way.
    }



    //helper methods
    public Walker setupNewWalker() {
        return new Walker("Ryan");
    }
}