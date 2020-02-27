package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

public class Powerup extends Entity {

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
                    e.powerupType = powerupValue;
                    System.out.println("Powering up! " + e.powerupType);
                    e.poweredUp = true;
                    e.turnsPowered = 0; //Overwrites any existing powerup when a new one is picked up
                    e.updatePowerup();

                    setTexture(AssetManager.getNull());
                    this.usedUp = true;
                }
            }
        }
    }
}
