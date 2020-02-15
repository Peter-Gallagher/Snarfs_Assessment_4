package com.septagon.entites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TiledGameMapTest {

    TiledGameMap mockTGM;



    @BeforeEach // setup
    public void setUp() {

        mockTGM = mock(TiledGameMap.class);
        mockTGM.tiledMap = mock(TiledMap.class);
        mockTGM.tiledMapRenderer = mock(OrthogonalTiledMapRenderer.class);


    }

    @Test //test constructor
    public void testTileGameMap() {
        TiledGameMap testTGM = new TiledGameMap("MinigameBackground.tmx");

        assertNotNull(testTGM.tiledMap);
        assertNotNull(testTGM.tiledMapRenderer);



    }

    @Test //test render
    public void testRender() {
        Mockito.doCallRealMethod().when(mockTGM).render(null);
        Mockito.doNothing().when(mockTGM.tiledMapRenderer).setView(null);
        Mockito.doNothing().when(mockTGM.tiledMapRenderer).render();

        mockTGM.render(null);

    }

    @Test // test dispose
    public void testDispose() {

        Mockito.doCallRealMethod().when(mockTGM).dispose();
        mockTGM.dispose();

        assertNull(mockTGM.tiledMap.getTileSets());



    }







}
