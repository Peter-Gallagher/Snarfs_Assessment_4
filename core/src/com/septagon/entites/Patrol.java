package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

//TODO Add patrol, extend from vehicle?
public class Patrol extends Vehicle  {

    private char direction;

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, char direction){
        super(col, row, texture, health, damage, range, speed);
        this.direction = direction;
    }
}
