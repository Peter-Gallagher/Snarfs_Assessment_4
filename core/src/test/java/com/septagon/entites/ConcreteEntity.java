package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;

/*
 * Concrete class for testing of the Entity class only
 * Not for use besides testing
 */

public class ConcreteEntity extends Entity{

    public ConcreteEntity(int col, int row, int width, int height, Texture texture)
    {
        super(col, row, width, height, texture);
    }
}
