package com.septagon.entites;

/**
 * Class that will be used to keep track of and handle the processing
 * of all entities in the game
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.ArrayList;
import java.util.Random;

public class EntityManager {

    private ArrayList<Powerup> powerups = new ArrayList<Powerup>();


    /*This is new*/
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    //ArrayList that will hold a pointer to all the entities in the game
    private ArrayList<Entity> entities;

    /***
     * Constructor to initialise the ArrayList of the engine
     */
    public EntityManager()
    {
        entities = new ArrayList<Entity>();
    }

    /***
     * Adds an entity to the arraylist of engines
     * @param newEntity The engine to be added
     */
    public void addEntity(Entity newEntity)
    {
        entities.add(newEntity);
    }

    /***
     * Removes an entity from the ArrayList
     * @param entityToRemove The entity to be removed
     */
    /*This changed*/
    public void removeEntity(Entity entityToRemove)
    {
        entities.remove(entityToRemove);
    }

    /***
     * Calls the initialise method for all entities in the game
     */
    /*This changed*/
    public void initialise()
    {
        for(Entity entity: entities)
            entity.initialise();
    }

    /***
     * Calls the update method for all entities in the game
     */
    public void update()
    {
        for(Entity entity: entities)
            entity.update();
    }
    /***
     * Calls the render method for all entities in the game
     */
    /*This changed*/
    public void render(SpriteBatch batch)
    {
        for(Entity entity: entities)
            entity.render(batch);
    }

    /**
     *
     * @param row
     * @param col
     * @param gameState
     */
    protected void dropPowerup(int row, int col, GameState gameState){
        int randInt = new Random().nextInt(5);
        Powerup powerup = new Powerup(col, row, 32, 32, AssetManager.getPowerup(randInt), gameState, randInt);
        powerups.add(powerup);
        entities.add(powerup);
    }

    /**
     *
     * @param tileManager
     * @param gameState
     */
    public void checkPowerups(TileManager tileManager, GameState gameState){
        if(powerups.size() > 0){
            for(Powerup p : powerups){
                p.powerupUpdate(this, tileManager, gameState);
            }
        }
    }

    public void movePowerup(Engine engine, TileManager tileManager, GameState gameState) {
        for(Powerup p : powerups){
            if(p.inUse) {
                if (p.affectedEngine.id == engine.id) {
                    p.powerupUpdate(this, tileManager, gameState);
                }
            }
        }
    }


    //Getters
    public ArrayList<Entity> getEntities() { return entities; }
}