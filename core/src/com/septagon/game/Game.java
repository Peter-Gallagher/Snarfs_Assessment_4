package com.septagon.game;

/**
 * First class that will be called from the DesktopLauncher
 * Class which is used to setup everything in the game and handle
 * which state the game is in
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.septagon.states.GameState;
import com.septagon.states.MenuState;
import com.septagon.states.State;
import com.septagon.states.StateManager;

public class Game extends ApplicationAdapter
{
	private SpriteBatch batch;
	private State startState;
	private StateManager stateManager;
	private OrthographicCamera camera;
	private BitmapFont font;

	private InputManager inputManager;

	@Override
	/**
	 * Method that is automatically called when an instance of the Game class is created
	 * Will initialize all variables in the game and set up the initial state
	 */
	public void create ()
	{
		batch = new SpriteBatch();

		//Setup the camera so that it starts at (0,0)
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.update();

		loadFont();

		//Intialise all variables with default values
		stateManager = new StateManager();
		inputManager = new InputManager(camera, stateManager, font, batch);
		Gdx.input.setInputProcessor(inputManager);
		startState = new MenuState(inputManager, font, stateManager, camera);

		//Set the current state of the game to be the GameState and 
		//initialise this state
		stateManager.changeState(startState);
		stateManager.initialise();
	}

	/**
	 * Method that is used to enter new states
	 * Enter the desired state as a parameter and the state will be initialized
	 */
	public void initializeState(State newState){
		stateManager.changeState(newState);
		stateManager.initialise();
	}

	/**
	 * Method that is called exclusively by create
	 * Will load the font into the game loaded from our custom font files
	 */
	public void loadFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-";
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	/**
	 * Method that will be called whenever the user resizes the window
	 * @param width The new width of the screen
	 * @param height The new height of the screen
	 */

	public void resize(int width, int height)
	{
		//Change the width and height of the camera
		camera.viewportWidth = width;
		camera.viewportHeight = height;


		//Call the methods specific to the state of the game that will update their values
		if(stateManager.getCurrentState().getID() == State.StateID.GAME)
		{
			GameState state = (GameState) stateManager.getCurrentState();
			state.hasResized();
		}
		else if(stateManager.getCurrentState().getID() == State.StateID.MENU)
		{
			MenuState state = (MenuState) stateManager.getCurrentState();
			state.hasResized(width, height);
		}

		camera.update();
	}

	@Override
	/**
	 * Method that will render everything in the game - this gets called every frame
	 */
	public void render ()
	{
		//Updates the current state of the game
		stateManager.update();

		//Used to clear the background of the screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		//Anything between begin and end is used to render our whole game
		batch.begin();

		//Render the current state of the game
		stateManager.render(batch);

		batch.end();
	}

	@Override
	/**
	 * Used to clean up and remove all objects in the game once it has finished
	 */
	public void dispose ()
	{
		stateManager.dispose();
		batch.dispose();
		font.dispose();
	}
}