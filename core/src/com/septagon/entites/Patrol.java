package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

import java.util.ArrayList;

public class Patrol extends Vehicle  {

    //Variable to keep track of the patrol's route
    private char direction;

    //path owned by any one patrol
    private ArrayList<Tile> path;

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, char direction, ArrayList<Tile> path){
        super(col, row, texture, health, damage, range, speed);
        this.direction = direction;
        this.path = path;
    }


    //Function to attack Fire Engine, same as fortress. Refactor into same class at some point?
    public void DamageEngineIfInRange(Engine fireEngine) {

        int engineCol = fireEngine.getCol();
        int engineRow = fireEngine.getRow();

        int xDisplacement = Math.min(Math.abs(this.getCol() - engineCol), Math.abs((this.getCol() + (this.width / 32)) - engineCol));
        int yDisplacement = Math.min(Math.abs(this.getRow() - engineRow), Math.abs((this.getRow() + (this.height / 32)) - engineRow));

        int engineX = fireEngine.getX();
        int engineY = fireEngine.getY();

        int centreX = this.x + (this.width/2);
        int centreY = this.y + (this.height/2);;

        //define the number of projectiles are drawn when an entity is shot
        int numBullets = 25;

        //Actual functionality
        if(Math.sqrt( (xDisplacement * xDisplacement) + (yDisplacement * yDisplacement) ) <= this.range){
            fireEngine.takeDamage(this.damage);

            for (int i = 0; i< numBullets; i++){
                GameState.bullets.add(new Bullet(centreX + (i * 2), centreY + (i*3),  engineX + 10, engineY, false));
            }
        }
    }
}
