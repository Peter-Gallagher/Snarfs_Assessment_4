package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.septagon.helperClasses.AssetManager;
import com.septagon.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

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
        //testE = new Engine(1,1, null, 10, 2, 4, 2, 20, 5);
        testE = mock(Engine.class);
        testE.col = 1;
        testE.row = 1;
        testE.health = 10;
        testE.damage = 2;
        testE.range = 4;
        testE.speed = 2;
        testE.maxVolume = 20;
        testE.id = 5;
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
        assertEquals(testE.volume, 20);
        assertEquals(testE.id, 5);

    }

    @Test //A test for the Engine class' ifInRangeFill method
    public void testIfInRangeFill() throws Exception {

        Station testS = new Station(0, 2, 256, 128, null, 3);


        Mockito.doCallRealMethod().when(testE).setVolume(10);
        Mockito.doCallRealMethod().when(testE).getVolume();
        Mockito.doCallRealMethod().when(testE).setRangeCorners();
        Mockito.doCallRealMethod().when(testE).ifInRangeFill(testS);





        testE.setVolume(10);
        testE.setRangeCorners();
        assertTrue(testE.ifInRangeFill(testS));
        assertEquals(testE.getVolume(), 20);
    }

    @Test //A test for the Engine class' getMaxVolume method
    public void testGetMaxVolume() throws Exception {
        when(testE.getMaxVolume()).thenCallRealMethod();
        assertEquals(testE.getMaxVolume(), 20);
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
    public void testID() throws Exception {
        when(testE.getID()).thenCallRealMethod();
        assertEquals(testE.getID(), 5);
    }

    @Test //A test for the Engine class' fire method
    public void testFire() throws Exception {
        when(testE.getVolume()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).loseWater();

        testE.loseWater();
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
    public void testSetID() throws Exception {
        when(testE.getID()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(testE).setID(6);

        testE.setID(6);
        assertEquals(testE.getID(), 6);
    }

    @Test //A test for the checkInRange function
    public void testCheckInRange() throws  Exception {
        //Texture test = AssetManager.getDefeatedStationTexture();
        Fortress testFortress1 = new Fortress(2, 2, 256, 256, null, null, 100, 20, 3);
        Fortress testFortress2 = new Fortress(2, 10, 256, 256, null, null, 100, 20, 3);
        when(testE.inRange(testFortress1)).thenCallRealMethod();
        when(testE.inRange(testFortress2)).thenCallRealMethod();
        assertTrue(testE.inRange(testFortress1));
        assertFalse(testE.inRange(testFortress2));

    }

    @Test //A test for the DamageFortressIfInRange function
    public void testDamageFortressIfInRange() throws Exception {

        //  NO IDEA HOW TO GET THIS TEST TO WORK


        GameState gs = mock(GameState.class);
        GameState.bullets = new ArrayList<Bullet>();

        Fortress fortress = new Fortress(2, 2, 256, 256, null, null, 100, 20, 3);
        //Mockito.doCallRealMethod().when(testE).damageEnemyIfInRange(fortress);
        Mockito.doCallRealMethod().when(testE).inRange(fortress);
        Mockito.doCallRealMethod().when(testE).shoot(fortress,false);

        try {
            testE.damageIfInRange(null,true);
        } catch (Exception e) {


        }

        try {
            testE.damageIfInRange(fortress,true);
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        assertEquals(98, fortress.health);

    }

    @Test
    public void testCanSaveAndLoad(){
        Json json = new Json();
        Engine expected = new Engine(15,23,null,62,86,10,5,150,1);
        expected.takeDamage(5);
        expected.setVolume(125);
        expected.setMoved(true);
        String jsonEngine = json.toJson(expected);
        Engine loadedEngine = json.fromJson(Engine.class, jsonEngine);
        //Make sure all fields are equivalent
        assertTrue(new ReflectionEquals(expected, "").matches(loadedEngine));
    }




}