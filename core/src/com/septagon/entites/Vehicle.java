package com.septagon.entites;

/**
 * Class that will be used to define all vehicles within the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;

public class Vehicle extends Attacker
{
    protected int speed;


    public Vehicle (int col, int row, String textureId, int health, int damage, int range, int speed)
    {
        super(col,row, Tile.TILE_SIZE, Tile.TILE_SIZE, textureId, health, damage, range);
        this.speed = speed;
    }

    //Getters
    public int getSpeed() { return speed; }

    public void setSpeed(int speed) { this.speed = speed; }
}
