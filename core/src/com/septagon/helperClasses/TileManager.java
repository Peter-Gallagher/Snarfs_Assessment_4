package com.septagon.helperClasses;

import com.septagon.entites.Engine;
import com.septagon.entites.Patrol;
import com.septagon.entites.Tile;
import com.septagon.entites.TiledGameMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Used to handle all of the tile interaction methods and methods for updating values of tiles
 */

public class TileManager {


    private ArrayList<Engine> engines;
    private ArrayList<Tile> tiles;
    private int[][] adjacencyList;
    /*This changed*/
    public TileManager(ArrayList<Engine> engines, ArrayList<Tile> tiles){
        this.engines = engines;
        this.tiles = tiles;
        adjacencyList = new int[tiles.size()][4];
    }

    public TileManager(ArrayList<Tile> tiles){
        this.tiles = tiles;
        adjacencyList = new int[tiles.size()][4];
    }


    /***
     * Clear all the current moveable tiles
     */
    public void resetMovableTiles(){
        for(Tile tile: tiles)
        {
            tile.setMovable(false);
        }
    }

    /***
     * Sets up all tiles which contain an engine, fortress or the station to be occupied
     * @param gameMap a tileMap containing the map of the game
     */
    /*This is new*/
    public void setOccupiedTiles(TiledGameMap gameMap)
    {
        setEngineTilesOccupied();

        boolean[][]  passableTiles = gameMap.getPassable();

        for (int i = 0; i < passableTiles.length - 1; i++){
            for (int j = 0; j < passableTiles[0].length - 1; j++){
                Tile tile = getTileAtLocation(j, i);
                tile.setOccupied(passableTiles[i][j]);
            }
        }
    }

    /***
     * sets all tiles occupied by fire engines as inaccessible
     */
    /*This is new*/
    public void setEngineTilesOccupied(){
        //Set the tiles that currently have an engine on to be occupied
        for (Engine fireEngine : engines)
        {
            int engineRow = fireEngine.getRow();
            int engineCol = fireEngine.getCol();
            Integer tileIndex = null;

            for (Tile tile : tiles)
            {
                if (tile.getCol() == engineCol && tile.getRow() == engineRow)
                {
                    tile.setOccupied(true);
                    tileIndex = tile.getIndex();
                    updateTileInAdjacencyList(tileIndex, 0);

                    break;
                }
            }
        }
    }

    /***
     * updates the adjacency list to reflect a tile becoming (un)occupied
     * @param tileIndex the index of the tile being updated
     * @param access indicates if the tile is to made accessible: 1 = accessible, 0 = inaccessible
     */
    /*This is new*/
    public void updateTileInAdjacencyList(int tileIndex, int access){
            try {
            adjacencyList[tileIndex - 1][1] = access;
            adjacencyList[tileIndex + 1][0] = access;
            adjacencyList[tileIndex + 80][2] = access;
            adjacencyList[tileIndex - 80][3] = access;
            } catch (Exception e){

            }

        }


    /***
     * Method to get the tile at a row and column
     * @param col The column of the tile you want to get
     * @param row The row of the tile you want to get
     * @return The tile at the location asked for
     */
    public Tile getTileAtLocation(int col, int row)
    {
        for(Tile t: tiles)
        {
            if(t.getCol() == col && t.getRow() == row)
                return t;
        }
        return null;
    }

    /***
     * Get the movable tiles for all the engines based on their positions
     */
    /*This changed*/
    public void setMovableTiles(Engine currentEngine) {
        //Reset all moveable tiles from previous turn
        resetMovableTiles();
        setEngineTilesOccupied();
        Tile accessTile;

        int startTileIndex = currentEngine.getCol() + (currentEngine.getRow() * 80);

        for (Integer index: BFS(adjacencyList, startTileIndex, 80, currentEngine.getSpeed())) {
            accessTile = this.getTileAtLocation(index % 80, index / 80);
            accessTile.setMovable(true);
        }
    }

    /***
     * Creates an adjacency list for all tiles in the game map
     * @param maxWidth the width of the tileMap
     * @param maxHeight the height of the tileMap
     */
    /*This is new*/
    public void createAdjacencyList(int maxWidth, int maxHeight){

        Tile checkTile;

        for (int width = 0; width <= maxWidth; width++) {
            for (int height = 0; height <= maxHeight; height++) {

                checkTile = this.getTileAtLocation(width, height);
                int tileIndex = width + (maxWidth * height);

                if (checkTile != null){
                    if(checkTileAdjacent(checkTile,-1,0, maxWidth, maxHeight)){
                        adjacencyList[tileIndex][0] = 1;
                    }
                    if(checkTileAdjacent(checkTile,1,0, maxWidth, maxHeight)){
                        adjacencyList[tileIndex][1] = 1;
                    }
                    if(checkTileAdjacent(checkTile,0,-1, maxWidth, maxHeight)){
                        adjacencyList[tileIndex][2] = 1;
                    }
                    if(checkTileAdjacent(checkTile,0,1, maxWidth, maxHeight)){
                        adjacencyList[tileIndex][3] = 1;
                    }
                }
            }
        }

    }


    /***
     * Performs a breadth first search of tiles which a can be reached from a statring position within a given range
     * @param tileAdjacencyList an adjacency list indicating which tiles are adjacent
     * @param startTileIndex the index of the start tile within tiles
     * @param width the width of the tileMap
     * @param maxDepth the height of the tileMap
     * @return a list containing the indexes of all tiles which can be reached
     */
    /*This is new*/
    public ArrayList<Integer> BFS(int[][] tileAdjacencyList, int startTileIndex, int width, int maxDepth) {

        int numNodes = tileAdjacencyList.length;
        int depth = 0;
        int newIndex;
        Integer qVal;

        ArrayList<Integer> visitedTilesIndex = new ArrayList<Integer>();
        boolean[] visited = new boolean[numNodes];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(startTileIndex);
        queue.add(null);

        while (queue.isEmpty() == false) {

            qVal = queue.poll();

            if (qVal == null){
                if (queue.isEmpty()){
                    break;
                }
                depth++;
                queue.offer(null);
                qVal = queue.poll();
                if(depth >= maxDepth || qVal == null){
                    break;
                }
            }

            int currentTileIndex = qVal;

            if (currentTileIndex < 0 || currentTileIndex >= numNodes || visited[currentTileIndex])
                continue;

            visited[currentTileIndex] = true;

            for (int i = 0; i < 4; i++) {
                if (tileAdjacencyList[currentTileIndex][i] == 1) {
                    newIndex = getNewIndex(currentTileIndex, i, width);
                    if (newIndex < visited.length && newIndex > 0){
                        if (!visited[newIndex]) {
                            queue.offer(newIndex);
                            visitedTilesIndex.add(newIndex);
                        }
                    }
                }
            }
        }

        return visitedTilesIndex;
    }

    /***
     * gets the index of a target tile in a given direction from a reference tile
     * @param currentIndex the index of the reference tile
     * @param direction the direction of the target tile
     * @param width the width of the tileMap
     * @return the index of the target tile
     */
    /*This is new*/
    private int getNewIndex(int currentIndex, int direction, int width){
        switch(direction){
            case 0:
                return currentIndex - 1;
            case 1:
                return currentIndex + 1;
            case 2:
                return currentIndex - width;
            case 3:
                return currentIndex + width;
            default:
                return 0;
        }
    }

    /***
     * checks if a tile adjacent to a given tile exists and is unoccupied
     * @param centreTile the tile to be used as reference
     * @param xOffset the difference in x position of the two tiles
     * @param yOffset the difference in y position of the two tiles
     * @param width the width of the tileMap
     * @param height the height of the tileMape
     * @return true if tile exists and is unoccupied, else false
     */
    /*This is new*/
    private boolean checkTileAdjacent(Tile centreTile, int xOffset, int yOffset, int width, int height){
        int newX = centreTile.getCol() + xOffset;
        int newY = centreTile.getRow() + yOffset;

        if (newX == width || newX < 0 || newY == height || newY < 0) {
            return false;
        }

        Tile checkTile = this.getTileAtLocation(newX, newY);

        if (checkTile != null){
            return !checkTile.isOccupied();
        }

        return false;
    }

    /***
     *returns the tile in tiles located at a given index
     * @param tileIndex the index of desired tile
     * @return the tile located at the given index
     */
    /*This is new*/
    public Tile getTileFromIndex(int tileIndex){
        if (tileIndex >= 0 && tileIndex < tiles.size()){
            return tiles.get(tileIndex);
        }
        return null;
    }

    public ArrayList<Engine> getEngines() {
        return engines;
    }
    /*This is new*/
    public int[][] getAdjacencyList(){
        return adjacencyList;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setAdjacencyList(int[][] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
