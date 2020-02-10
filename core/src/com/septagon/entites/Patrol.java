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
        this.path = path;
    }

    //Checks whether a fireEngine is within its range. Return true if it is, return false if it isnt
    private boolean inRange(Engine fireEngine) {
        int engineCol = fireEngine.getCol();
        int engineRow = fireEngine.getRow();

        int xDisplacement = Math.min(Math.abs(this.getCol() - engineCol), Math.abs((this.getCol() + (this.width / 32)) - engineCol));
        int yDisplacement = Math.min(Math.abs(this.getRow() - engineRow), Math.abs((this.getRow() + (this.height / 32)) - engineRow));

        if (Math.sqrt((xDisplacement * xDisplacement) + (yDisplacement * yDisplacement)) <= this.range) {
            return true;
        } else {
            return false;
        }
    }

    //TODO implement shooting mechanism
    private void shoot(Engine fireEngine){
        int numBullets = 25;

        int engineX = fireEngine.getX();
        int engineY = fireEngine.getY();

        int centreX = this.x + (this.width/2);
        int centreY = this.y + (this.height/2);

        //Actual functionality
        if(inRange(Engine fireEngine)){
            fireEngine.takeDamage(this.damage);

            for (int i = 0; i< numBullets; i++){
                GameState.bullets.add(new Bullet(centreX + (i * 2), centreY + (i*3),  engineX + 10, engineY, false));
            }
        }
    }

    //TODO implement movement mechanism
    private void move(){
        }
    }

