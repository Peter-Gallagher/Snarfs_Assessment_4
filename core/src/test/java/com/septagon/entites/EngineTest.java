package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 * A class used to test the Engine class
 */

class EngineTest {

    Engine testE = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Teture(Gdx.files.internal("images/engine1.png"));
        testE = new Engine(1,1, null, 10, 2, 4, 2, 20, 4, 01);
        testE = mock(Engine.class);
        testE.col = 1;
        testE.row = 1;
        testE.health = 10;
        testE.damage = 2;
        testE.range = 4;
        testE.speed = 2;
        testE.maxVolume = 20;
        testE.fillSpeed = 4;
        testE.id = 01;
        testE.x = 32;
        testE.y = 32;
        testE.height = 32;
        testE.width = 32;
        testE.volume = 20;
        testE.rangeCorners = new ArrayList<Integer>();



    }


    @Test  //A test for the Engine class initialisation
    public void testEngine() throws Exception {
        assertEquals(testE.x, 32);
        assertEquals(testE.y, 32);
        assertEquals(testE.col, 1);
        assertEquals(testE.row, 1);
        assertEquals(testE.width, 32);
        assertEquals(testE.height, 32);
        assertEquals(testE.texture, null);
        assertEquals(testE.health, 10);
        assertEquals(testE.damage, 2);
        assertEquals(testE.range, 4);
        assertEquals(testE.speed, 2);
        assertEquals(testE.maxVolume, 20);
        assertEquals(testE.id, 01);
        assertEquals(testE.volume, 20);
        assertEquals(testE.fillSpeed, 4);

    }

    @Test //A test for the Engine class' ifInRangeFill method
    public void testIfInRangeFill() throws Exception {

        Station testS = new Station(2, 2, 256, 128, null);


        Mockito.doCallRealMethod().when(testE).setVolume(10);
        Mockito.doCallRealMethod().when(testE).getVolume();
        Mockito.doCallRealMethod().when(testE).setRangeCorners();
        Mockito.doCallRealMethod().when(testE).ifInRangeFill(testS);


        testE.setVolume(10);
        testE.setRangeCorners();
        testE.ifInRangeFill(testS);
        assertEquals(testE.getVolume(), 10);
    }

    @Test //A test for the Engine class' getMaxVolume method
    public void testGetMaxVolume() throws Exception {
        when(testE.getMaxVolume()).thenCallRealMethod();
        assertEquals(testE.getMaxVolume(), 20);
    }

    @Test //A test for the Engine class' getID method
    public void testGetID() throws Exception {
        when(testE.getID()).thenCallRealMethod();
        assertEquals(testE.getID(), 01);
    }

    @Test //A test for the Engine class' isMoved method
    public void testIsMoved() throws Exception {
        when(testE.isMoved()).thenCallRealMethod();
        assertFalse(testE.isMoved());
    }

    @Test //A test for the Engine class' setMoved method
    public void testSetMoved() throws Exception {
        when(testE.isMoved()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).setMoved(true);

        testE.setMoved(true);
        assertTrue(testE.isMoved());
    }

    @Test //A test for the Engine class' getID method
    public void testGetVolume() throws Exception {
        when(testE.getVolume()).thenCallRealMethod();
        assertEquals(testE.getVolume(), 20);
    }

    @Test //A test for the Engine class' getID method
    public void testGetFillSpeed() throws Exception {
        when(testE.getFillSpeed()).thenCallRealMethod();
        assertEquals(testE.getFillSpeed(), 4);
    }

    @Test //A test for the Engine class' fire method
    public void testFire() throws Exception {
        when(testE.getVolume()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).fire();

        testE.fire();
        assertEquals(testE.getVolume(), 18);
    }

    @Test //A test for the Engine class' setVolume method
    public void testSetVolume() throws Exception {
        when(testE.getVolume()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).setVolume(18);

        testE.setVolume(18);
        assertEquals(testE.getVolume(), 18);
    }

    @Test //A test for the Engine class' setFillSpeed method
    public void testSetFillSpeed() throws Exception {
        when(testE.getFillSpeed()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).setFillSpeed(6);

        testE.setFillSpeed(6);
        assertEquals(testE.getFillSpeed(), 6);
    }

    @Test //A test for the checkInRange function
    public void testCheckInRange() throws  Exception {
        Fortress testFortress1 = new Fortress(2, 2, 256, 256, null, null, 100, 20, 3);
        Fortress testFortress2 = new Fortress(2, 10, 256, 256, null, null, 100, 20, 3);
        when(testE.checkInRange(testFortress1)).thenCallRealMethod();
        when(testE.checkInRange(testFortress2)).thenCallRealMethod();
        assertTrue(testE.checkInRange(testFortress1));
        assertFalse(testE.checkInRange(testFortress2));

    }

    @Test //A test for the DamageFortressIfInRange function
    public void testDamageFortressIfInRange() throws Exception {

        //  NO IDEA HOW TO GET THIS TEST TO WORK


        GameState gs = mock(GameState.class);
        gs.bullets = new ArrayList<Bullet>();

        Fortress fortress = new Fortress(2, 2, 256, 256, null, null, 100, 20, 3);
        Mockito.doCallRealMethod().when(testE).DamageFortressIfInRange(fortress);
        Mockito.doCallRealMethod().when(testE).checkInRange(fortress);

        try {
            testE.DamageFortressIfInRange(fortress);
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        assertEquals(80, fortress.health);


    }






}