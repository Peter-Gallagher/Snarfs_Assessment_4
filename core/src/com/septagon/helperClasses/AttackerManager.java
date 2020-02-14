package com.septagon.helperClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.entites.*;
import com.septagon.states.GameState;

import java.util.ArrayList;

/**
 * Helper class that is used to handle a lot of the Attacker classes
 */

public class AttackerManager
{
    public ArrayList<Engine> getEngines() {
        return engines;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Fortress> getFortresses() {
        return fortresses;
    }

    public ArrayList<Patrol> getPatrols() {
        return patrols;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Tile getCurrentlyTouchedTile() {
        return currentlyTouchedTile;
    }

    public Tile getPreviouslyTouchedTile() {
        return previouslyTouchedTile;
    }

    public Engine getCurrentEngine() {
        return currentEngine;
    }

    private ArrayList<Engine> engines;
    private ArrayList<Tile> tiles;
    private ArrayList<Fortress> fortresses;
    private ArrayList<Patrol> patrols;
    private GameState gameState;

    private Tile currentlyTouchedTile = null;
    private Tile previouslyTouchedTile = null;
    private Engine currentEngine = null;

    private int turnOfFirstAttack;

    public AttackerManager(ArrayList<Engine> engines, ArrayList<Tile> tiles, ArrayList<Patrol> patrols, ArrayList<Fortress> fortresses, GameState gameState){
        this.engines = engines;
        this.tiles = tiles;
        this.patrols = patrols;
        this.fortresses = fortresses;
        this.gameState = gameState;
    }

    /**
     * Method that will move the camera position to one of the attackers
     *
     * @param attacker The attacker which the camera should be moved to
     */
    public void snapToAttacker(Attacker attacker, TiledGameMap gameMap, OrthographicCamera camera)
    {
        //TODO make the snapping less spazzy if possible or change it
        //Get the positions of where the camera should move to
        int newCameraX = attacker.getX() + (attacker.getWidth() / 2);
        int newCameraY = attacker.getY() + (attacker.getHeight() / 2);

        //Make sure the new camera position is within the bounds of the screen
        if (newCameraX <= Gdx.graphics.getWidth() / 2)
            newCameraX = Gdx.graphics.getWidth() / 2;
        else if (newCameraX >= (gameMap.getMapWidth() * Tile.TILE_SIZE) - Gdx.graphics.getWidth() / 2)
            newCameraX = (gameMap.getMapWidth() * Tile.TILE_SIZE) - Gdx.graphics.getWidth() / 2;

        if (newCameraY <= Gdx.graphics.getHeight() / 2)
            newCameraY = Gdx.graphics.getHeight() / 2;
        else if (newCameraY >= (gameMap.getMapHeight() * Tile.TILE_SIZE) - Gdx.graphics.getHeight() / 2)
            newCameraY = (gameMap.getMapHeight() * Tile.TILE_SIZE) - Gdx.graphics.getHeight() / 2;

        //Move the camera to its new position
        camera.position.x = newCameraX;
        camera.position.y = newCameraY;
        camera.update();
    }

    /***
     * Checks if all engines have been moved or not so that the game knows when to end the players turn
     * @return boolean of whether all the engines have been moved or not
     */
    //TODO why is this here? move to Fire Engine
    public boolean allEnginesMoved(){
        for(Engine fireEngine : engines){

            //just return the function!?
            if(!fireEngine.isMoved()){
                return false;
            }

        }
        return true;
    }

    /***
     * Check if the user has pressed on a fortress and display a bounding box if they have
     * @param x The x position of the input - in world coordinates
     * @param y The y position of the input - in world coordinates
     */
    public void checkIfTouchingFortress(float x, float y)
    {
        //Loops through all fortresses to check if any have been pressed
        for(Fortress fortress: fortresses)
        {
            //If the clicked on tile is within the bounds of the fortress make it selected, if not make not selected
            if(x >= fortress.getX() && x <= fortress.getX() + fortress.getWidth() &&
                    y >= fortress.getY() && y <= fortress.getY() + fortress.getHeight()) {
                fortress.setSelected(true);
            }
            else {
                fortress.setSelected(false);
            }
        }
    }

    /**
     * Method that works out is all the engines have been destroyed by the fortresses
     * @return Returns true if all engines are destroyed, false otherwise
     */
    public boolean checkIfAllEnginesDead(){
        for(Engine fireEngine: engines){
            //again?
            if(!fireEngine.isDead()) return false;
        }
        return true;
    }

    /***
     * Called when the InputManager detects an input and is used to work out what tile was pressed and what should occur as a result
     * @param x X position of the input
     * @param y Y position of the input
     * @return boolean that will say if a tile has been pressed or not (true if it has been pressed)
     */
    public Boolean touchedTile(float x, float y)
    {
        //Loops through all tiles to see if it has been pressed

        currentlyTouchedTile = getTileClicked(x,y);
        System.out.println("Clicked tile Col =" + currentlyTouchedTile.getCol() + " Row = " + currentlyTouchedTile.getRow());

        if (currentlyTouchedTile != null){
            //updated the pointers to the current and previous tiles
            //if an engine has been previously pressed on, check on if a valid move has been pressed
            //and if so perform that move
            if (currentEngine != null) {
                if (currentlyTouchedTile.isMovable() && !currentEngine.isMoved() && !currentEngine.isDead()) {
                    currentlyTouchedTile.setOccupied(true);
                    previouslyTouchedTile.setOccupied(false);
                    gameState.getTileManager().updateTileInAdjacencyList(previouslyTouchedTile.getIndex(), 1);
                    currentEngine.setX(currentlyTouchedTile.getX());
                    currentEngine.setY(currentlyTouchedTile.getY());
                    currentEngine.setMoved(true);
                }
            }
            previouslyTouchedTile = currentlyTouchedTile;

            //If not a moveable tile pressed, check if a fortress tile has been pressed
            checkIfTouchingFortress(x, y);
            for (Engine fireEngine: engines){
                if (currentlyTouchedTile.getCol() == fireEngine.getCol() && currentlyTouchedTile.getRow() == fireEngine.getRow()) {
                    currentEngine = fireEngine;
                    gameState.getUiManager().setCurrentEngine(fireEngine);
                    gameState.getTileManager().setMovableTiles(currentEngine);
                    return true;
                }
        }
        for(Tile tile: tiles) {
            //When we have found the tile that has been pressed, perform neccessary processing
            if(tile.checkIfClickedInside(x, y)) {

                }
            }
        }
        return false;
    }


    public Tile getTileClicked(float x, float y){
        for(Tile tile: tiles) {
            if(tile.checkIfClickedInside(x, y)){
                return tile;
            }
        }
        return null;
    }
    /***
     * Method that is run for the phase of the game where damage events occur (damage, filling etc) turn
     */
    //TODO include patrols in battleturn
    public void BattleTurn(Fortress fortress){
        //Set the moved variable to false for each engine and then check if damages can occur
        gameState.getTileManager().resetMovableTiles();
        for (int i = 0; i < engines.size(); i++){
            engines.get(i).setMoved(false);

            if(engines.get(i).damageEnemyIfInRange(fortress)){
                if (turnOfFirstAttack > 0){
                    turnOfFirstAttack = gameState.getTurnsPassed();
                }
            }

            if(fortress.damageIfInRange(engines.get(i))){
                fortress.enemyBullets(engines.get(i), 25);
            }

            if (engines.get(i).isDead()){
                engines.remove(engines.get(i));
            }

            if (fortress.isDead()){
                fortresses.remove(fortress);
            }
            //engines.get(i).ifInRangeFill(gameState.getStation());
        }
    }


    public void handleDeadEngines(){
        ArrayList<Engine> destroyedEngines = new ArrayList<>();

        for (Engine engine : engines) {
            if (engine.isDead()){
                destroyedEngines.add(engine);
            }
        }

        for (Engine destroyedEngine : destroyedEngines) {
            engines.remove(destroyedEngine);
        }
    }

    public void handleDeadPatrols(){
        ArrayList<Patrol> destroyedPatrols = new ArrayList<>();

        for (Patrol patrol : patrols) {
            if (patrol.isDead()){
                destroyedPatrols.add(patrol);
            }
        }

        for (Patrol destroyedPatrol : destroyedPatrols) {
            engines.remove(destroyedPatrol);
        }
    }

    /***
     * Renders a grid showing the player where the engine that they have pressed on can move to
     */
    public void renderMovementGrid(SpriteBatch batch){
        //If there is a engine that has been pressed and that engine has not yet moved this turn
        if(currentlyTouchedTile != null && currentEngine != null && !currentEngine.isMoved() && !currentEngine.isDead()) {
            //Draw grid around engine with all the movable spaces
            for(Tile tile: tiles) {
                if (tile.isMovable()) {
                    batch.draw(AssetManager.getMoveSpaceTexture(), tile.getX(), tile.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
                }
            }
        }
    }

    public int getTurnOfFirstAttack(){
        return turnOfFirstAttack;
    }
}
