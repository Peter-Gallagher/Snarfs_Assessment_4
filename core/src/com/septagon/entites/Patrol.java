package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.ArrayList;

/*Everything in this class is new*/
public class Patrol extends Vehicle  {

    //keep track of where we are on the patrol
    protected int pathIndex;
    protected ArrayList<Tile> path;

    protected TileManager tileManager;
    private boolean hasDropped = false;

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, ArrayList<Tile> path, TileManager tileManager){
        super(col, row, texture, health, damage, range, speed);
        this.tileManager = tileManager;
        this.path = path;
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
        ArrayList<Integer> moves = new ArrayList<Integer>();
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
            gameState.getTileManager().getTileFromIndex(row+80*col);
            gameState.getEntityManager().dropPowerup(col, row, gameState);

        }
    }
}


