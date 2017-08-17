package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalkerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Walker.clearAllWalker();
    }

    @Test
    public void NewWalkerObjectGetsCorrectlyCreates_true() throws Exception {
        Walker walker = newWalker();
        assertEquals(true, walker instanceof Walker);
    }

    @Test
    public void WalkerInstantiatesWithWalkerName_Ryan() throws Exception {
        Walker walker = newWalker();
        assertEquals("Ryan", walker.getWalkerName());
    }

    @Test
    public void AllWalkerContainsAllWalker_true() {
        Walker walker = newWalker();
        Walker otherWalker = newWalker ();
        assertEquals(true, Walker.getAll().contains(walker));
        assertEquals(true, Walker.getAll().contains(otherWalker));
    }

    @Test
    public void getId_WalkerInstantiateWithAnID_1() throws Exception {
        Walker.clearAllWalker();
        Walker oneWalker = newWalker();
        assertEquals(1, oneWalker.getId());
    }

    public Walker newWalker(){
        return new Walker("Ryan");
    }

}