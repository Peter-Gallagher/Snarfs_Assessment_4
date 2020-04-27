package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*Everything in this class is new*/
public class Patrol extends Vehicle implements Json.Serializable {

    //keep track of where we are on the patrol
    protected int pathIndex;
    protected List<Tile> path;
    private String pathId;

    protected TileManager tileManager;
    private boolean hasDropped = false;


    public Patrol(int col, int row, String textureId, int health, int damage, int range, int speed, List<Tile> path, String pathId, TileManager tileManager){
        super(col, row, textureId, health, damage, range, speed);
        this.tileManager = tileManager;
        this.path = path;
        this.pathId = pathId;
        pathIndex = 0;
    }



    //this will return the index of all tiles within range that the patrol can move to.

    /***
     * Method that finds all tiles which the patrol can move to
     * @return a list of indexes of all tiles the patrol can move to
     */
    public ArrayList<Integer> getPossibleMoveIndexes(){
        int startIndex = this.col + (this.row * 80);
        ArrayList<Integer> BFSReturnedValues;
        BFSReturnedValues = tileManager.BFS(tileManager.getAdjacencyList(), startIndex,80, this.speed);
        return  BFSReturnedValues;
    }


    /***
     *Method which finds the distances of a list of tiles to a given target tile
     * @param targetNode the target tile to calculate distance from
     * @param moves a list of tiles
     * @return a list of distances from moves to targetNode
     */
    protected ArrayList<Float> getDistanceToTarget(Tile targetNode, ArrayList<Integer> moves) {
        ArrayList<Float> listOfDistances = new ArrayList<Float>();
        int targetX = targetNode.getCol();
        int targetY = targetNode.getRow();


        for (Integer move : moves) {
            int consideredX = tileManager.getTileFromIndex(move).getCol();
            int consideredY = tileManager.getTileFromIndex(move).getRow();

            int xDistance = Math.abs(consideredX - targetX);
            int yDistance = Math.abs(consideredY - targetY);
            float absDistance = (float) (Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));

            listOfDistances.add(absDistance);
        }

        return listOfDistances;
    }

    /***
     * Method which finds which of a given set of tiles is closest to a target tile
     * @param moves a list of tile indexes
     * @return the index of the tile closest to the goal tile
     */
    protected int getTileClosestToGoal(ArrayList<Integer> moves){
        ArrayList<Float> distances = getDistanceToTarget(path.get(pathIndex), moves);
        int shortestDistanceIndex = 0;

        for (int i = 0; i < distances.size(); i++){
            if (distances.get(i) < distances.get(shortestDistanceIndex)){
                shortestDistanceIndex = i;
            }
        }

        return shortestDistanceIndex;
    }

    /***
     * Method to handel the movement of patrols
     */
    public void move(){
        ArrayList<Integer> moves = new ArrayList<>();
        int currentTileIndex = this.col + (this.row * 80);
        int moveIndex;
        Tile tileToMoveTo;

        moves = getPossibleMoveIndexes();
        moveIndex = getTileClosestToGoal(moves);
        tileToMoveTo = tileManager.getTileFromIndex(moves.get(moveIndex));

        tileManager.getTileAtLocation(this.col, this.row).setOccupied(false);
        tileManager.updateTileInAdjacencyList(currentTileIndex,1);
        this.col = tileToMoveTo.getCol();
        this.row = tileToMoveTo.getRow();
        this.x = tileToMoveTo.getX();
        this.y = tileToMoveTo.getY();
        tileToMoveTo.setOccupied(true);
        tileManager.updateTileInAdjacencyList(moves.get(moveIndex),0);


        updatePathIndex(path.get(pathIndex));
    }

    private void updatePathIndex(Tile currentGoal){
        if (this.col == currentGoal.getCol() && this.row == currentGoal.getRow()){
            pathIndex = (pathIndex + 1) % path.size();
        }
    }

    public void cleanup(GameState gameState){
        if(!hasDropped) {
            hasDropped = true;
            setTexture(AssetManager.getNull());
            gameState.getTileManager().getTileAtLocation(col, row).setMovable(true);
            gameState.getTileManager().getTileAtLocation(col, row).setOccupied(false);
            gameState.getEntityManager().dropPowerup(col, row, gameState);


        }
    }

    public String getPathId(){ return this.pathId; }

    public void setTileManager(TileManager tileManager){
        this.tileManager = tileManager;
    }

    public Patrol(){
        super(1, 1, "", 1, 1,1, 1);

    }
    @Override
    public void write(Json json) {
        json.writeValue("col", getCol());
        json.writeValue("row", getRow());
        json.writeValue("textureId", this.textureId);
        json.writeValue("health", getHealth());
        json.writeValue("maxHealth", getMaxHealth());
        json.writeValue("damage", getDamage());
        json.writeValue("range", getRange());
        json.writeValue("speed", getSpeed());
        json.writeValue("pathId", this.pathId);
        json.writeValue("hasDropped", this.hasDropped);
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {
        this.setCol(jsonValue.get("col").asInt());
        this.setRow(jsonValue.get("row").asInt());
        this.textureId = jsonValue.get("textureId").asString();
        this.texture = AssetManager.getTextureFromId(this.textureId);
        this.setHealth(jsonValue.get("health").asInt());
        this.setMaxHealth(jsonValue.get("maxHealth").asInt());
        this.setDamage(jsonValue.get("damage").asInt());
        this.setRange(jsonValue.get("range").asInt());
        this.setSpeed(jsonValue.get("speed").asInt());
        this.pathId = jsonValue.get("pathId").asString();
        this.hasDropped = jsonValue.get("hasDropped").asBoolean();
        switch (this.pathId){
            case("path1"): this.path = GameState.path1; break;
            case("path2"): this.path = GameState.path2; break;
            case("path3"): this.path = GameState.path3; break;
        }
        if(this.hasDropped){
            setTexture(AssetManager.getNull());
            this.setDead();
        }
    }
}


