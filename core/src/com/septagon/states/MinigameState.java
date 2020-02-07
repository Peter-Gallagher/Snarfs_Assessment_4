package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.septagon.entites.Tile;
import com.septagon.entites.TiledGameMap;
import com.septagon.game.InputManager;

import java.util.ArrayList;

/**
Child of State class that will be used to manage the system when the user is playing the minigame
 */
//TODO Empty minigame class, implement a minigame here
public class MinigameState extends State 
{
    //Used to keep track of the score in the minigame
    private int score;
    private TiledGameMap pipeMap;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private OrthographicCamera camera;
    //Viewport that is used alongside the camera that contains the whole game map
    private ExtendViewport viewport;
    //Spritebatch that is used for rendering all objects in the game
    private SpriteBatch objectBatch;

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

        initializeCamera();
    }

    private void initializeCamera(){
        // Intialises the game viewport and spritebatch
        viewport = new ExtendViewport(1280, 720, camera);
        objectBatch = new SpriteBatch();
        //objectBatch.setProjectionMatrix(camera.combined);

        //Sets up the camera parameters and moves it to its inital position
        //camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.x = pipeMap.getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2;
        camera.update();
    }

    public void update()
    {
    }

    public void render(SpriteBatch batch)
    {

        //Clear the background to red - the colour does not really matter
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        pipeMap.render(camera);
    }

    public void dispose(){

    }

    public void rotateTile(){

    }

    public void handleInputForMinigame() {
        float xCoord = Gdx.input.getX();
        float yCoord = Gdx.input.getY();

        //Convert input coords to screen coords
        xCoord = xCoord + camera.position.x - (Gdx.graphics.getWidth() / 2);
        yCoord = (Gdx.graphics.getHeight() - yCoord) + camera.position.y - (Gdx.graphics.getHeight() / 2);

    }

    private void returnToMainGame() {}
}
