package com.septagon.entites;

/**
 * Class that inherits from vehicle that will define all the fire engines
 * in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.states.GameState;

import java.util.Arrays;

public class Engine extends Vehicle
{
    //Member variables that will be unique stats of each engine
    private int volume;
    private int maxVolume;
    private int fillSpeed;
    private Integer id;

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
    public void ifInRangeFill(Station station){
        System.out.println("Checking if should fill");
        int xRange =station.getCol() + station.getWidth()/Tile.TILE_SIZE;
        int stationRow = station.getRow();

        if(this.col <= xRange && this.col > station.getCol() && this.row >= stationRow-5 && this.row <= stationRow-1){
            System.out.println("filling");
            this.volume = this.maxVolume;
            this.health = this.maxHealth;
        }
    }

    /**
     * Calls to update the required variables when the engine fires at a fortress
     */
    public void fire(){
        this.volume = Math.max(this.volume - this.damage, 0) ;
    }



    /***
     * Checks if any of the corners of the engines range are in the body of the fortress or station
     * @param fortress Entity that is being checked
     * @return returns true if there is any overlap, false otherwise
     */
    public Boolean checkForOverlap(Fortress fortress){
        int fortX = fortress.getCol();
        int fortWidth = fortress.getWidth() / 32;
        int fortY = fortress.getRow();
        int fortHeight = fortress.getHeight() / 32;

        int xDisplacement = Math.min(Math.abs(this.getCol() - (fortX + fortWidth)), Math.abs(this.getCol() - fortX));
        int yDisplacement = Math.min(Math.abs(this.getRow() - (fortY + fortHeight)), Math.abs(this.getRow() - fortY));

        return (Math.sqrt( (xDisplacement * xDisplacement) + (yDisplacement * yDisplacement) ) <= this.range);
    }

    /***
     * Method that will check if the Attacker is in range of the fortress and if so will damage it
     * @param fortress The fortress we are currently checking the bounds/range of
     */
    public void DamageFortressIfInRange(Fortress fortress){
        //this.setRangeCorners();

        if(checkForOverlap(fortress)){
            if (this.volume >= this.damage){
                this.fire();
                fortress.takeDamage(this.damage);

                int fortX = fortress.getX();
                int fortY = fortress.getY();
                int numBullets = 20;

                for (int i = 0; i< numBullets; i++){
                    GameState.bullets.add(new Bullet(this.x + (i * 2), this.y + (i*3),  fortX + 100, fortY + 70, true));
                }

            }
        }

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