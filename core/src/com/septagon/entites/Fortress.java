package com.septagon.entites;

/**
 * Class that will be used to define all the fortresses in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.septagon.game.Difficulty;
import com.septagon.helperClasses.AssetManager;
import com.septagon.states.GameState;

public class Fortress extends Attacker implements Json.Serializable
{
    //Stores if an engine is currently active/pressed on
    protected boolean selected = false;
    private String defeatedTextureId;
    private Texture defeatedTexture;

    /***
     * Constructor that calls the Entity constructor to set up all the member variables
     */
    public Fortress(int col, int row, int width, int height, String textureId, String defeatedTextureId, int health, int damage, int range)
    {
        super(col,row, width, height, textureId, health, damage, range);
        this.setDamage((int) (damage * Difficulty.getFortressDamageMod()));
        this.defeatedTextureId = defeatedTextureId;
        if(this.defeatedTextureId != null) {
            this.defeatedTexture = AssetManager.getTextureFromId(defeatedTextureId);
        }
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

    public Fortress(){
        super(1, 1,1,1, null, 1,1,1);


    }


    @Override
    public void write(Json json) {
        json.writeValue("col", getCol());
        json.writeValue("row", getRow());
        json.writeValue("textureId", this.textureId);
        json.writeValue("defeatedTextureId", this.defeatedTextureId);
        json.writeValue("health", getHealth());
        json.writeValue("maxHealth", getMaxHealth());
        json.writeValue("damage", getDamage());
        json.writeValue("width", getWidth());
        json.writeValue("height", getHeight());
        json.writeValue("health", getHealth());
        json.writeValue("damage", getDamage());
        json.writeValue("range", getRange());
        json.writeValue("dead", this.dead);
        json.writeValue("defeatedTexture", this.defeatedTexture);

    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
        String test = jsonMap.toString();
        this.setCol(jsonMap.get("col").asInt());
        this.setRow(jsonMap.get("row").asInt());
        this.textureId = jsonMap.get("textureId").asString();
        this.defeatedTextureId = jsonMap.get("defeatedTextureId").asString();
        if (textureId != null) {
            this.texture = AssetManager.getTextureFromId(this.textureId);
            this.defeatedTexture = AssetManager.getTextureFromId(this.defeatedTextureId);
        }
        this.setHealth(jsonMap.get("health").asInt());
        this.setMaxHealth(jsonMap.get("maxHealth").asInt());
        this.setDamage(jsonMap.get("damage").asInt());
        this.dead = jsonMap.get("dead").asBoolean();
        this.setWidth(jsonMap.get("width").asInt());
        this.setHeight(jsonMap.get("height").asInt());
        this.setRange(jsonMap.get("range").asInt());
    }
}