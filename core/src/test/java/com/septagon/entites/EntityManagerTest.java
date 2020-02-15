package com.septagon.entites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class EntityManagerTest {

    EntityManager mockEM;


    @BeforeEach //setup the EntityManager mock
    public void setUp() {
        mockEM = mock(EntityManager.class);

        Mockito.doCallRealMethod().when(mockEM).setEntities(new ArrayList<Entity>());

        mockEM.setEntities(new ArrayList<Entity>());


    }


    @Test //test for constructor
    public void testConstructor() {
        EntityManager testEM = new EntityManager();
        assertEquals(0, testEM.getEntities().size());
    }

    @Test //test for add entity
    public void testAddEntity() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).getEntities();
        mockEM.getEntities().add(testE);
        assertEquals(testE, mockEM.getEntities().get(0));


    }

    @Test //test initialise
    public void testInitialise() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).initialise();
        Mockito.doCallRealMethod().when(testE).initialise();

        mockEM.addEntity(testE);
        mockEM.initialise();

    }


    @Test //test update
    public void testUpdate() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).update();
        Mockito.doCallRealMethod().when(testE).update();

        mockEM.addEntity(testE);
        mockEM.update();


    }

    @Test //test render
    public void testRender() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).render(null);
        Mockito.doNothing().when(testE).render(any(SpriteBatch.class));

        mockEM.addEntity(testE);
        mockEM.render(null);



    }

    @Test //test getEntities
    public void testGetEntites() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).getEntities();
        ArrayList<Entity> testData = new ArrayList<Entity>(Arrays.asList(testE));

        mockEM.addEntity(testE);

        assertEquals(testData, mockEM.getEntities());

    }

    @Test //test remove
    public void testRemove() {

        Entity testE = mock(Entity.class);
        Mockito.doCallRealMethod().when(mockEM).addEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).removeEntity(testE);
        Mockito.doCallRealMethod().when(mockEM).getEntities();
        mockEM.getEntities().add(testE);
        mockEM.removeEntity(testE);

        assertEquals(0, mockEM.getEntities().size());


    }



}
