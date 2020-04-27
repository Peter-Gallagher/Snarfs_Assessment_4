package com.septagon.entites;

import com.badlogic.gdx.utils.Json;
import com.septagon.game.Game;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

public class PatrolTest {

    Patrol mockedPatrol;

    @BeforeEach
    public void setUp() {
        mockedPatrol = mock(Patrol.class);

        mockedPatrol.col = 1;
        mockedPatrol.row = 1;
        mockedPatrol.texture = null;
        mockedPatrol.health = 10;
        mockedPatrol.dead = false;
        mockedPatrol.damage = 2;
        mockedPatrol.range = 4;
        mockedPatrol.speed = 5;
        mockedPatrol.maxHealth = 10;
        mockedPatrol.x = 1;
        mockedPatrol.y = 1;
        mockedPatrol.width = 32;
        mockedPatrol.height = 32;

    }

    @Test //test for inRange function
    public void testInRange() {
        Engine testEngine1 = mock(Engine.class);
        testEngine1.col = 2;
        testEngine1.row = 2;
        Mockito.doCallRealMethod().when(testEngine1).getCol();
        Mockito.doCallRealMethod().when(testEngine1).getRow();

        Engine testEngine2 = mock(Engine.class);
        testEngine2.col = 100;
        testEngine2.row = 100;

        Mockito.doCallRealMethod().when(testEngine2).getCol();
        Mockito.doCallRealMethod().when(testEngine2).getRow();

        Mockito.doCallRealMethod().when(mockedPatrol).inRange(testEngine1);
        Mockito.doCallRealMethod().when(mockedPatrol).inRange(testEngine2);


        assertTrue(mockedPatrol.inRange(testEngine1));
        assertFalse(mockedPatrol.inRange(testEngine2));

    }

    @Test //test for shoot method
    public void testShoot() {

        Engine testEngine1 = mock(Engine.class);
        GameState.bullets = mock(ArrayList.class);

        Mockito.doReturn(true).when(GameState.bullets).add(any(Bullet.class));

        testEngine1.col = 200;
        testEngine1.row = 200;
        testEngine1.x = 1;
        testEngine1.y = 1;
        testEngine1.health = 10;
        Mockito.doCallRealMethod().when(testEngine1).getCol();
        Mockito.doCallRealMethod().when(testEngine1).getRow();
        Mockito.doCallRealMethod().when(testEngine1).getX();
        Mockito.doCallRealMethod().when(testEngine1).getY();
        Mockito.doCallRealMethod().when(testEngine1).takeDamage(anyInt());
        Mockito.doCallRealMethod().when(testEngine1).setDead();

        Mockito.doCallRealMethod().when(mockedPatrol).inRange(testEngine1);
        //Mockito.doCallRealMethod().when(mockedPatrol).patrolShoot(testEngine1);
        Mockito.doCallRealMethod().when(mockedPatrol).shoot(testEngine1,false);

        try {
            mockedPatrol.shoot(testEngine1,false);
        } catch (Exception e) {

        }

        assertEquals(10, testEngine1.health);

        testEngine1.col = 2;
        testEngine1.row = 2;

        try {
            mockedPatrol.shoot(testEngine1,false);
        } catch (Exception e) {

        }

        assertEquals(8, testEngine1.health);

    }

    @Test //test for getDistanceToTarget
    public void testGetDistanceToTarget() {
        Tile mockedTile = mock(Tile.class);
        Tile mockedTargetTile = mock(Tile.class);


        mockedPatrol.tileManager = new TileManager(new ArrayList<Engine>(), new ArrayList<Tile>());


        mockedTile.col = 1;
        mockedTile.row = 1;

        Mockito.doCallRealMethod().when(mockedTile).getCol();
        Mockito.doCallRealMethod().when(mockedTile).getRow();

        mockedTargetTile.col = 10;
        mockedTargetTile.row = 10;

        Mockito.doCallRealMethod().when(mockedTargetTile).getCol();
        Mockito.doCallRealMethod().when(mockedTargetTile).getRow();


        mockedPatrol.tileManager.getTiles().add(mockedTargetTile);
        ArrayList<Integer> moves = new ArrayList<Integer>(Arrays.asList(0));

        Mockito.doCallRealMethod().when(mockedPatrol).getDistanceToTarget(mockedTile, moves);
        ArrayList<Float> retVal = mockedPatrol.getDistanceToTarget(mockedTile, moves);
        ArrayList<Float> compVal = new ArrayList<Float>(Arrays.asList(12.727922f));
        assertEquals(retVal, compVal);


    }

    @Test // test for move
    public void testMove() {
        mockedPatrol.tileManager = new TileManager(new ArrayList<Engine>(), new ArrayList<Tile>());
        mockedPatrol.path = new ArrayList<Tile>();
        Tile mockedTargetTile = mock(Tile.class);

        mockedTargetTile.col = 10;
        mockedTargetTile.row = 10;

        mockedTargetTile.x = 10;
        mockedTargetTile.y = 10;

        mockedPatrol.col = 0;
        mockedPatrol.row = 0;

        Mockito.doCallRealMethod().when(mockedTargetTile).getCol();
        Mockito.doCallRealMethod().when(mockedTargetTile).getRow();
        Mockito.doCallRealMethod().when(mockedTargetTile).getX();
        Mockito.doCallRealMethod().when(mockedTargetTile).getY();
        //Mockito.doNothing().when(mockedPatrol).updatePathIndex(any(Tile.class));

        Mockito.doCallRealMethod().when(mockedPatrol).move();
        Mockito.doReturn(0).when(mockedPatrol).getTileClosestToGoal(any(ArrayList.class));
        Mockito.doReturn(new ArrayList<Integer>(Arrays.asList(0))).when(mockedPatrol).getPossibleMoveIndexes();

        mockedPatrol.tileManager.getTiles().add(mockedTargetTile);
        mockedPatrol.path.add(mockedTargetTile);

        mockedPatrol.move();

        assertEquals(mockedPatrol.x, 10);
        assertEquals(mockedPatrol.y, 10);

    }

    @Test //test for constructor
    public void testConstructor() {
        Patrol testPatrol = new Patrol(1,1,null,10,2,4,2,new ArrayList<Tile>(), null, mock(TileManager.class));
        assertEquals(0, testPatrol.pathIndex);
    }


    @Test
    public void testCanSaveAndLoad(){
        Json json = new Json();
        Patrol expected = new Patrol(45, 11, null, 65, 3, 7,10, null, "Testing", null);
        expected.takeDamage(9);
        expected.pathIndex = 3;
        String jsonPatrol = json.toJson(expected);
        Patrol actual = json.fromJson(Patrol.class, jsonPatrol);
        //Make sure all fields are equivalent
        assertTrue(new ReflectionEquals(expected, "").matches(actual));
    }


}
