package com.septagon.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.util.ArrayList;

import  static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/*
 * A class used to test the Attacker class
 * Uses the concrete version of the Attacker class to enable testing
 */

class AttackerTest {

    @Mock
    private Attacker mockedAttacker;


    @BeforeEach //A set up function for the tests`
    public void beforeMethod() {
        //mockedAttacker = new ConcreteAttacker(1, 1, 32, 32, null, 10, 2, 4);
        mockedAttacker = mock(Attacker.class);

        mockedAttacker.col = 1;
        mockedAttacker.row = 1;
        mockedAttacker.x = 32;
        mockedAttacker.y = 32;
        mockedAttacker.width = 32;
        mockedAttacker.height = 32;
        mockedAttacker.texture = null;
        mockedAttacker.health = 10;
        mockedAttacker.maxHealth = 10;
        mockedAttacker.damage = 2;
        mockedAttacker.range = 4;
        mockedAttacker.rangeCorners = new ArrayList<Integer>();

    }

    @Test //A test for the Attacker class initialisation
    public void testAttacker() throws Exception {
        assertEquals(mockedAttacker.col, 1);
        assertEquals(mockedAttacker.row, 1);
        assertEquals(mockedAttacker.x, 32);
        assertEquals(mockedAttacker.y, 32);
        assertEquals(mockedAttacker.width, 32);
        assertEquals(mockedAttacker.height, 32);
        assertNull(mockedAttacker.texture);
        assertEquals(mockedAttacker.health, 10);
        assertEquals(mockedAttacker.health, 10);
        assertEquals(mockedAttacker.damage, 2);
        assertEquals(mockedAttacker.range, 4);
    }

    @Test //test for update
    public void testUpdate(){
        Mockito.doCallRealMethod().when(mockedAttacker).update();

        mockedAttacker.health = -1;
        mockedAttacker.update();
        assertEquals(0, mockedAttacker.health);

        mockedAttacker.health = 5;
        mockedAttacker.update();
        assertEquals(5, mockedAttacker.health);


    }

    @Test //test for initialise
    public void testInitialise(){
        Mockito.doCallRealMethod().when(mockedAttacker).initialise();
        mockedAttacker.initialise();

    }



    @Test //A test for the Attacker class' damageFortressIfInRange method
    public void testCheckForOverlap() throws Exception {

        Mockito.doCallRealMethod().when(mockedAttacker).setRangeCorners();


        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        //Texture testTexture2 = new Texture(Gdx.files.internal("images/FortressMinister.png"));
        Fortress testF1 = new Fortress(2, 2, 256, 256, null, null,100, 20, 3);
        Fortress testF2 = new Fortress(10, 10, 256, 256, null, null, 100, 20, 3);
        mockedAttacker.setRangeCorners();



    }

    @Test //A test for the Attacker class' getHealth method
    public void testGetHealth() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).getHealth();
        assertEquals(mockedAttacker.getHealth(), 10);
    }

    @Test //A test for the Attacker class' takeDamage method
    public void testTakeDamage() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).takeDamage(6);
        Mockito.doCallRealMethod().when(mockedAttacker).takeDamage(4);
        Mockito.doCallRealMethod().when(mockedAttacker).setDead();
        mockedAttacker.takeDamage(6);
        assertEquals(mockedAttacker.health, 4);

        mockedAttacker.takeDamage(4);
        assertEquals( 0 , mockedAttacker.health);


    }

    @Test //A test for the Attacker class' getDamage method
    public void testGetDamage() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).getDamage();
        assertEquals(mockedAttacker.getDamage(), 2);
    }

    @Test //A test for the Attacker class' setDamage method
    public void testSetDamage() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).setDamage(3);
        mockedAttacker.setDamage(3);
        assertEquals(mockedAttacker.damage, 3);
    }

    @Test //A test for the Attacker class' getRange method
    public void testGetRange() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).getRange();
        assertEquals(mockedAttacker.getRange(), 4);
    }

    @Test //A test for the Attacker class' setRange method
    public void testSetRange() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).setRange(3);
        mockedAttacker.setRange(3);
        assertEquals(mockedAttacker.range, 3);
    }

    @Test //A test for the Attacker class' getRangeCorners method
    public void testGetRangeCorners() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).setRangeCorners();
        mockedAttacker.setRangeCorners();
        assertNotNull(mockedAttacker.getRangeCorners());
    }

    @Test //A test for the Attacker class' setMaxHealth method
    public void testSetMaxHealth() throws Exception {

        Mockito.doCallRealMethod().when(mockedAttacker).setMaxHealth(15);
        mockedAttacker.setMaxHealth(15);
        assertEquals(mockedAttacker.maxHealth, 15);
    }

    @Test //A test for the Attacker class' setHealth method
    public void testSetHealth() throws Exception {

        Mockito.doCallRealMethod().when(mockedAttacker).setHealth(20);
        mockedAttacker.setHealth(20);
        assertEquals(mockedAttacker.health, 20);
    }

    @Test //A test for the Attacker class' getMaxHealth method
    public void testGetMaxHealth() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).getMaxHealth();
        assertEquals(mockedAttacker.getMaxHealth(), 10);
    }

    @Test //A test for the setRangeCorners method
    public void testSetRangeCorners() throws Exception {
        Mockito.doCallRealMethod().when(mockedAttacker).setRangeCorners();
        Mockito.doCallRealMethod().when(mockedAttacker).getRangeCorners();

        mockedAttacker.setRangeCorners();
        ArrayList<Integer> testCorners = mockedAttacker.getRangeCorners();
        assertEquals(testCorners.get(0), -3);
        assertEquals(testCorners.get(1), 6);
        assertEquals(testCorners.get(2), -3);
        assertEquals(testCorners.get(3), 6);




    }

}

