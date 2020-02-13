package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;
//import com.sun.tools.javac.comp.Todo;

import java.awt.*;

/**
Child of the State class that will be used to manage the system when the user has reached game over
 */

public class GameOverState extends State
{
    private GlyphLayout gameOverLabel;
    private GlyphLayout extraInfoLabel;
    private GlyphLayout playAgainLabel;
    private GlyphLayout yesLabel;
    private GlyphLayout noLabel;

    private int gameOverX, gameOverY, extraInfoX, extraInfoY, playAgainX, playAgainY, yesX, yesY, noX, noY;

    private boolean didWin = false;
    private SpriteBatch gameOverSpriteBatch;

    private int position = 1;
    private Rectangle yesBox;
    private Rectangle noBox;

    public GameOverState(InputManager inputManager, BitmapFont font, StateManager stateManager, boolean didWin)
    {
        super(inputManager, font, StateID.GAME_OVER, stateManager);
        this.didWin = didWin;
    }

    /**
     * Sets up all text and positions
     */
    public void initialise()
    {
        gameOverLabel = new GlyphLayout();
        extraInfoLabel = new GlyphLayout();
        playAgainLabel = new GlyphLayout();
        yesLabel = new GlyphLayout();
        noLabel = new GlyphLayout();
        gameOverSpriteBatch = new SpriteBatch();

        //If the user won, tell them they won if not tell them they lost
        if(didWin){
            gameOverLabel.setText(font, "Congrats, you win!");
            extraInfoLabel.setText(font, "You destroyed all the ET Fortresses and saved York!");
        }else {
            gameOverLabel.setText(font, "Game Over! You Lose.");
            extraInfoLabel.setText(font, "All your fire engines were destroyed!");
        }

        //Setup the text for the play Again section
        playAgainLabel.setText(font, "Play Again?");
        font.setColor(Color.BLUE);
        yesLabel.setText(font, "Yes");
        font.setColor(Color.WHITE);
        noLabel.setText(font, "No");

        //Setup positions for all text on the screen
        gameOverX = (int)((Gdx.graphics.getWidth() / 2) - gameOverLabel.width / 2);
        gameOverY = (int)((Gdx.graphics.getHeight() / 2) + gameOverLabel.height + 100);
        extraInfoX = (int)((Gdx.graphics.getWidth() / 2) - extraInfoLabel.width / 2);
        extraInfoY = (Gdx.graphics.getHeight() / 2) + 70;
        playAgainX = (int)((Gdx.graphics.getWidth() / 2) - playAgainLabel.width / 2);
        playAgainY = (Gdx.graphics.getHeight() / 2) + 20;
        yesX = (int)((Gdx.graphics.getWidth() / 2) - yesLabel.width / 2);
        yesY = (Gdx.graphics.getHeight() / 2) - 60;
        noX = (int)((Gdx.graphics.getWidth() / 2) - noLabel.width / 2);
        noY = (Gdx.graphics.getHeight() / 2) - 110;

        setupRectangles();
    }

    //Todo Smelly code, empty method
    //Unused method that is needed since we are a child class of State
    public void update()
    {
    }

    /**
     * Used to draw all the text to the screen
     * @param batch The batch which is used for rendering game to screen (not used in this class)
     */
    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor(50.0f/255.0f, 205.0f/255.0f, 50.0f/255.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameOverSpriteBatch.begin();

        font.setColor(Color.WHITE);

        //TODO redundant win message
        if(didWin){
            gameOverLabel.setText(font, "Congrats, you win!");
            extraInfoLabel.setText(font, "You destroyed all the ET Fortresses and saved York!");
        }
        playAgainLabel.setText(font, "Play Again?");

        font.draw(gameOverSpriteBatch, gameOverLabel, gameOverX, gameOverY);
        font.draw(gameOverSpriteBatch, extraInfoLabel, extraInfoX, extraInfoY);
        font.draw(gameOverSpriteBatch, playAgainLabel, playAgainX, playAgainY);
        this.setTextColour(1, yesLabel, "Yes");
        font.draw(gameOverSpriteBatch, yesLabel, yesX, yesY);
        this.setTextColour(2, noLabel, "No");
        font.draw(gameOverSpriteBatch, noLabel, noX, noY);

        gameOverSpriteBatch.end();
    }

    /**
     * Method used to dispose of the SpriteBatch
     */
    public void dispose(){
        gameOverSpriteBatch.dispose();
    }

    /**
     * Sets the text colour of the yes and no text which may switch colours depending on inputs
     * @param currentPosition The position of the text in the play again menu
     * @param currentLabel The layout being used to render the text
     * @param text The actual string containing the text to be renderer
     */
    //TODO redundant? unclear on purpose of method
    private void setTextColour(int currentPosition, GlyphLayout currentLabel, String text){
        //If the menu should be on the current text, draw it blue if not white
        if(currentPosition == this.position){
            font.setColor(Color.BLUE);
            currentLabel.setText(font, text);
        }
        else{
            font.setColor(Color.WHITE);
            currentLabel.setText(font, text);
        }
    }

    /**
     * Setup the positions of the rectangles which are used to handle mouse input
     */
    private void setupRectangles(){
        yesX = (int)((Gdx.graphics.getWidth() / 2) - yesLabel.width / 2);
        yesY = (Gdx.graphics.getHeight() / 2) - 60;
        noX = (int)((Gdx.graphics.getWidth() / 2) - noLabel.width / 2);
        noY = (Gdx.graphics.getHeight() / 2) - 110;

        yesBox = new Rectangle();
        noBox = new Rectangle();

        yesBox.setBounds(yesX - 20, yesY - 20, 100, 30);
        noBox.setBounds(noX - 20, noY - 20, 100, 30);
    }

    /**
     * Called when mouse input happens and checks if the user has pressed either yes or no buttons
     * @param x The x position of the input
     * @param y The y position of the input
     */
    public void checkIfButtonPressed(float x, float y){
        if(x >= yesBox.x && x <= yesBox.x + yesBox.width && y >= yesBox.y && y <= yesBox.y + yesBox.height){
            stateManager.changeState(new GameState(inputManager, font, stateManager, inputManager.getCamera()));
        }else if(x >= noBox.x && x <= noBox.x + noBox.width && y >= noBox.y && y <= noBox.y + noBox.height){
            Gdx.app.exit();
        }
    }

    //Getters
    public int getPosition(){ return position; }

    //Setters
    public void setPosition(int position) { this.position = position; }
}
