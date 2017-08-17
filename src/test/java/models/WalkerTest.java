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
    }

    @Test
    public void NewWalkerObjectGetsCorrectlyCreates_true() throws Exception {
        Walker walker = new Walker("Ryan");
        assertEquals(true, walker instanceof Walker);
    }
}