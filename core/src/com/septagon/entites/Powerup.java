package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

public class Powerup extends Entity implements Json.Serializable{

    private int powerupValue;
    protected int engineID;
    protected boolean usedUp, inUse = false;
    private Tile curTile; //currentTile



    /***
     * Constructor that sets initial values for class members based on given input
     * @param col
     * @param row
     * @param width
     * @param height
     * @param textureId
     */
    public Powerup(int row, int col, int width, int height, String textureId, GameState gameState, int randInt) {
        super(col, row, width, height, textureId);
        curTile = gameState.getTileManager().getTileFromIndex(row+80*col);
        if(curTile == null){
            curTile = gameState.getTileManager().getTileAtLocation(col, row);
        }
        curTile.setOccupied(false);
        curTile.setMovable(true);
        this.powerupValue = randInt;
    }

    /**
     *
     * @param gameState
     */
    public void powerupUpdate(GameState gameState){
        if(!inUse) {
            curTile.setOccupied(false);
            curTile.setMovable(true);
            for (Engine e : gameState.getEngines()) {
                if ((e.getRow() == row) && (e.getCol() == col)) {
                    this.engineID = e.getID();
                    e.powerupsActive[powerupValue] = 1;
                    System.out.println("Powering up! " + powerupValue);
                    e.powerupTurnsLeft[powerupValue] = 5;
                    e.updatePowerup(gameState.getEntityManager(), gameState.getTileManager(), gameState);

                    //setTexture(AssetManager.getNull());
                    this.inUse = true;
                    this.setCol(e.getCol());
                    this.setRow(e.getRow()+1);
                }
            }
        } else if(!usedUp){
            for(Engine e : gameState.getEngines()) {
                if(e.getID() == this.engineID) {
                    this.setCol(e.getCol());
                    this.setRow(e.getRow() + 1);
                    if (e.powerupTurnsLeft[powerupValue] <= 0) {
                        this.usedUp = true;
                    }
                    break;
                }
            }
        } else {
            setTexture(AssetManager.getNull());
        }
    }

    public Powerup(){
        super(0, 0, Tile.TILE_SIZE, Tile.TILE_SIZE, "powerup1");
    }

    @Override
    public void write(Json json) {
        json.writeValue("col", getCol());
        json.writeValue("row", getRow());
        json.writeValue("textureId", this.textureId);
        json.writeValue("powerupValue", this.powerupValue);
        json.writeValue("usedUp", this.usedUp);
        json.writeValue("inUse", this.inUse);
        json.writeValue("engineID", this.engineID);
    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
        this.setCol(jsonMap.get("col").asInt());
        this.setRow(jsonMap.get("row").asInt());
        this.textureId = jsonMap.get("textureId").asString();
        this.texture = AssetManager.getTextureFromId(this.textureId);
        this.powerupValue = jsonMap.get("powerupValue").asInt();
        this.usedUp = jsonMap.get("usedUp").asBoolean();
        this.inUse = jsonMap.get("inUse").asBoolean();
        this.engineID = jsonMap.get("engineID").asInt();
        if (this.usedUp){
            setTexture(AssetManager.getNull());
        }
    }
}
