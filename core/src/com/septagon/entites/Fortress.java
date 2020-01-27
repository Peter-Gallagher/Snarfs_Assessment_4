package com.septagon.entites;

/**
 * Class that will be used to define all the fortresses in the game
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.helperClasses.AssetManager;
import com.septagon.states.GameState;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fortress extends Attacker
{
    //Stores if an engine is currently active/pressed on
    private boolean selected = false;
    private Texture defeatedTexture;

    /***
     * Constructor that calls the Entity constructor to set up all the member variables
     */
    public Fortress(int col, int row, int width, int height, Texture texture, Texture defeatedTexture, int health, int damage, int range)
    {
        super(col,row, width, height, texture, health, damage, range);
        this.defeatedTexture = defeatedTexture;
    }


    /***
     * Initialise method that is used to init all variables in the class and set up everything
     * Overwritten from Attacker
     */
    public void initialise()
    {
        super.initialise();
        setRangeCorners();
    }

    /***
     * Method that will be called if an engine is in range of the fortress so that the engine can be damaged
     * @param fireEngine The current engine that is being checked
     */
    public void DamageEngineIfInRange(Engine fireEngine) {
        System.out.println("Checking if should damage engine");
        if (fireEngine.getCol() >= this.rangeCorners.get(0) && fireEngine.getCol() < this.rangeCorners.get(1) && fireEngine.getRow() >= this.rangeCorners.get(2) && fireEngine.getRow() < this.rangeCorners.get(3)){
            fireEngine.takeDamage(this.damage);
            GameState.bullets.add(new Bullet(this.x + 150, this.y + 50, fireEngine.getX() + 20, fireEngine.getY() + 10, false));
            GameState.bullets.add(new Bullet(this.x + 100, this.y + 25, fireEngine.getX() + 20, fireEngine.getY() + 10, false));
            GameState.bullets.add(new Bullet(this.x + 200, this.y + 75, fireEngine.getX() + 20, fireEngine.getY() + 10, false));
        }
    }

    /***
     * Method that is used to draw the fortress on the screen
     * Overwritten from Attacker
     * @param batch The batch that is used to display all objects on the screen
     */
    public void render(SpriteBatch batch)
    {
        //If the fortress is pressed, show its boundary image
        if(selected && !dead)
        {
            batch.draw(AssetManager.getFortressBoundaryImage(), (col - this.getRange()) * Tile.TILE_SIZE, (row - this.getRange()) * Tile.TILE_SIZE,
                    (((int)width / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE, (((int)height / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE);
        }else if(dead){
            this.texture = this.defeatedTexture;
    }


        super.render(batch);
    }

    //Getters
    public boolean isSelected() { return selected; }

    //Setters
    public void setSelected(boolean selected) { this.selected = selected; }


}