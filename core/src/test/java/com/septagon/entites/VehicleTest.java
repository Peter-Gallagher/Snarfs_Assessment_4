package com.septagon.entites;

/*
 * A class used to test the Vehicle class
 */

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    Vehicle testV = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        testV = new Vehicle(0,0, null, 10, 2, 4, 2);
    }

    @Test //A test for the Vehicle class initialisation
    public void testVehicle() throws Exception {
        assertEquals(testV.x, 0);
        assertEquals(testV.y, 0);
        assertEquals(testV.col, 0);
        assertEquals(testV.row, 0);
        assertEquals(testV.width, 32);
        assertEquals(testV.height, 32);
        //assertEquals(testV.texture, null);
        assertEquals(testV.health, 10);
        assertEquals(testV.damage, 2);
        assertEquals(testV.range, 4);
        assertEquals(testV.speed, 2);
    }

    @Test //A test for the Vehicle class' getSpeed method
    public void testGetSpeed() throws Exception {
        assertEquals(testV.getSpeed(), 2);
    }

    @Test //A test for the Vehicle class' getDirection method
    public void testGetDirection() throws Exception {
        testV.direction = 'U';
        assertEquals(testV.getDirection(), 'U');
    }

}
