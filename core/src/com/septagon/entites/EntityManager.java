package com.septagon.entites;

/**
 * Class that will be used to keep track of and handle the processing
 * of all entities in the game
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class EntityManager
{
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

    //Getters
    public ArrayList<Entity> getEntities() { return entities; }
}