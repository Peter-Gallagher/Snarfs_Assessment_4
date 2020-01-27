package com.septagon.entites;
/**
 * Abstract class that provides features that extend upon entities features
 * for all entities in the game that will be able to attack other entities
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

import java.util.ArrayList;
import java.util.List;

public abstract class Attacker extends Entity
{
    //Variables that will store different attributes for each Attacker - unique for each instance
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int range;

    /***
     * Constructor that sets up values based on inputs (also refers to Entity constructor)
     * @param col The map column of the Attacker
     * @param row The map row of the Attacker
     * @param texture The texture of the Attacker
     * @param health The health of the Attacker
     * @param damage The damage dealt by the Attacker
     * @param range The range of attacks for the Attacker
     */
    public Attacker(int col, int row, int width, int height, Texture texture, int health, int damage, int range)
    {
        super(col,row,width,height,texture);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.range = range;
    }

    protected ArrayList<Integer> rangeCorners = new ArrayList<Integer>();

    @Override
    public void initialise() {
        super.initialise();
        setRangeCorners();
    }

    /***
     * Overriding of the update method, used to make sure an attackers health never passes below 0
     */
    public void update(){
        super.update();
        if(health <= 0) health = 0;
    }

    /**
     * Set up the range corner values for each attacker which are used for detecting if an engine can be fired at
     */
    protected void setRangeCorners() {
        //Makes an arrayList of the boundaries of the 2 x values and 2 y values at the corner
        rangeCorners.clear();
        Integer leftX = this.col - this.range;
        Integer rightX = this.col + this.width/Tile.TILE_SIZE + this.range;
        Integer bottomY = this.row - this.range;
        Integer topY = this.row + this.height/Tile.TILE_SIZE + this.range;
        this.rangeCorners.add(leftX);
        this.rangeCorners.add(rightX);
        this.rangeCorners.add(bottomY);
        this.rangeCorners.add(topY);
    }

    //Getters
    public int getHealth() {
        return health;
    }
    public int getDamage() {
        return damage;
    }
    public int getRange() {
        return range;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public ArrayList getRangeCorners(){
        return rangeCorners;
    }

    //Setters
    public void takeDamage(int damage) {
        if(this.health - damage > 0){
            this.health -= damage;
        }else{
            this.health = 0;
            this.setDead();
        }

    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }

    public void setHealth(int health){
        this.health = health;
    }
}