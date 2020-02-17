package com.septagon.entites;

/**
 * Class that will be used to define all the fortresses in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.helperClasses.AssetManager;
import com.septagon.states.GameState;

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
    }

    /***
     * Method that is used to draw the fortress on the screen
     * Overwritten from Attacker
     * @param batch The batch that is used to display all objects on the screen
     */
    /*This changed*/
    public void render(SpriteBatch batch)
    {
        //If the fortress is pressed, show its boundary image
        if(selected && !dead)
        {
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