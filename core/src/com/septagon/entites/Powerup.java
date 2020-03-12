package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

public class Powerup extends Entity implements Json.Serializable{

    private int powerupValue;
    private boolean usedUp = false;


    /***
     * Constructor that sets initial values for class members based on given input
     * @param col
     * @param row
     * @param width
     * @param height
     * @param texture
     */
    public Powerup(int row, int col, int width, int height, Texture texture, GameState gameState, int randInt) {
        super(col, row, width, height, texture);
        Tile curTile = gameState.getTileManager().getTileFromIndex(row+80*col);
        if(curTile == null){
            curTile = gameState.getTileManager().getTileAtLocation(col, row);
        }
        curTile.setOccupied(false);
        curTile.setMovable(true);
        this.powerupValue = randInt;
    }

    /**
     *
     * @param tileManager
     * @param gameState
     */
    public void checkContact(TileManager tileManager, GameState gameState){
        if(!usedUp) {
            for (Engine e : gameState.getEngines()) {
                if ((e.getRow() == row) && (e.getCol() == col)) {
                    e.powerupsActive[powerupValue] = 1;
                    System.out.println("Powering up! " + powerupValue);
                    e.powerupTurnsLeft[powerupValue] = 5;
                    e.updatePowerup();

                    setTexture(AssetManager.getNull());
                    this.usedUp = true;
                }
            }
        }
    }

    public Powerup(){
        super(0, 0, Tile.TILE_SIZE, Tile.TILE_SIZE, AssetManager.getPowerup(1));//TODO: allow texture to be saved
    }
    @Override
    public void write(Json json) {
        json.writeValue("col", getCol());
        json.writeValue("row", getRow());
        json.writeValue("powerupValue", this.powerupValue);
        json.writeValue("usedUp", this.usedUp);
    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
        String test = jsonMap.toString();
        this.setCol(jsonMap.get("col").asInt());
        this.setRow(jsonMap.get("row").asInt());
        this.powerupValue = jsonMap.get("powerupValue").asInt();
        this.usedUp = jsonMap.get("usedUp").asBoolean();
    }
}
