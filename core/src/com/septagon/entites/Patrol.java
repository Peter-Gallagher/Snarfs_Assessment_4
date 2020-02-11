package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.ArrayList;

public class Patrol extends Vehicle  {

    //keep track of where we are on the patrol
    private Tile currentTile;
    private int tileIndex;
    private int[][] adjacencyList;

    private TileManager tileManager;

    ArrayList<Integer> moves = new ArrayList<Integer>();

    public Patrol(int col, int row, Texture texture, int health, int damage, int range, int speed, char direction, ArrayList<Tile> path,TileManager tileManager){
        super(col, row, texture, health, damage, range, speed);
        this.tileManager = tileManager;
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
    private ArrayList<Integer> distanceToTarget(Patrol patrol, Tile targetNode) {
        ArrayList<Integer> listOfDistances = new ArrayList<Integer>();

        for (int i = 0; i <= moves.size(); i++) {

            int targetCol = tileManager.getTileFromIndex(moves.get(i)).getCol();
            int targetRow = tileManager.getTileFromIndex(moves.get(i)).getRow();

            int xDistance = Math.abs(targetCol - targetNode.getCol());
            int yDistance = Math.abs(targetRow - targetNode.getRow());
            int absDistance = (int) Math.round(Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));

            listOfDistances.add(absDistance);
        }
        return listOfDistances;
    }

    //TODO implement movement mechanism
    private void move(Patrol patrol){
        moves = tileManager.BFS(adjacencyList, tileIndex, 5, 20);
        }
    }

