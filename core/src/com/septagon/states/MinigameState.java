package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.septagon.entites.Tile;
import com.septagon.entites.TiledGameMap;
import com.septagon.game.InputManager;
import com.septagon.helperClasses.TileManager;

import java.util.ArrayList;

/**
Child of State class that will be used to manage the system when the user is playing the minigame
 */
public class MinigameState extends State
{
    //Used to keep track of the score in the minigame
    private int score;
    private int[][] adjacencyList;
    int[] terminalTileIndex = new int[2];
    private TiledGameMap pipeMap;
    private TiledGameMap backGround;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private OrthographicCamera camera;
    private OrthographicCamera cameraBackground;
    private Integer[][] pipeAdjacencyDirections;
    private TileManager tileManager;

    public MinigameState(InputManager inputManager, BitmapFont font, StateManager stateManager)
    {
        super(inputManager, font, StateID.MINIGAME, stateManager);
        score = 0;
    }

    public void initialise()
    {
        pipeMap = new TiledGameMap("MiniGame2.tmx");
        backGround = new TiledGameMap("MinigameBackground.tmx");

        for(int row = 0; row < pipeMap.getMapHeight(); row++)
        {
            for(int col = 0; col < pipeMap.getMapWidth(); col++)
            {
                if(pipeMap.getTileByCoordinate(0, col, row) != null)
                    tiles.add(pipeMap.getTileByCoordinate(0, col, row));
            }
        }

        pipeAdjacencyDirections = new Integer[tiles.size()][4];

        tileManager = new TileManager(tiles);
        createPipeAdjacencyList(pipeMap.getMapWidth(),pipeMap.getMapHeight());

        initializeCamera();
    }

    private void initializeCamera(){

        //Sets up the camera parameters and moves it to its inital position
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);
        camera.zoom = 0.3f;

        camera.position.x = 80;
        camera.position.y = 70;
        camera.update();

        cameraBackground = new OrthographicCamera(width, height);
        cameraBackground.zoom = 0.29f;

        cameraBackground.position.x = 192;
        cameraBackground.position.y = 115;
        cameraBackground.update();
    }


    public void update()
    {
    }

    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //final Texture tex = new Texture(Gdx.files.internal("images/fuck.png"));
        //batch.draw(tex, -0, -0, 0, 0, 1280, 720);

        backGround.render(cameraBackground);
        pipeMap.render(camera);
    }

    public void dispose(){

    }


    public void rotateTile(Tile tileToRotate){
        TiledMapTileLayer pipeLayer = pipeMap.getTileLayer(0);

        TiledMapTileLayer.Cell cell = pipeLayer.getCell(tileToRotate.getCol(),tileToRotate.getRow());
        int newRotation = (cell.getRotation() + 1) % 4;
        cell.setRotation(newRotation);

        String[] adjDir = ((String) cell.getTile().getProperties().get("AdjacentDirections")).split(",");
        int[] adjacencyDirections = new int[adjDir.length];
        for (int i = 0; i < adjDir.length; i++){
            adjacencyDirections[i] = Integer.parseInt(adjDir[i]);
        }

        int tileIndex = tileToRotate.getCol() + (pipeLayer.getWidth() * tileToRotate.getRow());
        updateAdjacencyMatrix(tileIndex);
    }


    public void createPipeAdjacencyList(int maxWidth, int maxHeight){
        int terminalIndex = 0;
        int currentIndex;
        Integer currentDirectionValue;

        TiledMapTileLayer pipeLayer = pipeMap.getTileLayer(0);
        adjacencyList = new int[maxWidth * maxHeight][4];

        for (int width = 0; width < maxWidth; width++) {
            for (int height = 0; height < maxHeight; height++) {
                currentIndex = width + (height * maxWidth);
                TiledMapTileLayer.Cell cell = pipeLayer.getCell(width, height);
                String[] adjDir = ((String) cell.getTile().getProperties().get("AdjacentDirections")).split(",");


                int[] adjacencyDirections = new int[adjDir.length];
                for (int i = 0; i < adjDir.length; i++){
                    pipeAdjacencyDirections[currentIndex][i] = Integer.parseInt(adjDir[i]);
                    adjacencyDirections[i] = Integer.parseInt(adjDir[i]);
                }

                int sourceIndex = width + (maxWidth * height);

                for (int i = 0; i < pipeAdjacencyDirections[0].length; i++){
                    currentDirectionValue = pipeAdjacencyDirections[currentIndex][i];
                    if (currentDirectionValue != null){
                        int targetIndex = sourceIndex + getOffset(currentDirectionValue);
                        int indexDifference = sourceIndex - targetIndex;
                        boolean changeRow = ((currentDirectionValue == 0 && indexDifference == 1 && (sourceIndex % 5) == 0)
                                || (currentDirectionValue == 1 && indexDifference == -1 && (sourceIndex % 5) == 4));

                        if (targetIndex > 0 && targetIndex < (maxWidth * maxHeight) && ! changeRow){
                            adjacencyList[sourceIndex][getRelativePosition(sourceIndex,targetIndex)] = 1;
                        }

                    }
                }

                if((boolean) cell.getTile().getProperties().get("Terminal")){
                    terminalTileIndex[terminalIndex] = sourceIndex;
                    terminalIndex++;
                }
            }
        }
        ensureTwoWayRelationship();
    }


    private void ensureTwoWayRelationship(){
        TiledMapTileLayer pipeLayer = pipeMap.getTileLayer(0);
        int direction;
        int targetIndex;

        for (int sourceIndex = 0; sourceIndex < adjacencyList.length; sourceIndex++){
            int cellX = sourceIndex % pipeMap.getMapWidth();
            int cellY = sourceIndex / pipeMap.getMapWidth();

            TiledMapTileLayer.Cell cell = pipeLayer.getCell(cellX, cellY);
            String[] adjDir = ((String) cell.getTile().getProperties().get("AdjacentDirections")).split(",");

            for (int i = 0; i < adjDir.length; i++){
                direction = Integer.parseInt(adjDir[i]);
                if(checkConnetcsToPipe(sourceIndex, direction)){
                    adjacencyList[sourceIndex][direction] = 1;
                    //targetIndex = sourceIndex + getOffset(direction);
                    //adjacencyList[targetIndex][getRelativePosition(sourceIndex, targetIndex)] = 1;
                }else{
                    adjacencyList[sourceIndex][direction] = 0;
                    targetIndex = sourceIndex + getOffset(direction);
                    if(targetIndex >= 0 && targetIndex < adjacencyList.length){
                        adjacencyList[targetIndex][getRelativePosition(targetIndex, sourceIndex)] = 0;
                    }
                }
            }

        }

    }


    private int getRelativePosition(int sourceIndex, int targetIndex){
        int indexDifference = sourceIndex - targetIndex;
        switch (indexDifference){
            case 1:
                return 0;
            case -1:
                return 1;
            case 5:
                return 2;
            case -5:
                return 3;
            default:
                return 0;
        }
    }


    public void updateAdjacencyMatrix(int sourceIndex){
        int targetIndex;

        Integer[] newPipeAdjacencyDirections = new Integer[4];

        for(int i = 0; i < 4; i++){
            if (rotationIncrement(pipeAdjacencyDirections[sourceIndex][i]) != null){
                newPipeAdjacencyDirections[i] = rotationIncrement(pipeAdjacencyDirections[sourceIndex][i]) % 4;
            }
        }

        for (Integer adjacencyDirection : pipeAdjacencyDirections[sourceIndex]) {
            if (adjacencyDirection != null){
                targetIndex = sourceIndex + getOffset(adjacencyDirection);
                if(targetIndex >= 0 && targetIndex < adjacencyList.length){
                    adjacencyList[sourceIndex][adjacencyDirection] = 0;
                    adjacencyList[targetIndex][getRelativePosition(targetIndex, sourceIndex)] = 0;
                }
            }
        }

        for (Integer newAdjacencyDirection : newPipeAdjacencyDirections) {
            if (newAdjacencyDirection != null){
                targetIndex = sourceIndex + getOffset(newAdjacencyDirection);

                if(targetIndex >= 0 && targetIndex < adjacencyList.length){
                    for (Integer direction: pipeAdjacencyDirections[targetIndex]) {
                        if (direction != null){
                            if (direction == getRelativePosition(targetIndex, sourceIndex)){
                                adjacencyList[sourceIndex][newAdjacencyDirection] = 1;
                                adjacencyList[targetIndex][getRelativePosition(targetIndex, sourceIndex)] = 1;
                            }
                        }
                    }
                }
            }
        }

        pipeAdjacencyDirections[sourceIndex] = newPipeAdjacencyDirections;
    }


    private int getOffset(int direction){
        switch(direction){
            case 0:
                return -1;
            case 1:
                return 1;
            case 2:
                return - 5;
            case 3:
                return 5;
            case 4:
                return -1;
            default:
                return 0;
        }
    }


    private Integer rotationIncrement(Integer currentDirection){

        if (currentDirection == null){
            return null;
        }

        switch (currentDirection){
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 1;
            case 3:
                return 0;
            default:
                return currentDirection;
        }
    }


    public void handleInputForMinigame(float xCoord, float yCoord) {
        Tile tileClicked = getTileClicked(xCoord,yCoord);
        boolean isNonTerminal = false;
        TiledMapTileLayer pipeLayer = pipeMap.getTileLayer(0);
        try{
            TiledMapTileLayer.Cell cell = pipeLayer.getCell(tileClicked.getCol(), tileClicked.getRow());
            isNonTerminal = !(boolean) cell.getTile().getProperties().get("Terminal");
        }catch(Exception e){

        }

        if (tileClicked != null && isNonTerminal){
            rotateTile(tileClicked);
        }

        if(checkPuzzleComplete()){
            returnToMainGame();
        }
    }


    public boolean checkPuzzleComplete(){
        ArrayList<Integer> arr;
        arr = tileManager.BFS(adjacencyList, terminalTileIndex[0],5, 50);

        return arr.contains(terminalTileIndex[1]);
    }


    private boolean checkConnetcsToPipe(int sourceIndex, int pipeEndPosition){
        int targetIndex = sourceIndex + getOffset(pipeEndPosition);
        int indexDifference = sourceIndex - targetIndex;
        boolean connectionExists = false;
        boolean changeRow = ((pipeEndPosition == 0 && indexDifference == 1 && (sourceIndex % 5) == 0)
                                || (pipeEndPosition == 1 && indexDifference == -1 && (sourceIndex % 5) == 4));


        if (targetIndex >= 0 && targetIndex < adjacencyList.length){
            if (indexDifference != 0 && !changeRow){
                for (Integer pipeAdjacencyDirection : pipeAdjacencyDirections[targetIndex]) {
                    if(pipeAdjacencyDirection != null){
                        if (pipeAdjacencyDirection == getRelativePosition(targetIndex, sourceIndex)){
                            connectionExists = true;
                            break;
                        }
                    }
                }
            }
        }

        return connectionExists;
    }


    private void returnToMainGame() {
        stateManager.changeToGameState();
    }


    public Tile getTileClicked(float x, float y){
        for(Tile tile: tiles) {
            if(tile.checkIfClickedInside(x, y)){
                return tile;
            }
        }
        return null;
    }
}
