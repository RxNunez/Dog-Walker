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


    //helper methods
    public Walker setupNewWalker() {
        return new Walker("Ryan");


    }
}