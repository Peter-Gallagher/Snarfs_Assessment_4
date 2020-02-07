package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

//TODO Add patrol, extend from vehicle?
public class Patrol extends Vehicle  {

    //Variable to keep track of the patrol's route
    private char direction;

    //Variable to help keep track of each patrol
    private Integer id;

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, char direction, Integer id){
        super(col, row, texture, health, damage, range, speed);
        this.direction = direction;
        this.id = id;
    }

    //Placeholder copy paste from the fortress class. Same functionality.
    public void DamageEngineIfInRange(Engine fireEngine) {

        int engineCol = fireEngine.getCol();
        int engineRow = fireEngine.getRow();

        int xDisplacement = Math.min(Math.abs(this.getCol() - engineCol), Math.abs((this.getCol() + (this.width / 32)) - engineCol));
        int yDisplacement = Math.min(Math.abs(this.getRow() - engineRow), Math.abs((this.getRow() + (this.height / 32)) - engineRow));

        //define the number of projectiles are drawn when an entity is shot
        int numBullets = 25;

        if(Math.sqrt( (xDisplacement * xDisplacement) + (yDisplacement * yDisplacement) ) <= this.range){
            fireEngine.takeDamage(this.damage);

            int engineX = fireEngine.getX();
            int engineY = fireEngine.getY();

            int centreX = this.x + (this.width/2);
            int centreY = this.y + (this.height/2);;

            for (int i = 0; i< numBullets; i++){
                GameState.bullets.add(new Bullet(centreX + (i * 2), centreY + (i*3),  engineX + 10, engineY, false));
            }
        }
    }
}
