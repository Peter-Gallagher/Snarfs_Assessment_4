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




    public TileManager(ArrayList<Engine> engines, ArrayList<Tile> tiles){
        this.engines = engines;
        this.tiles = tiles;
        adjacencyList = new int[tiles.size()][4];
    }

    public TileManager(ArrayList<Tile> tiles){
        this.tiles = tiles;
        adjacencyList = new int[tiles.size()][4];
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
     * Clear all the current moveable tiles
     */
    public void resetMovableTiles(){
        for(Tile tile: tiles)
        {
            tile.setMovable(false);
        }
    }

    /***
     * Sets up the tiles which contain an engine, fortress or the station to be occupied
     */
    //TODO there must be a better way to initialise the Fortresses than hard coding each
    public void setOccupiedTiles(TiledGameMap gameMap)
    {
        int width = gameMap.getMapWidth();
        int height = gameMap.getMapHeight();
        setEngineTilesOccupied();

        boolean[][]  passableTiles = gameMap.getPassable();

        for (int i = 0; i < passableTiles.length - 1; i++){
            for (int j = 0; j < passableTiles[0].length - 1; j++){
                Tile tile = getTileAtLocation(j, i, width, height);
                tile.setOccupied(passableTiles[i][j]);
            }
        }


        //Set the all the tiles within the fire station fortress bounds as occupied
        for (int x = 34; x < 42; x++)
        {
            for (int y = 10; y < 15; y++)
            {
                Tile t = getTileAtLocation(x, y, width, height);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles within the minister fortress as occupied
        for (int x = 41; x < 49; x++)
        {
            for (int y = 41; y < 48; y++)
            {
                Tile t = getTileAtLocation(x, y, width, height);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles within the station fortress as occupied
        for (int x = 71; x < 79; x++)
        {
            for (int y = 30; y < 34; y++)
            {
                Tile t = getTileAtLocation(x, y, width, height);
                if (t != null)
                    t.setOccupied(true);
            }
        }

        //Sets all the tiles in the fire station as occupied
        for (int x = 72; x < 80; x++)
        {
            for (int y = 6; y < 10; y++)
            {
                Tile t = getTileAtLocation(x, y, width, height);
                if (t != null)
                    t.setOccupied(true);
            }
        }
    }


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


    public void updateTileInAdjacencyList(int tileIndex, int access){
            try {
            adjacencyList[tileIndex - 1][1] = access;
            adjacencyList[tileIndex + 1][0] = access;
            adjacencyList[tileIndex + 80][2] = access;
            adjacencyList[tileIndex - 80][3] = access;
        } catch (Exception e){
            System.out.println("asdasdasdasd");
        }

        }


    /***
     * Method to get the tile at a row and column
     * @param col The column of the tile you want to get
     * @param row The row of the tile you want to get
     * @return The tile at the location asked for
     */
    public Tile getTileAtLocation(int col, int row, int width, int height)
    {
        int tileIndex = col + (row * width);

        if (tileIndex >= 0 && tileIndex < (width * height)){
            Tile tile = tiles.get(tileIndex);
            return  tile;
        }else{
            return null;
        }
    }

    /***
     * Get the movable tiles for all the engines based on their positions
     */

    public void setMovableTiles(Engine currentEngine) {
        //Reset all moveable tiles from previous turn
        resetMovableTiles();
        setEngineTilesOccupied();
        Tile accessTile;

        int startTileIndex = currentEngine.getCol() + (currentEngine.getRow() * 80);

        for (Integer index: BFS(adjacencyList, startTileIndex, 80, currentEngine.getSpeed())) {
            accessTile = this.getTileAtLocation(index % 80, index / 80, 80, 50);
            accessTile.setMovable(true);
        }
    }

    //TODO: should be called on startup and then updated when fire engines move
    public void createAdjacencyList(int maxWidth, int maxHeight){

        Tile checkTile;

        for (int width = 0; width <= maxWidth; width++) {
            for (int height = 0; height <= maxHeight; height++) {

                checkTile = this.getTileAtLocation(width, height,maxWidth, maxHeight);
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
                if(depth > maxDepth || qVal == null){
                    break;
                }
            }

            int currentTileIndex = qVal;

            if (currentTileIndex < 0 || currentTileIndex >= numNodes || visited[currentTileIndex])
                continue;

            visited[currentTileIndex] = true;

            //TODO: can probably be done better
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

    private int getNewIndex(int currentIndex, int i, int width){
        switch(i){
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


    private boolean checkTileAdjacent(Tile centreTile, int xOffset, int yOffset, int width, int height){
        int newX = centreTile.getCol() + xOffset;
        int newY = centreTile.getRow() + yOffset;

        if (newX == width || newX < 0 || newY == height || newY < 0) {
            return false;
        }

        Tile checkTile = this.getTileAtLocation(newX, newY, width, height);

        if (checkTile != null){
            return !checkTile.isOccupied();
        }


        return false;
    }

    public Tile getTileFromIndex(int tileIndex){
        if (tileIndex >= 0 && tileIndex < tiles.size()){
            return tiles.get(tileIndex);
        }
        return null;
    }

    public int[][] getAdjacencyList(){
        return adjacencyList;
    }
}
