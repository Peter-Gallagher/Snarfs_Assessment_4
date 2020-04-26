package com.septagon.entites;

/**
 * Class that will be used to define the fire stations in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;

public class Station extends Entity
{
    private int fillRange;

    /***
     *Constructor that calls Entity constructor that is used to set up all member variables
     */
    /*This changed*/
    public Station(int col, int row, int width, int height, String textureId, int fillRange)
    {
        super(col,row,width,height,textureId);
        this.fillRange = fillRange;
    }

    public int getFillRange(){
        return this.fillRange;
    }

}