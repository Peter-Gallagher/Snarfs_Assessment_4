package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class BulletTest {


    Bullet testBullet = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {

        testBullet = mock(Bullet.class);
        testBullet.x = 1;
        testBullet.y = 1;
        testBullet.targetX = 10;
        testBullet.targetY = 10;
        testBullet.remove = false;
        testBullet.texture = null;
        testBullet.ySPEED = 500;
        testBullet.xSPEED = 500;
        testBullet.deltaY = testBullet.targetY - testBullet.y;
        testBullet.deltaX = testBullet.targetX - testBullet.x;

    }

    @Test //test for update
    public void testUpdate() {
        Mockito.doCallRealMethod().when(testBullet).update(0.00001f);
        testBullet.update(0.00001f);

        assertTrue(testBullet.remove);


    }

    @Test //test for render if applicable
    public void testRender() {
        Mockito.doNothing().when(testBullet).render(null);
        testBullet.render(null);


    }



}
