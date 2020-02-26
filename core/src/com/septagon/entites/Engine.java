package com.septagon.entites;

/**
 * Class that inherits from vehicle that will define all the fire engines
 * in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.TileManager;
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

    //Values used to track and control powerups
    protected boolean poweredUp = false;
    protected int powerupType = 0;
    protected int turnsPowered = 0;
    private int baseHealth, baseDamage, baseRange, baseSpeed;
    private boolean invulnerable = false;

    /***
     * Constructor that Sets up the member variables for engine
     */
    public Engine(int col, int row, Texture texture, int health, int damage, int range, int speed, int maxVolume, int fillSpeed, Integer id) {
        super(col, row, texture, health, damage, range, speed);
        this.volume = maxVolume;
        this.maxVolume = maxVolume;
        this.fillSpeed = fillSpeed;
        this.id = id;
        this.baseHealth = health;
        this.baseDamage = damage;
        this.baseRange = range;
        this.baseSpeed = speed;
    }


    /***
     * Checks if the engine is in range to fill and calls the fill method if it is
     * @param station The Fire Station
     */
    /*This changed*/
    public boolean ifInRangeFill(Station station){

        int xDisplacement = Math.min(Math.abs(station.getCol() - this.getCol()), Math.abs((station.getCol() + (station.width / 32)) - this.getCol()));
        int yDisplacement = Math.min(Math.abs(station.getRow() - this.getRow()), Math.abs((station.getRow() + (station.height / 32)) - this.getRow()));

        if (Math.sqrt((xDisplacement * xDisplacement) + (yDisplacement * yDisplacement)) <= station.getFillRange()){
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
    /*This changed*/
    public void loseWater(){
        this.volume = Math.max(this.volume - this.damage, 0) ;
    }

    @Override
    /*This is new*/
    public boolean damageIfInRange(Attacker attacker, boolean useWater){

        if(this.inRange(attacker)) {
            if (this.volume >= this.damage) {
                shoot(attacker, useWater);
                loseWater();
                return true;
            }
        }

        return false;
    }

    /**
     * Method used to powerup or debuff fire engines
     * New for Assessment 4
     * @param tog true if toggling on, false if toggling off
     */
    private void powerupToggle(boolean tog){
        if(tog) {
            switch (powerupType) {
                case (1): //Instant heal / refill
                    health = maxHealth;
                    volume = maxVolume;
                    poweredUp = false;
                    break;
                case (2): //Damage
                    damage = (int) Math.ceil(baseDamage * 1.5);
                    break;
                case (3): //Attack Range
                    range = (int) Math.ceil(baseRange * 1.2);
                    break;
                case (4): //Move Range
                    speed = (int) Math.ceil(baseSpeed * 1.2);
                    break;
                case (5): //Temporary Invulnerability
                    invulnerable = true;
                    break;
            }
        } else {
            damage = baseDamage;
            range = baseRange;
            speed = baseSpeed;
            invulnerable = false;
        }
    }

    /**
     * Method used to control powerup logic for fire engines
     * New for Assessment 4
     */
    public void updatePowerup(){
        if(poweredUp){
            if(turnsPowered == 0){
                powerupToggle(true);    //toggles on
            } else if (turnsPowered > 4){
                poweredUp = false;
                turnsPowered = 0;   //resets associated values
                powerupType = 0;
                powerupToggle(false);   //toggles off
                return;
            }
            turnsPowered++;
        }
    }

    /**
     * Checks if Fire Engine is invulnerable from powerups, if it isn't
     * then it takes damage
     * New for Assessment 4
     * @param damage the amount of damage to be taken
     */
    @Override
    public void takeDamage(int damage) {
        if (!invulnerable){
            this.health = Math.max(this.health - damage, 0);

            if (this.health <= 0) {
                this.setDead();
            }
        }
    }




    //Getters and Setters
    public int getMaxVolume() { return this.maxVolume; }
    public int getVolume() { return this.volume; }
    public int getFillSpeed() { return fillSpeed; }
    public Integer getID() { return this.id; }
    public boolean isMoved(){return this.moved;}

    public void setMoved(boolean moved){this.moved = moved;}
    public void setVolume(int volume) { this.volume = volume; }
    public void setFillSpeed(int fillSpeed) { this.fillSpeed = fillSpeed; }




}