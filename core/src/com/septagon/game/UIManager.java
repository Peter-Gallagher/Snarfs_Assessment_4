package com.septagon.game;

/**
Class that is used for rendering and managing all the heads up display
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.septagon.entites.Engine;
import com.septagon.helperClasses.SaveManager;
import com.septagon.states.GameState;
import com.septagon.states.MenuState;
import com.septagon.states.StateManager;

import java.awt.*;

public class UIManager
{
    //Create variables for all the different font variants that will be used
    private BitmapFont font;
    private BitmapFont smallFont;

    //Create the renderer for rendering all the gui shapes and the spritebatch for rendering all the gui
    private ShapeRenderer engineStatsRenderer;
    private SpriteBatch uiBatch;

    //Create objects for all the pieces of text that will be rendered to the screen
    private GlyphLayout playerTurnText;
    private GlyphLayout enemyTurnText;
    private GlyphLayout showEngineStatsText;
    private GlyphLayout maxVolumeText;
    private GlyphLayout healthText;
    private GlyphLayout damageText;
    private GlyphLayout rangeText;
    private GlyphLayout speedText;
    private GlyphLayout minimiseSymbol;
    private GlyphLayout pauseText;
    private GlyphLayout resumeText;
    private GlyphLayout saveText;
    private GlyphLayout exitText;

    //Create objects of the current instance of gamestate and for the currently pressed engine
    private GameState gameState;
    private Engine currentEngine;
    private InputManager inputManager;
    private StateManager stateManager;
    private OrthographicCamera camera;

    //Booolean that will tell if the stats menu is open or closed
    private boolean displayingStats = false;

    //Position variables for all gui elements
    private int showRectX, showRectY, showRectWidth, showRectHeight;
    private int statsRectX, statsRectY, statsRectWidth, statsRectHeight;
    private int minimiseX, minimiseY, minimiseWidth, minimiseHeight;
    private float playerTurnX, playerTurnY, enemyTurnX, enemyTurnY;
    private int pauseRectX, pauseRectY, pauseRectWidth, pauseRectHeight;
    private int pauseTextX, pauseTextY, resumeTextX, resumeTextY, saveTextX, saveTextY, exitTextX, exitTextY;

    //Variables to keep track of the pause state
    private boolean paused = false;
    private int pausePosition = 1;
    private Rectangle resumeBox;
    private Rectangle saveBox;
    private Rectangle exitBox;

    public UIManager(GameState gameState, BitmapFont font, StateManager stateManager, InputManager inputManager, OrthographicCamera camera)
    {
        this.gameState = gameState;
        this.font = font;
        this.stateManager = stateManager;
        this.inputManager = inputManager;
        this.camera = camera;
    }


    /***
     * Sets up all the variables
     */

    public void initialise()
    {
        engineStatsRenderer = new ShapeRenderer();
        uiBatch = new SpriteBatch();

        //Generate font from font file and create all the varients of it
        generateFont();

        //Set up all the text objects
        setUpStatsText();

        //Sets up text for the pause menu text
        setUpPauseText();

        //Sets up positions for all text on the screen
        setupPositions();

        //Sets up the pause menu textbox
        setupRectanglePositions();
    }

    private void generateFont(){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.GREEN;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-";
        smallFont = generator.generateFont(parameter);
        generator.dispose();
        smallFont.getData().setScale(Gdx.graphics.getWidth() / GameState.VP_WIDTH, Gdx.graphics.getHeight() / GameState.VP_HEIGHT);

        font.setColor(Color.BLUE);
        playerTurnText = new GlyphLayout(font, "Your Turn");
        font.setColor(Color.RED);
        enemyTurnText = new GlyphLayout(font, "Enemy Turn");
    }

    /*This is new*/
    private void setUpStatsText(){
        showEngineStatsText = new GlyphLayout(smallFont, "Show Stats");
        maxVolumeText = new GlyphLayout(smallFont, "Max Volume: 0");
        healthText = new GlyphLayout(smallFont, "Health: 0");
        damageText = new GlyphLayout(smallFont, "Damage: 0");
        rangeText = new GlyphLayout(smallFont, "Range: 0");
        speedText = new GlyphLayout(smallFont, "Speed: 0");
        minimiseSymbol = new GlyphLayout(smallFont, "-");
    }

    /*This is new*/
    private void setUpPauseText(){
        font.setColor(Color.DARK_GRAY);
        pauseText = new GlyphLayout(font, "Paused");
        font.setColor(Color.WHITE);
        resumeText = new GlyphLayout(font, "Resume");
        saveText = new GlyphLayout(font, "Save");
        exitText = new GlyphLayout(font, "Exit");
    }


    /***
     * Render method that draws all objects to the screen
     */
    public void render()
    {
        uiBatch.begin();

        //If not paused, render all UI elements
        if(!paused)
        {
            //Draws either the button to open the stats menu or the stats menu itself
            renderEngineStats();

            //Draws all the text to the screen in its correct place
            drawText();
        }
        //If the game is paused, draw the pause menu
        else{

            font.draw(uiBatch, pauseText, pauseTextX, pauseTextY);
            drawPauseString(1, "Resume", resumeTextX, resumeTextY, resumeText);
            drawPauseString(2, "Save", saveTextX, saveTextY, saveText);
            drawPauseString(3, "Exit", exitTextX, exitTextY, exitText);
        }
        uiBatch.end();
    }

    /*This is new*/
    private void renderEngineStats(){

        engineStatsRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Draws either the button to open the stats menu or the stats menu itself
        if (currentEngine != null && !displayingStats)
        {
            engineStatsRenderer.setColor(Color.GRAY);
            engineStatsRenderer.rect(showRectX, showRectY, showRectWidth, showRectHeight);
        } else if (currentEngine != null && displayingStats)
        {
            engineStatsRenderer.setColor(Color.GRAY);
            engineStatsRenderer.rect(statsRectX, statsRectY, statsRectWidth, statsRectHeight);
        }
        engineStatsRenderer.end();

        //Draws the outline for the button/menu
        engineStatsRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (currentEngine != null && !displayingStats)
        {
            engineStatsRenderer.setColor(Color.BLACK);
            engineStatsRenderer.rect(showRectX, showRectY, showRectWidth, showRectHeight);
        } else if (currentEngine != null && displayingStats)
        {
            engineStatsRenderer.setColor(Color.BLACK);
            engineStatsRenderer.rect(statsRectX, statsRectY, statsRectWidth, statsRectHeight);
            engineStatsRenderer.rect(minimiseX, minimiseY, minimiseWidth, minimiseHeight);
        }
        engineStatsRenderer.end();
        uiBatch.end();

    }

    /*This is new*/
    private void drawText(){
        //Draws all the text to the screen in its correct place
        uiBatch.begin();
        engineStatsRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Draws the text that tells the player who's turn it is
        if (gameState.isPlayerTurn())
        {
            font.draw(uiBatch, playerTurnText, playerTurnX, playerTurnY);
        } else
        {
            font.draw(uiBatch, enemyTurnText, enemyTurnX, enemyTurnY);
        }

        //If stats are not showing, just display button text
        if (currentEngine != null && !displayingStats)
        {
            smallFont.setColor(Color.WHITE);
            smallFont.draw(uiBatch, showEngineStatsText, showRectX + 5, showRectY + 20);
        }
        //If stats are showing, draw all the text relating to them
        else if (currentEngine != null && displayingStats)
        {
            smallFont.setColor(Color.WHITE);
            uiBatch.draw(currentEngine.getTexture(), statsRectX + 50, statsRectY + statsRectHeight - 70);
            smallFont.draw(uiBatch, maxVolumeText, statsRectX + 10, statsRectY + statsRectHeight - 90);
            smallFont.draw(uiBatch, healthText, statsRectX + 10, statsRectY + statsRectHeight - 120);
            smallFont.draw(uiBatch, damageText, statsRectX + 10, statsRectY + statsRectHeight - 150);
            smallFont.draw(uiBatch, rangeText, statsRectX + 10, statsRectY + statsRectHeight - 180);
            smallFont.draw(uiBatch, speedText, statsRectX + 10, statsRectY + statsRectHeight - 210);
            smallFont.draw(uiBatch, minimiseSymbol, minimiseX + 7, minimiseY + 15);
        }

        engineStatsRenderer.end();
    }


    /***
     * Method that is run for each string in the pause menu to set up its colour and position
     * @param position The position in the menu of the string
     * @param text The actual string that should be drawn
     * @param x the x position of the text
     * @param y the y position of the text
     * @param layout the layout that is used to store and render the text
     */
    public void drawPauseString(int position, String text, int x, int y, GlyphLayout layout){
        //If you are currently on this piece of text, draw it in blue, otherwise in white
        if(position == this.pausePosition){
            font.setColor(Color.BLUE);
            layout.setText(font, text);
        }else{
            font.setColor(Color.WHITE);
            layout.setText(font, text);
        }

        font.draw(uiBatch, layout, x, y);
    }


    /***
     * Sets up positions for all text and UI elements on the screen
     */
    public void setupPositions()
    {
        //Set up all positions for gui objects
        playerTurnX = (int)(Gdx.graphics.getWidth() / 2 - playerTurnText.width / 2);
        playerTurnY = Gdx.graphics.getHeight() - 30;
        enemyTurnX = (int)(Gdx.graphics.getWidth() / 2 - enemyTurnText.width / 2);
        enemyTurnY = Gdx.graphics.getHeight() - 30;

        showRectX = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4);
        showRectY = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 8);
        showRectWidth = Gdx.graphics.getWidth() / 5;
        showRectHeight = Gdx.graphics.getHeight() / 16;

        statsRectX = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4) - 20;
        statsRectY = (Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 2) - 30);
        statsRectWidth = Gdx.graphics.getWidth() / 4;
        statsRectHeight = Gdx.graphics.getHeight() / 2;

        minimiseWidth = Gdx.graphics.getWidth() / 32;
        minimiseHeight = Gdx.graphics.getHeight() / 24;
        minimiseX = statsRectX + statsRectWidth - minimiseWidth;
        minimiseY = statsRectY + statsRectHeight - minimiseHeight;

        pauseRectWidth = Gdx.graphics.getWidth() / 4;
        pauseRectHeight = Gdx.graphics.getHeight() / 2;
        pauseRectX = Gdx.graphics.getWidth() / 2 - pauseRectWidth / 2;
        pauseRectY = Gdx.graphics.getHeight() / 2 - pauseRectHeight / 2;

        pauseTextX = (int)(Gdx.graphics.getWidth() / 2 - pauseText.width / 2);
        pauseTextY = pauseRectY + pauseRectHeight - 25;

        resumeTextX = (int)(Gdx.graphics.getWidth() / 2 - resumeText.width / 2);
        resumeTextY = pauseRectY + pauseRectHeight - 75;

        saveTextX = (int)(Gdx.graphics.getWidth() / 2 - saveText.width / 2);
        saveTextY = pauseRectY + pauseRectHeight - 125;

        exitTextX = (int)(Gdx.graphics.getWidth() / 2 - exitText.width / 2);
        exitTextY = pauseRectY + pauseRectHeight - 175;
    }

    //Called by InputManager when the use presses the showStats button
    public void pressedShowStatsButton()
    {
        if(!displayingStats) displayingStats = true;
    }

    //Called by InputManager when the use presses the minimise button
    public void pressedMinimiseButton()
    {
        if(displayingStats) displayingStats = false;
    }

    private void setupRectanglePositions(){
        resumeBox = new Rectangle();
        resumeBox.setBounds((int)(Gdx.graphics.getWidth() / 2 - resumeText.width / 2), pauseRectY + pauseRectHeight - 125, 55, 50);
        saveBox = new Rectangle();
        saveBox.setBounds((int)(Gdx.graphics.getWidth() / 2 - exitText.width / 2), pauseRectY + pauseRectHeight - 175, 55, 50);
        exitBox = new Rectangle();
        exitBox.setBounds((int)(Gdx.graphics.getWidth() / 2 - exitText.width / 2), pauseRectY + pauseRectHeight - 225, 55, 50);

    }

    public void checkPausedButtonClicked(float x, float y) {
        if (paused && resumeBox.contains(x, y)) {
            isNotPaused();
        } else if (paused && saveBox.contains(x, y)){
            isNotPaused();
            SaveManager.makeNewSave(this.gameState);
        } else if (paused && exitBox.contains(x, y)) {
            this.stateManager.changeState(new MenuState(inputManager, font, stateManager, camera));
        }
    }

    //When game has ended, dispose of all objects
    public void dispose()
    {
        uiBatch.dispose();
        engineStatsRenderer.dispose();
    }

    //Getters and Setters
    public boolean isDisplayingStats()
    {
        return displayingStats;
    }

    public boolean isNotPaused() {
        return paused = false;
    }


    public boolean isPaused()
    {
        return paused;
    }

    public int getPausePosition(){
        return pausePosition;
    }

    public void setPausePosition(int pausePosition){
        this.pausePosition = pausePosition;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public void setDisplayingStats(boolean stats)
    {
        this.displayingStats = displayingStats;
    }

    public void setCurrentEngine(Engine engine)
    {
        this.currentEngine = engine;
        maxVolumeText = new GlyphLayout(smallFont, "Max Volume: " + engine.getMaxVolume());
        healthText = new GlyphLayout(smallFont, "Health: " + engine.getHealth());
        damageText = new GlyphLayout(smallFont, "Damage: " + engine.getDamage());
        rangeText = new GlyphLayout(smallFont, "Range: " + engine.getRange());
        speedText = new GlyphLayout(smallFont, "Speed: " + engine.getSpeed());
    }

    public Rectangle getShowStatsRect()
    {
        return new Rectangle(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 8),
                Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 16);
    }

    public Rectangle getStatsRect()
    {
        return new Rectangle(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4) - 20, (Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 2) - 30),
                Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
    }

    public Rectangle getMinimiseRect()
    {
        return new Rectangle(getStatsRect().x + getStatsRect().width - (Gdx.graphics.getWidth() / 32),
                getStatsRect().y + getStatsRect().height - (Gdx.graphics.getHeight() / 24),
                Gdx.graphics.getWidth() / 32, Gdx.graphics.getHeight() / 24);
    }
}