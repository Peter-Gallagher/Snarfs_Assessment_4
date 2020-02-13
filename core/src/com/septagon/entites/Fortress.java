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
    protected boolean selected = false;
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
        //setRangeCorners();
    }

    /***
     * Method that will be called if an engine is in range of the fortress so that the engine can be damaged
     * @param fireEngine The current engine that is being checked
     */
    public void DamageEngineIfInRange(Engine fireEngine) {
        System.out.println("Checking if should damage engine");

        int engineCol = fireEngine.getCol();
        int engineRow = fireEngine.getRow();

        int xDisplacement = Math.min(Math.abs(this.getCol() - engineCol), Math.abs((this.getCol() + (this.width / 32)) - engineCol));
        int yDisplacement = Math.min(Math.abs(this.getRow() - engineRow), Math.abs((this.getRow() + (this.height / 32)) - engineRow));

        int engineX = fireEngine.getX();
        int engineY = fireEngine.getY();

        int centreX = this.x + (this.width/2);
        int centreY = this.y + (this.height/2);

        //define the number of projectiles are drawn when an entity is shot
        int numBullets = 25;

        if(Math.sqrt( (xDisplacement * xDisplacement) + (yDisplacement * yDisplacement) ) <= this.range){
            fireEngine.takeDamage(this.damage);

            for (int i = 0; i< numBullets; i++){
                GameState.bullets.add(new Bullet(centreX + (i * 2), centreY + (i*3),  engineX + 10, engineY, false));
            }
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
            //int imgX = (col - this.getRange()) * Tile.TILE_SIZE;
            //int imgY = (row - this.getRange()) * Tile.TILE_SIZE;
            //int imgWidth = ( ((int)width / Tile.TILE_SIZE) + this.range * 2) * Tile.TILE_SIZE;
            //int imgHeight = ( ((int)height / Tile.TILE_SIZE) + this.range * 2) * Tile.TILE_SIZE;
            int imgX = (col ) * Tile.TILE_SIZE;
            int imgY = (row ) * Tile.TILE_SIZE;
            int imgWidth = ( (width / Tile.TILE_SIZE) ) * Tile.TILE_SIZE;
            int imgHeight = ( (height / Tile.TILE_SIZE) ) * Tile.TILE_SIZE;

            batch.draw(AssetManager.getFortressBoundaryImage(), imgX, imgY, imgWidth, imgHeight);
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