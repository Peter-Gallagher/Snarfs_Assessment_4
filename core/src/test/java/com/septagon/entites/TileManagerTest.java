package com.septagon.entites;

import com.septagon.helperClasses.AttackerManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TileManagerTest {

    TileManager testTM;


    @BeforeEach //set up function
    public void beforeMethod() {

        testTM = new TileManager(new ArrayList<Engine>(), new ArrayList<Tile>());


    }


    @Test // test for resetMovableTiles
    public void testResetMovable() {
        Tile testTile1 = mock(Tile.class);
        Tile testTile2 = mock(Tile.class);

        testTile1.movable = true;
        testTile2.movable = false;

        Mockito.doCallRealMethod().when(testTile1).setMovable(false);
        Mockito.doCallRealMethod().when(testTile2).setMovable(false);

        testTM.getTiles().add(testTile1);
        testTM.getTiles().add(testTile2);
        testTM.resetMovableTiles();

        assertFalse(testTile1.movable);
        assertFalse(testTile2.movable);


    }

    @Test // test for setEngineTilesOccupied
    public void testSetEngineTilesOccupied() {
        Tile testTile1 = mock(Tile.class);
        Engine testEngine = mock(Engine.class);

        //tile params
        testTile1.col = 1;
        testTile1.row = 2;

        Mockito.doCallRealMethod().when(testTile1).getRow();
        Mockito.doCallRealMethod().when(testTile1).getCol();
        Mockito.doCallRealMethod().when(testTile1).setOccupied(true);


        //engine params
        testEngine.col = 1;
        testEngine.row = 1;

        Mockito.doCallRealMethod().when(testEngine).getRow();
        Mockito.doCallRealMethod().when(testEngine).getCol();


        testTM.getTiles().add(testTile1);
        testTM.getEngines().add(testEngine);

        //test 1

        testTM.setEngineTilesOccupied();
        assertFalse(testTile1.occupied);



        //test 2
        testTile1.row = 1;
        testTM.setEngineTilesOccupied();
        assertTrue(testTile1.occupied);

    }


    @Test //test for updateTileInAdjacencyList
    public void testUpdateTileInAdjacencyList(){

        testTM.setAdjacencyList(new int[100][4]);



    }

    @Test //test for getTileAtLocation
    public void testGetTileAtLocation() {



        Tile testTile1 = mock(Tile.class);
        for(int i=0 ; i<10; i++) {
            testTM.getTiles().add(testTile1);
        }

        assertEquals(testTile1, testTM.getTileAtLocation(1,1));
        assertEquals(testTile1, testTM.getTileAtLocation(4,1));
        assertEquals(null, testTM.getTileAtLocation(20,20));

    }


    @Test //test for bfs
    public void testBFS() {



    }






}
