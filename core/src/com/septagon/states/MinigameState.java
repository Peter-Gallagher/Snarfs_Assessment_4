package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private TiledGameMap pipeMap;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private OrthographicCamera camera;
    //Viewport that is used alongside the camera that contains the whole game map
    private ExtendViewport viewport;
    //Spritebatch that is used for rendering all objects in the game
    private SpriteBatch objectBatch;

    private TileManager tileManager;

    public MinigameState(InputManager inputManager, BitmapFont font, StateManager stateManager)
    {
        super(inputManager, font, StateID.MINIGAME, stateManager);
        score = 0;
    }

    public void initialise()
    {
        pipeMap = new TiledGameMap("pipeShite.tmx");

        for(int row = 0; row < pipeMap.getMapHeight(); row++)
        {
            for(int col = 0; col < pipeMap.getMapWidth(); col++)
            {
                if(pipeMap.getTileByCoordinate(0, col, row) != null)
                    tiles.add(pipeMap.getTileByCoordinate(0, col, row));
            }
        }

        tileManager = new TileManager(tiles);
        createPipeAdjacencyList(pipeMap.getMapWidth(),pipeMap.getMapHeight());

        initializeCamera();
    }

    private void initializeCamera(){
        // Intialises the game viewport and spritebatch
        //viewport = new ExtendViewport(1280, 720, camera);
        //objectBatch = new SpriteBatch();
        //objectBatch.setProjectionMatrix(camera.combined);

        //Sets up the camera parameters and moves it to its inital position
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);
        camera.zoom = 0.3f;

        //camera.setToOrtho(false, width, height);
        camera.position.x = 80;
        camera.position.y = 70;
        camera.update();
    }

    public void update()
    {
    }

    public void render(SpriteBatch batch)
    {

        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        pipeMap.render(camera);
    }

    public void dispose(){

    }

    public void rotateTile(Tile tileToRotate){

        //pipeMap.getTileByCoordinate(0, tileToRotate.getCol(),tileToRotate.getRow());

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
        updateAdjacencyMatrix(tileIndex, adjacencyDirections);

    }


    public void createPipeAdjacencyList(int maxWidth, int maxHeight){

        Tile checkTile;
        TiledMapTileLayer pipeLayer = pipeMap.getTileLayer(0);
        adjacencyList = new int[maxWidth * maxHeight][4];

        for (int width = 0; width < maxWidth; width++) {
            for (int height = 0; height < maxHeight; height++) {
                TiledMapTileLayer.Cell cell = pipeLayer.getCell(width, height);
                String[] adjDir = ((String) cell.getTile().getProperties().get("AdjacentDirections")).split(",");

                int[] adjacencyDirections = new int[adjDir.length];
                for (int i = 0; i < adjDir.length; i++){
                    adjacencyDirections[i] = Integer.parseInt(adjDir[i]);
                }

                int sourceIndex = width + (maxWidth * height);
                for (int adjacencyDirection : adjacencyDirections) {
                    int targetIndex = sourceIndex + getOffset(adjacencyDirection);
                    if (targetIndex > 0 && targetIndex < (maxWidth * maxHeight)){
                        adjacencyList[sourceIndex][getRelativePosition(sourceIndex,targetIndex)] = 1;
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
            case - 1:
                return 1;
            case 5:
                return 2;
            case - 5:
                return 3;
            default:
                return 0;
        }
    }

    public void updateAdjacencyMatrix(int sourceIndex, int[] pipeEndPositions){
        int oppositePipePosition;
        int newPipeEndPosition;
        int targetIndex;
        int newTargetIndex;

        for(int i = 0; i < pipeEndPositions.length; i++){
        targetIndex = sourceIndex + getOffset(pipeEndPositions[i]);
        oppositePipePosition = (pipeEndPositions[i] + 2) % 4;
        adjacencyList[sourceIndex][pipeEndPositions[i]] = 0;
        //adjacencyList[targetIndex][oppositePipePosition] = 0;

        newPipeEndPosition = (pipeEndPositions[i]+ 1) % 4;
        newTargetIndex = sourceIndex + getOffset(newPipeEndPosition);
        oppositePipePosition = (newPipeEndPosition + 2) % 4;
        adjacencyList[sourceIndex][pipeEndPositions[i]] = 1;
        //adjacencyList[newTargetIndex][oppositePipePosition] = 1;
        }

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


    public void handleInputForMinigame(float xCoord, float yCoord) {
        Tile tileClicked = getTileClicked(xCoord,yCoord);
        System.out.println(xCoord);
        System.out.println(yCoord);

        if (tileClicked != null){
            System.out.println("IT WORKED");
            rotateTile(tileClicked);
        }

        GameState state = (GameState)stateManager.getState(StateID.GAME);
        Tile tile = state.getAttackerManager().getTileClicked(xCoord, yCoord);
    }

    private void returnToMainGame() {}

    public Tile getTileClicked(float x, float y){
        for(Tile tile: tiles) {
            if(tile.checkIfClickedInside(x, y)){
                return tile;
            }
        }
        return null;
    }
}
