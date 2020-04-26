package com.septagon.entites;

/**
 * Class that inherits from vehicle that will define all the fire engines
 * in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.septagon.game.Difficulty;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

public class Engine extends Vehicle implements Json.Serializable
{
    //Member variables that will be unique stats of each engine
    protected int volume;
    protected int maxVolume;
    protected int id;

    //Keeps track of whether the engine has moved on the current player turn
    private boolean moved = false;

    //Values used to track and control powerups
    protected int[] powerupTurnsLeft = new int[]{0,0,0,0,0};
    protected int[] powerupsActive = new int[]{0,0,0,0,0};
    private boolean poweredUp = false;
    private int baseHealth, baseDamage, baseRange, baseSpeed;
    private boolean invulnerable = false;

    /***
     * Constructor that Sets up the member variables for engine
     */
    public Engine(int col, int row, String textureId, int health, int damage, int range, int speed, int maxVolume, int id) {
        super(col, row, textureId, health, damage, range, speed);
        this.setDamage((int) (damage * Difficulty.getEngineDamageMod()));

        this.volume = (int) (maxVolume * Difficulty.getEngineVolumeMod());
        this.maxVolume = maxVolume;
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
            } else if(this.volume > 0){ //This is new for Assessment 4**
                shoot(attacker, useWater, volume);
                loseWater();
                return true;
            }
        }

        return false;
    }

    /**
     * Method called to check for powerups on Fire Engines every turn and handle them accordingly
     * New for Assessment 4
     */
    public void updatePowerup(EntityManager entityManager, TileManager tileManager, GameState gameState){
        for(int i = 0; i < powerupsActive.length; i++){
           if(powerupsActive[i] == 1){
               if(powerupTurnsLeft[i] == 0){
                   togglePowerup(i, false); //Toggles powerup with powerupID i off
                   poweredUp = false;
                   powerupsActive[i] = 0;
               } else if(powerupTurnsLeft[i] == 5){
                   powerupTurnsLeft[i] --;
                   togglePowerup(i, true); //Toggles powerup with powerupID i on
                   poweredUp = true;
               } else {
                   powerupTurnsLeft[i]--;
               }
           }
        }
    }

    /**
     * Method used to modify (buff or de-buff) a Fire Engines stats or properties based on the given
     * powerup related inputs
     * New for Assessment 4
     *
     * @param powerID Position of the powerup to be toggled in powerupsActive
     * @param toggle True if the powerup is being toggled on, false if toggling off
     */
    public void togglePowerup(int powerID, boolean toggle){
        if(toggle){
            poweredUp = true;
        }
        switch(powerID){
            case(0): //Restores hp and volume to full
                if(toggle){
                    health = maxHealth;
                    volume = maxVolume;
                    powerupTurnsLeft[powerID] = 0; //This powerup isnt turn based, so it is instantly turned off
                }
                break;
            case(1): //Increases the damage dealt by this Engine by 50%
                if(toggle){
                    damage = (int) Math.ceil(baseDamage * 1.5);
                } else { damage = baseDamage; }
                break;
            case(2): //Increases the range this Engine can attack at by 20%
                if(toggle){
                    range = (int) Math.ceil(baseRange * 1.2);
                } else { range = baseRange; }
                break;
            case(3): //Increases the range this Engine can move by 20%
                if(toggle){
                    speed = (int) Math.ceil(baseSpeed * 1.2);
                } else { speed = baseSpeed; }
                break;
            case(4): //Makes this Engine temporarily invulnerable
                if(toggle){
                    invulnerable = true;
                } else { invulnerable = false; }
                break;
        }
    }



    /**
     * Checks if Fire Engine is invulnerable from powerups, if it isn't then it takes damage
     * New for Assessment 4
     * @param damage the amount of damage to be taken
     */
    @Override
    public void takeDamage(int damage) {
        if (!this.invulnerable){
            this.health = Math.max(this.health - damage, 0);

            if (this.health <= 0) {
                this.setDead();
            }
        }
    }




    //Getters and Setters
    public int getMaxVolume() { return this.maxVolume; }
    public int getVolume() { return this.volume; }
    public int getID() { return this.id; }
    public boolean isMoved(){return this.moved;}
    public boolean poweredUp(){ return this.poweredUp;}

    public void setMoved(boolean moved){this.moved = moved;}
    public void setVolume(int volume) { this.volume = volume; }
    public void setID(int id) { this.id = id; }

    public Engine(){
        super(1, 1, "engineTexture1", 1,1,1,1);

    }


    @Override
    public void write(Json json) {
        json.writeValue("col", getCol());
        json.writeValue("row", getRow());
        json.writeValue("textureId", this.textureId);
        json.writeValue("health", getHealth());
        json.writeValue("maxHealth", getMaxHealth());
        json.writeValue("damage", getDamage());
        json.writeValue("range", getRange());
        json.writeValue("speed", getSpeed());
        json.writeValue("volume", getVolume());
        json.writeValue("maxVolume", getMaxVolume());
        json.writeValue("id", getID());
        json.writeValue("baseHealth", this.baseHealth);
        json.writeValue("baseDamage", this.baseDamage);
        json.writeValue("baseRange", this.baseRange);
        json.writeValue("baseSpeed", this.baseSpeed);
        json.writeValue("moved", this.moved);
        json.writeValue("powerupTurnsLeft", this.powerupTurnsLeft);
        json.writeValue("powerupsActive", this.powerupsActive);
        json.writeValue("invulnerable", this.invulnerable);

    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
        this.setCol(jsonMap.get("col").asInt());
        this.setRow(jsonMap.get("row").asInt());
        this.textureId = jsonMap.get("textureId").asString();
        this.texture = AssetManager.getTextureFromId(this.textureId);
        this.setHealth(jsonMap.get("health").asInt());
        this.setMaxHealth(jsonMap.get("maxHealth").asInt());
        this.setDamage(jsonMap.get("damage").asInt());
        this.setRange(jsonMap.get("range").asInt());
        this.setSpeed(jsonMap.get("speed").asInt());
        this.volume = jsonMap.get("volume").asInt();
        this.maxVolume  = jsonMap.get("maxVolume").asInt();
        this.id = jsonMap.get("id").asInt();
        this.baseHealth = jsonMap.get("baseHealth").asInt();
        this.baseDamage = jsonMap.get("baseDamage").asInt();
        this.baseRange = jsonMap.get("baseRange").asInt();
        this.baseSpeed = jsonMap.get("baseSpeed").asInt();
        this.moved = jsonMap.get("moved").asBoolean();
        this.powerupTurnsLeft = jsonMap.get("powerupTurnsLeft").asIntArray();
        this.powerupsActive = jsonMap.get("powerupsActive").asIntArray();
        this.invulnerable = jsonMap.get("invulnerable").asBoolean();
    }
}