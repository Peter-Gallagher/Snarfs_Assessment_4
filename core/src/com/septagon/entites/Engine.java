package com.septagon.entites;

/**
 * Class that inherits from vehicle that will define all the fire engines
 * in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

public class Engine extends Vehicle
{
    //Member variables that will be unique stats of each engine
    protected int volume;
    protected int maxVolume;
    protected int fillSpeed;
    protected Integer id;

    //Keeps track of whether the engine has moved on the current player turn
    private boolean moved = false;

    /***
     * Constructor that Sets up the member variables for engine
     */
    public Engine(int col, int row, Texture texture, int health, int damage, int range, int speed, int maxVolume, int fillSpeed, Integer id) {
        super(col, row, texture, health, damage, range, speed);
        this.volume = maxVolume;
        this.maxVolume = maxVolume;
        this.fillSpeed = fillSpeed;
        this.id = id;
    }


    /***
     * Checks if the engine is in range to fill and calls the fill method if it is
     * @param station The Fire Station
     */
    public boolean ifInRangeFill(Station station){
        //TODO: make a circle

        int xRange =station.getCol() + station.getWidth()/Tile.TILE_SIZE;
        int stationRow = station.getRow();

        if(this.col <= xRange && this.col > station.getCol() && this.row >= stationRow-5 && this.row <= stationRow-1){
            if(this.volume < this.maxVolume || this.health < this.maxHealth){

                this.volume = this.maxVolume;
                this.health = this.maxHealth;
                return true;
            }
        }

        return false;
    }

    /**
     * Calls to update the required variables when the engine fires at a fortress
     */
    public void loseWater(){
        this.volume = Math.max(this.volume - this.damage, 0) ;
    }



    /***
     * Checks if any of the corners of the engines range are in the body of the fortress or station
     * @param  attacker that is being checked
     * @return returns true if there is any overlap, false otherwise
     */

    public void engineBullets(Attacker attacker) {
        int attackerX = attacker.getX();
        int attackerY = attacker.getY();
        int numBullets = 20;

        for (int i = 0; i < numBullets; i++) {
            GameState.bullets.add(new Bullet(this.x + (i * 2), this.y + (i * 3), attackerX + 100, attackerY + 70, true));
        }
    }

    //TODO num bullets want to be a local?
    /***
     * Method that will check if the Attacker is in range of the fortress and if so will damage it
     * @param attacker The fortress we are currently checking the bounds/range of
     */
    public boolean damageEnemyIfInRange(Attacker attacker){
        //this.setRangeCorners();
        boolean attackMade = false;

        if(inRange(attacker)){
            if (this.volume >= this.damage){
                this.loseWater();
                this.shoot(attacker);
                attackMade = true;

                engineBullets(attacker);
            }
        }
        return attackMade;
    }





    //Getters and Setters
    public int getMaxVolume()
    {
        return this.maxVolume;
    }
    public int getVolume() { return this.volume; }
    public int getFillSpeed() { return fillSpeed; }

    public Integer getID()
    {
        return this.id;
    }

    public boolean isMoved(){return this.moved;}

    public void setMoved(boolean moved){this.moved = moved;}
    public void setVolume(int volume) { this.volume = volume; }
    public void setFillSpeed(int fillSpeed) { this.fillSpeed = fillSpeed; }




}