package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.ArrayList;

public class Patrol extends Vehicle  {

    //keep track of where we are on the patrol
    private int pathIndex;
    private ArrayList<Tile> path;

    private TileManager tileManager;

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, ArrayList<Tile> path, TileManager tileManager){
        super(col, row, texture, health, damage, range, speed);
        this.tileManager = tileManager;
        this.path = path;
        pathIndex = 0;
    }



    //this will return the index of all tiles within range that the patrol can move to.
    public ArrayList<Integer> functionForLucas(){
        int startIndex = this.col + (this.row * 80);
        ArrayList<Integer> BFSReturnedValues;
        BFSReturnedValues = tileManager.BFS(tileManager.getAdjacencyList(), startIndex,80, this.speed);
        return  BFSReturnedValues;
    }






    //Checks whether a fireEngine is within its range. Return true if it is, return false if it isnt
    private boolean inRange(Engine fireEngine) {
        int engineCol = fireEngine.getCol();
        int engineRow = fireEngine.getRow();

        int xDisplacement = Math.min(Math.abs(this.getCol() - engineCol), Math.abs((this.getCol() + (this.width / 32)) - engineCol));
        int yDisplacement = Math.min(Math.abs(this.getRow() - engineRow), Math.abs((this.getRow() + (this.height / 32)) - engineRow));

        return (Math.sqrt((xDisplacement * xDisplacement) + (yDisplacement * yDisplacement)) <= this.range);
    }

    //TODO implement shooting mechanism
    private void shoot(Engine fireEngine){
        int numBullets = 25;

        int engineX = fireEngine.getX();
        int engineY = fireEngine.getY();

        int centreX = this.x + (this.width/2);
        int centreY = this.y + (this.height/2);

        //Actual functionality
        if(inRange(fireEngine)){
            fireEngine.takeDamage(this.damage);

            for (int i = 0; i< numBullets; i++){
                GameState.bullets.add(new Bullet(centreX + (i * 2), centreY + (i*3),  engineX + 10, engineY, false));
            }
        }
    }

    //Function that returns a list of distances of possible moves from the target node
    private ArrayList<Float> getDistanceToTarget(Tile targetNode, ArrayList<Integer> moves) {
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

    private int getTileClosestToGoal(ArrayList<Integer> moves){
        ArrayList<Float> distances = getDistanceToTarget(path.get(pathIndex), moves);
        int shortestDistanceIndex = 0;

        for (int i = 0; i < distances.size(); i++){
            if (distances.get(i) < distances.get(shortestDistanceIndex)){
                shortestDistanceIndex = i;
            }
        }

        return shortestDistanceIndex;
    }

    //TODO implement movement mechanism
    public void move(){
        ArrayList<Integer> moves = new ArrayList<Integer>();
        int currentTileIndex = this.col + (this.row * 80);
        int moveIndex;
        Tile tileToMoveTo;

        moves = functionForLucas();
        moveIndex = getTileClosestToGoal(moves);
        tileToMoveTo = tileManager.getTileFromIndex(moves.get(moveIndex));

        tileManager.getTileAtLocation(this.col, this.row,80,50).setOccupied(false);
        tileManager.updateTileInAdjacencyList(currentTileIndex,1);
        this.col = tileToMoveTo.getCol();
        this.row = tileToMoveTo.getRow();
        tileToMoveTo.setOccupied(true);
        tileManager.updateTileInAdjacencyList(moves.get(moveIndex),0);

        System.out.println("Moving to X:" + col + " Y:" + row);

        updatePathIndex(path.get(pathIndex));
        }

        private void updatePathIndex(Tile currentGoal){
            if (this.col == currentGoal.getCol() && this.row == currentGoal.getRow()){
                pathIndex = (pathIndex + 1) % path.size();
            }
        }
    }

