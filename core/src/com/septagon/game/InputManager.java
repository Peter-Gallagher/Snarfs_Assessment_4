package com.septagon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.septagon.entites.Engine;
import com.septagon.entites.Tile;
import com.septagon.helperClasses.SaveManager;
import com.septagon.states.*;

import java.util.ArrayList;

/**
Class used to handle all inputs from the user
 */

public class InputManager implements InputProcessor
{

    private boolean dragging;
    private OrthographicCamera camera;
    private StateManager stateManager;

    private BitmapFont font;
    private SpriteBatch batch;

    //Variables that keep track of what input has occurred and where
    private boolean hasBeenTouched = false;
    private float xCoord;
    private float yCoord;


    public InputManager(OrthographicCamera camera, StateManager stateManager, BitmapFont font, SpriteBatch batch)
    {
        this.camera = camera;
        this.stateManager = stateManager;
        this.font = font;
        this.batch = batch;
    }


    /**
     * Usused method that is required since we are implementing InputProcessor
     */
    @Override public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the user presses the screen with the mouse
     * @param screenX The x position of the touch
     * @param screenY The y position of the touch
     * @param button The button that the input occurred with
     * @return
     */
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //checks if the game is in the main game state

        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            return gameClickInput(pointer, button);
        }
        else if(stateManager.getCurrentState().getID() == State.StateID.MENU){
            menuClickInput(screenX, screenY);
        }
        else if(stateManager.getCurrentState().getID() == State.StateID.GAME_OVER){
            gameOverClickInput(screenX, screenY);
        }
        else if(stateManager.getCurrentState().getID() == State.StateID.MINIGAME){
            miniGameClickInput();
        }
        return true;
    }

    /***
     * Method that handles input from mouse click while in Game state
     * @param pointer
     * @param button The button that the input occurred with
     * @return true if input was a left click, else false
     */
    /*This is new*/
    private boolean gameClickInput(int pointer, int button){
        //Cast the currentState to a gameState so that gameState specific methods can be used
        GameState currentState = (GameState)stateManager.getCurrentState();

        if(!currentState.isPaused() && currentState.isPlayerTurn())
        {
            // ignore if its not left mouse button or first touch pointer
            if (button != Input.Buttons.LEFT || pointer > 0) return false;

            //Get the positions of the input in terms of screen coords
            hasBeenTouched = true;
            xCoord = Gdx.input.getX();
            yCoord = Gdx.input.getY();

            //Convert input coords to screen coords
            xCoord = xCoord + camera.position.x - (Gdx.graphics.getWidth() / 2);
            yCoord = (Gdx.graphics.getHeight() - yCoord) + camera.position.y - (Gdx.graphics.getHeight() / 2);

            //Create versions of the input that are kept in terms of screen coords
            float onScreenXCoord = Gdx.input.getX();
            float onScreenYCoord = Gdx.graphics.getHeight() - Gdx.input.getY();

            //Check if any of the UI elements have been pressed
            if (currentState.getUiManager().getShowStatsRect().contains(onScreenXCoord, onScreenYCoord))
            {
                currentState.getUiManager().pressedShowStatsButton();
            }
            if (currentState.getUiManager().getMinimiseRect().contains(onScreenXCoord, onScreenYCoord))
            {
                currentState.getUiManager().pressedMinimiseButton();
            }


            //call gamestate method that handles when places on the map are pressed
            currentState.getAttackerManager().touchedTile(xCoord, yCoord);

            dragging = true;
        }
        if (currentState.isPaused()){
            if (button != Input.Buttons.LEFT || pointer > 0) return false;

            hasBeenTouched = true;
            xCoord = Gdx.input.getX();
            yCoord = Gdx.input.getY();

            //Convert input coords to screen coords
            xCoord = xCoord + camera.position.x - (Gdx.graphics.getWidth() / 2);
            yCoord = (Gdx.graphics.getHeight() - yCoord) + camera.position.y - (Gdx.graphics.getHeight() / 2);

            //Create versions of the input that are kept in terms of screen coords
            float onScreenXCoord = Gdx.input.getX();
            float onScreenYCoord = Gdx.graphics.getHeight() - Gdx.input.getY();

            currentState.getUiManager().checkPausedButtonClicked(onScreenXCoord,onScreenYCoord);


        }
        return true;
    }

    /***
     * Method that handles input from mouse click while in Menu state
     * @param screenX the X coordinate that was clicked on the screen
     * @param screenY the Y coordinate that was clicked on the screen
     */
    /*This is new*/
    private void menuClickInput(int screenX, int screenY){
        //Cast the currentState to menuState so menuState specific methods can be used
        MenuState currentState = (MenuState) stateManager.getCurrentState();

        //Call menustate method that processes a mouse press
        currentState.checkIfClickedOption(screenX, Gdx.graphics.getHeight() - screenY);
    }

    /***
     * Method that handles input from mouse click while in gameOver state
     * @param screenX the X coordinate that was clicked on the screen
     * @param screenY the Y coordinate that was clicked on the screen
     */
    /*This is new*/
    private void gameOverClickInput(int screenX, int screenY){
        //Casts the currentState to a gameOverState so gameOverState specific methods can be used
        GameOverState currentState = (GameOverState) stateManager.getCurrentState();

        //Call gameOverState method that processes a mouse press
        currentState.checkIfButtonPressed(screenX, Gdx.graphics.getHeight() - screenY);
    }

    /***
     * Method that handles input from mouse click while in miniGame state
     */
    /*This is new*/
    public void miniGameClickInput(){
        MinigameState currentState = (MinigameState) stateManager.getCurrentState();

        //Get the positions of the input in terms of screen coords
        xCoord = Gdx.input.getX();
        yCoord = Gdx.input.getY();

        //Convert input coords to screen coords
        xCoord = (xCoord - (Gdx.graphics.getWidth() / 2)) * 0.3f   + 80;
        xCoord = xCoord / (Gdx.graphics.getWidth() / 1280);


        yCoord = (Gdx.graphics.getHeight() - yCoord - (Gdx.graphics.getHeight() / 2)) * 0.3f + 70;
        yCoord = yCoord / (Gdx.graphics.getHeight() / 720);

        currentState.handleInputForMinigame(xCoord,yCoord);

    }


    /**
     * Checks if the user has performed a drag action on the screen
     * @param screenX The x position of where the drag finished
     * @param screenY The y position of where the drag finished
     * @return
     */
    @Override public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            //Convert currentState to gameState so that gameState specific methods can be used
            GameState currentState = (GameState) stateManager.getCurrentState();
            if(!currentState.isPaused() && currentState.isPlayerTurn())
            {
                if (!dragging) return false;

                //Get the positions of where the camera should be moved to
                float newX = camera.position.x - Gdx.input.getDeltaX();
                float newY = camera.position.y + Gdx.input.getDeltaY();

                //Check the new positions of the camera are within the screen bounds before performing the translation
                if (newX >= Gdx.graphics.getWidth() / 2 && newX <= currentState.getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2)
                    camera.translate(-Gdx.input.getDeltaX(), 0, 0);

                if (newY >= Gdx.graphics.getHeight() / 2 && newY <= currentState.getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2)
                    camera.translate(0, Gdx.input.getDeltaY(), 0);

                //Refresh the camera
                camera.update();
                camera.unproject(new Vector3(screenX, screenY, 0));
            }
        }
        return true;
    }

    /**
     * Method called when the user releases the mouse button
     * @param screenX The x position of the input
     * @param screenY The y position of the input
     * @param button The mouse button that performed the input
     */
     @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        //System.out.println(screenX + "  ,  " + screenY);
        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            camera.unproject(new Vector3(screenX, screenY, 0));
            dragging = false;
        }
        return true;
    }



    /**
     * Method calls when the user presses a key on the keyboard
     * @param keycode The code of the key that is pressed
     */
    /*This changed*/
    @Override public boolean keyDown (int keycode)
    {
        if(stateManager.getCurrentState().getID() == State.StateID.MENU)
        {
            //Casts the currentState to a menuState so menuState specific methods can be used
            MenuState currentState = (MenuState) stateManager.getCurrentState();

            //If up or down pressed, move the menuPosition accordingly
            switch(keycode){
                case Input.Keys.DOWN:
                    currentState.setMenuPosition(currentState.getMenuPosition() + 1);
                    System.out.println("<>");
                    //System.out.println(currentState.getMenuPosition());
                    break;

                case Input.Keys.UP:
                    currentState.setMenuPosition(currentState.getMenuPosition() - 1);
                    //System.out.println(currentState.getMenuPosition());
                    break;

                case Input.Keys.LEFT:
                    if (currentState.getMenuPosition() == 1){
                        Difficulty.previousDifficulty();
                    } else if (currentState.getMenuPosition() == 2){
                        if (currentState.saveIndex == 0){
                            currentState.saveIndex = currentState.getSaveList().size() - 1;
                        } else {
                            currentState.saveIndex -= 1;
                        }
                    }

                    break;
                case Input.Keys.RIGHT:
                    if (currentState.getMenuPosition() == 1){
                        Difficulty.nextDifficulty();
                    } else if (currentState.getMenuPosition() == 2){
                        if (currentState.saveIndex == currentState.getSaveList().size() - 1){
                            currentState.saveIndex = 0;
                        } else {
                            currentState.saveIndex += 1;
                        }
                    }

                    break;
                //If enter pressed, perform action depending on the position of the menu
                case Input.Keys.ENTER:

                    switch(currentState.getMenuPosition())
                    {
                        case 0:
                            stateManager.changeState(new GameState(this, font, stateManager, camera));
                            break;
                        case 1:
                            Difficulty.nextDifficulty();
                            break;
                        case 2:
                            //Get the save currently showing in the menu, load it into game state
                            GameState loadedState = SaveManager.loadSave(currentState.getSaveList().get(currentState.saveIndex));
                            //Set up references to stateManager, ect.
                            loadedState.setStateManager(this.stateManager);
                            loadedState.setCamera(this.camera);
                            loadedState.setInputManager(this);
                            stateManager.changeState(loadedState);
                            break;
                        case 3:
                            Gdx.app.exit();
                            break;
                        default:
                            System.err.println("Something went wrong with the menu system, (No case for: " + currentState.getMenuPosition());
                            break;
                    }

                default:
                    break;
            }


        }else if(stateManager.getCurrentState().getID() == State.StateID.GAME){
            //Cast currentState to a gameState so gameState specific methods can be used
            GameState currentState = (GameState) stateManager.getCurrentState();
            int currentPausePosition = currentState.getUiManager().getPausePosition();

            //If user presses escape, flip whether the game is paused or not
            if(keycode == Input.Keys.ESCAPE){
                currentState.setPaused(!currentState.isPaused());
            }

            if(keycode == Input.Keys.S){
                SaveManager.makeNewSave(currentState);
                System.out.println("Saved!!");
            }
//            if(keycode == Input.Keys.L){
//                this.gs = SaveManager.loadMostRecentSave();
//                this.gs.setStateManager(this.stateManager);
//                this.gs.setInputManager(this);
//                this.gs.setCamera(this.camera);
//                this.stateManager.changeState(this.gs);
//            }


            if(currentState.isPaused()){

                switch (keycode){
                    //If up or down pressed, move pause position accordingly
                    case Input.Keys.DOWN:
                        if(currentPausePosition == 3){ //If at the end of pause menu
                            currentState.getUiManager().setPausePosition(1);//Then go back to top
                            System.out.println(currentState.getUiManager().getPausePosition());
                            break;
                        } else {
                            currentState.getUiManager().setPausePosition(currentState.getUiManager().getPausePosition() + 1);
                            System.out.println(currentState.getUiManager().getPausePosition());
                            break;
                        }
                    case Input.Keys.UP:
                        if(currentPausePosition == 1){//If at the top of pause menu
                            currentState.getUiManager().setPausePosition(3);//Go to bottom
                            System.out.println(currentState.getUiManager().getPausePosition());
                            break;
                        } else {
                            currentState.getUiManager().setPausePosition(currentState.getUiManager().getPausePosition() - 1);
                            System.out.println(currentState.getUiManager().getPausePosition());
                            break;
                        }
                    //If enter pressed, perform action depending on where in the pause menu the user is
                    case Input.Keys.ENTER:
                        if(currentPausePosition == 1){
                            currentState.setPaused(false);
                        }else if (currentPausePosition == 2) {
                            SaveManager.makeNewSave(currentState);
                        } else {
                            stateManager.changeState(new MenuState(this, font, stateManager, camera));
                        }
                    default:
                        break;
                }
            }
        }

        //Handle input for the game over state
        else if(stateManager.getCurrentState().getID() == State.StateID.GAME_OVER){
            //Convert the currentState variable to an instance of GameOverState
            GameOverState currentState = (GameOverState) stateManager.getCurrentState();

            //Move the position of the gameOverState up or down based on inputs
            if(keycode == Input.Keys.DOWN && currentState.getPosition() == 1){
                currentState.setPosition(2);
            }else if(keycode == Input.Keys.UP && currentState.getPosition() == 2){
                currentState.setPosition(1);
            }
            //If the enter key is pressed, perform action based on the position
            if(keycode == Input.Keys.ENTER){
                //If on yes, start a new GameState
                if(currentState.getPosition() == 1){
                    stateManager.changeState(new GameState(this, font, stateManager, camera));
                }
                //If on no, close the window and exit the game
                else if(currentState.getPosition() == 2){
                    Gdx.app.exit();
                }
            }
        }
        return true;
    }

    //Unused override methods that are required since we are implementing InputProcessor
    @Override public boolean keyUp (int keycode) {
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean scrolled (int amount) {
        return false;
    }

    //Getters
    public boolean isHasBeenTouched() { return hasBeenTouched; }
    public float getXCoord() { return xCoord; }
    public float getYCoord() { return yCoord; }
    public OrthographicCamera getCamera() { return camera; }
}