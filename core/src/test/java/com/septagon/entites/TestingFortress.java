package com.septagon.entites;

/*
 * Class that will be used to define all the fortresses in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.helperClasses.AssetManager;

import java.util.ArrayList;

public class TestingFortress extends Attacker
{
    //Sets up texture that will be shown when a fortress is clicked on
    private Texture boundaryImage = null;
    //Stores if an engine is currently active/pressed on
    private boolean selected = false;
    //ArrayList that will store the corners of the bounding box for the range of the fortress
    private ArrayList<Integer> rangeCorners = new ArrayList<Integer>();

    /***
     * Constructor that calls the Entity constructor to set up all the member variables
     */
    public TestingFortress(int col, int row, int width, int height, String textureId, int health, int damage, int range)
    {
        super(col,row, width, height, textureId, health, damage, range);
    }

    /***
     * Method that will be called if an engine is in range of the fortress so that the engine can be damaged
     * @param e The current engine that is being checked
     */
    public void DamageEngineIfInRange(Engine e){
        if (e.getX() >= this.rangeCorners.get(0) && e.getX() < this.rangeCorners.get(1) && e.getY() >= this.rangeCorners.get(2) && e.getY() < this.rangeCorners.get(3)){
            e.takeDamage(this.damage);
        }
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
     * Method that is used to draw the fortress on the screen
     * Overwritten from Attacker
     * @param batch The batch that is used to display all objects on the screen
     */
    public void render(SpriteBatch batch)
    {
        //If the fortress is pressed, show its boundary image
        if(selected)
        {
            batch.draw(boundaryImage, (col - this.getRange()) * Tile.TILE_SIZE, (row - this.getRange()) * Tile.TILE_SIZE,
                    ((width / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE, ((height / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE);
        }
        super.render(batch);
    }

    //Getters
    public boolean isSelected() {return selected; }

    //Setters
    public void setSelected(boolean selected) { this.selected = selected; }

    protected void setRangeCorners() {
        //Makes an arrayList of the boundaries of the 2 x values and 2 y values at the corner
        Integer leftX = this.x - this.range;
        Integer rightX = this.x + this.width + this.range;
        Integer bottomY = this.y - this.range;
        Integer topY = this.y + this.height + this.range;
        this.rangeCorners.add(leftX);
        this.rangeCorners.add(rightX);
        this.rangeCorners.add(bottomY);
        this.rangeCorners.add(topY);
    }
}