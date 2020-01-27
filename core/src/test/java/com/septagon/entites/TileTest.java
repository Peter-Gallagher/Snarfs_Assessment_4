package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * A class used to test the Tile class
 */

class TileTest {

    Tile testT = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        testT = new Tile(2, 2, null, false);
    }

    @Test //A test for the Tile class initialisation
    public void testTile() throws Exception {
        assertEquals(testT.x, 64);
        assertEquals(testT.y, 64);
        assertEquals(testT.col, 2);
        assertEquals(testT.row, 2);
        assertEquals(testT.width, 32);
        assertEquals(testT.height, 32);
        assertEquals(testT.texture, null);
        assertFalse(testT.isOccupied());
    }

    @Test //A test for the Tile class' setMovable method
    public void testSetMovable() throws Exception {
        testT.setMovable(true);
        assertTrue(testT.isMovable());
    }

    @Test //A test for the Tile class' setOccupied method
    public void testSetOccupied() throws Exception {
        testT.setOccupied(true);
        assertTrue(testT.isOccupied());
    }

    @Test //A test for the Tile class' isMovable method
    public void testIsMovable() throws Exception {
        assertFalse(testT.isMovable());
    }

    @Test //A test for the Tile class' isOccupied method
    public void testIsOccupied() throws Exception {
        assertFalse(testT.isOccupied());
    }

}