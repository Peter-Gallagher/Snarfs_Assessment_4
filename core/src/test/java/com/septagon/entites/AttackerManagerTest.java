package com.septagon.entites;

import com.septagon.helperClasses.AttackerManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttackerManagerTest {

    AttackerManager testAManager;
    GameState gs;



    @BeforeEach //set up function
    public void beforeMethod() {

        gs = mock(GameState.class);
        testAManager = new AttackerManager(new ArrayList<Engine>(),new ArrayList<Tile>(), new ArrayList<Patrol>(), new ArrayList<Fortress>(), gs);

    }



    @Test //testing allEnginesMoved
    public void testAllEnginesMoves() {
        Engine testMovedEngine = mock(Engine.class);
        Engine testNotMovedEngine = mock(Engine.class);

        when(testMovedEngine.isMoved()).thenReturn(true);
        when(testNotMovedEngine.isMoved()).thenReturn(false);

        //test when empty
        assertTrue(testAManager.allEnginesMoved());

        testAManager.getEngines().add(testNotMovedEngine);

        //test with one non moved
        assertFalse(testAManager.allEnginesMoved());

        testAManager.getEngines().clear();
        testAManager.getEngines().add(testMovedEngine);
        testAManager.getEngines().add(testMovedEngine);

        //test with team of moved
        assertTrue(testAManager.allEnginesMoved());


    }

    @Test //test checkIfTouchingFortress
    public void testCheckIfTouchingFortress()
    {
        Fortress testFortress1 = mock(Fortress.class);
        testFortress1.x = 1;
        testFortress1.y = 1;
        testFortress1.width = 32;
        testFortress1.height = 32;
        testFortress1.selected = false;

        Mockito.doCallRealMethod().when(testFortress1).getX();
        Mockito.doCallRealMethod().when(testFortress1).getY();
        Mockito.doCallRealMethod().when(testFortress1).getWidth();
        Mockito.doCallRealMethod().when(testFortress1).getHeight();
        Mockito.doCallRealMethod().when(testFortress1).setSelected(true);
        Mockito.doCallRealMethod().when(testFortress1).setSelected(false);
        Mockito.doCallRealMethod().when(testFortress1).isSelected();

        Fortress testFortress2 = mock(Fortress.class);
        testFortress2.x = 1;
        testFortress2.y = 1;
        testFortress2.width = 64;
        testFortress2.height = 64;
        testFortress2.selected = false;

        Mockito.doCallRealMethod().when(testFortress2).getX();
        Mockito.doCallRealMethod().when(testFortress2).getY();
        Mockito.doCallRealMethod().when(testFortress2).getWidth();
        Mockito.doCallRealMethod().when(testFortress2).getHeight();
        Mockito.doCallRealMethod().when(testFortress2).setSelected(true);
        Mockito.doCallRealMethod().when(testFortress2).setSelected(false);
        Mockito.doCallRealMethod().when(testFortress2).isSelected();

        testAManager.getFortresses().add(testFortress1);
        testAManager.getFortresses().add(testFortress2);

        testAManager.checkIfTouchingFortress(40,40);
        assertFalse(testFortress1.selected);
        assertTrue(testFortress2.selected);

        testAManager.checkIfTouchingFortress(100,100);
        assertFalse(testFortress1.selected);
        assertFalse(testFortress2.selected);

        testAManager.checkIfTouchingFortress(10,10);
        assertTrue(testFortress1.selected);
        assertTrue(testFortress2.selected);




    }



    @Test //test for checkIfAllEnginesDead
    public void testCheckIfAllEnginesDead() {

        Engine testEngine = mock(Engine.class);
        Engine testDeadEngine = mock(Engine.class);

        when(testEngine.isDead()).thenReturn(true);
        when(testDeadEngine.isDead()).thenReturn(false);

        //test when no engines
        assertTrue(testAManager.checkIfAllEnginesDead());

        testAManager.getEngines().add(testEngine);

        //test with one dead
        assertTrue(testAManager.checkIfAllEnginesDead());

        testAManager.getEngines().clear();
        testAManager.getEngines().add(testEngine);
        testAManager.getEngines().add(testDeadEngine);

        //test with mixed team
        assertFalse(testAManager.checkIfAllEnginesDead());

    }

    @Test //test for getTiledClicked
    public void testGetTileClicked() {


        Tile testTile = mock(Tile.class);
        testTile.x = 1;
        testTile.y = 1;
        testTile.width = 32;
        testTile.height = 32;

        Mockito.doCallRealMethod().when(testTile).checkIfClickedInside(10,10);
        Mockito.doCallRealMethod().when(testTile).checkIfClickedInside(48,48);
        Mockito.doCallRealMethod().when(testTile).checkIfClickedInside(100,100);

        Tile testTile2 = mock(Tile.class);
        testTile2.x = 1;
        testTile2.y = 1;
        testTile2.width = 64;
        testTile2.height = 64;

        Mockito.doCallRealMethod().when(testTile2).checkIfClickedInside(10,10);
        Mockito.doCallRealMethod().when(testTile2).checkIfClickedInside(48,48);
        Mockito.doCallRealMethod().when(testTile2).checkIfClickedInside(100,100);

        //test when empty
        assertEquals(null,testAManager.getTileClicked(10,10));

        testAManager.getTiles().add(testTile);
        testAManager.getTiles().add(testTile2);


        //test with only one active
        assertEquals(testTile2,testAManager.getTileClicked(48,48));

        //test with multiple active
        assertEquals(testTile,testAManager.getTileClicked(10,10));

        //test with none active
        assertEquals(null,testAManager.getTileClicked(100,100));


    }

    @Test //test for BattleTurn
    public void testBattleTurn(){
        Engine testEngine = mock(Engine.class);
        Fortress testFortress = mock(Fortress.class);
        TileManager ts = mock(TileManager.class);


        when(gs.getTileManager()).thenReturn(ts);
        doNothing().when(ts).resetMovableTiles();

        when(testEngine.isDead()).thenReturn(true);
        when(testFortress.isDead()).thenReturn(true);

        testAManager.getEngines().add(testEngine);
        testAManager.getFortresses().add(testFortress);

        //test to see if engines are removed
        testAManager.BattleTurn(testFortress);
        assertEquals( 0, testAManager.getEngines().size());

        when(testEngine.isDead()).thenReturn(false);
        when(testFortress.isDead()).thenReturn(true);

        testAManager.getFortresses().clear();
        testAManager.getEngines().clear();
        testAManager.getEngines().add(testEngine);
        testAManager.getFortresses().add(testFortress);

        //test to see if fortresses are removed
        testAManager.BattleTurn(testFortress);
        assertEquals( 0, testAManager.getFortresses().size());


        when(testEngine.isDead()).thenReturn(false);
        when(testFortress.isDead()).thenReturn(false);

        testAManager.getFortresses().clear();
        testAManager.getEngines().clear();
        testAManager.getEngines().add(testEngine);
        testAManager.getFortresses().add(testFortress);

        //test to see nobody is removed
        testAManager.BattleTurn(testFortress);
        assertEquals( 1, testAManager.getFortresses().size());
        assertEquals( 1, testAManager.getEngines().size());

    }











}












