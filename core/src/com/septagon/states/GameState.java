package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.septagon.entites.*;
import com.septagon.game.Difficulty;
import com.septagon.game.GameData;
import com.septagon.game.InputManager;
import com.septagon.game.UIManager;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.AttackerManager;
import com.septagon.helperClasses.StatusBarGenerator;
import com.septagon.helperClasses.TileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Child class of the State class that will manage the system when the user is in the game
 */


public class GameState extends State implements Json.Serializable
{

    // this is our "target" resolution, note that the window can be any size, it is not bound to this one
    public final static float VP_WIDTH = 1280;
    public final static float VP_HEIGHT = 720;

    //Variable to keep track of whether it is the player or enemies turn
    private boolean playerTurn = true;

    //Camera that control the viewport of the game depending on input
    private OrthographicCamera camera;
    //Viewport that is used alongside the camera that contains the whole game map
    private ExtendViewport viewport;
    //Spritebatch that is used for rendering all objects in the game
    private SpriteBatch objectBatch;

    //Contains all the information about our game map
    private TiledGameMap gameMap;

    private int turnsPassed;
    private boolean paused = false;

    //Loads textures and creates objects for the engines
    public ArrayList<Engine> engines;

    //Loads textures and creates objects for the fortresses
    private ArrayList<Fortress> fortresses;

    //Loads textures and creates objects for the patrols
    private ArrayList<Patrol> patrols;

    //The paths that each patrol can take
    public static List<Tile> path1 = Arrays.asList(
            new Tile(10,18,null, false),
            new Tile(19,18, null, false),
            new Tile(19,33, null, false),
            new Tile(19,43, null, false),
            new Tile(19,33, null, false),
            new Tile(19,18, null, false));

    public static List<Tile> path2 = Arrays.asList(
            new Tile(44,35, null, false),
            new Tile(57,35, null, false),
            new Tile(57,28, null, false),
            new Tile(70,28, null, false),
            new Tile(57,28, null, false),
            new Tile(57,35, null, false));

    public static List<Tile> path3 = Arrays.asList(
            new Tile(37,2, null, false),
            new Tile(50,2, null, false),
            new Tile(45,12, null, false),
            new Tile(45,18, null, false),
            new Tile(57,18, null, false),
            new Tile(57,22, null, false),
            new Tile(64,22, null, false),
            new Tile(64,27, null, false),
            new Tile(64,22, null, false),
            new Tile(57,22, null, false),
            new Tile(57,18, null, false),
            new Tile(45,18, null, false),
            new Tile(45,12, null, false),
            new Tile(50,2, null, false));

    private ArrayList<Powerup> powerups = new ArrayList<>();
    //Loads textures and creates an object for the fire station
    private Station fireStation;

    //Keeps track of where in the game map the camera is currently
    private float currentCameraX, currentCameraY;

    //Create entityManager that will handle all entities in our game
    private EntityManager entityManager = new EntityManager();

    //These are used to help manage the input of the user when clicking our objects
    private ArrayList<Tile> tiles = new ArrayList<Tile>();

    //Creates instance of class that controls all the ui elements that stay on the screen
    private UIManager uiManager;

    //Creates an array of bullets
    public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    //private boolean shouldCreateBullets = false;

    private StatusBarGenerator statusBarGenerator;
    private TileManager tileManager;

    //Used to keep track of which fortress to move to when it is the enemies turn
    private int currentFortressIndex = 0;
    private int counter = 0;
    private boolean hasChangedFortress = false;

    //Adds a slight delay between switching turns so that it doesn't just happen straight away
    private int changeTurnCounter = 0;
    private boolean changingTurn = false;

    private AttackerManager attackerManager;

    /***
     * Constructor that sets inital values for all variables and gets values of variables that are used throughout full program
     * @param inputManager The games input manager that handles all the games input
     * @param font The font being used for the game
     * @param stateManager The games state manager for handling changing state
     * @param camera The camera that controls what is displayed on the screen
     */
    public GameState(InputManager inputManager, BitmapFont font, StateManager stateManager, OrthographicCamera camera)
    {
        super(inputManager, font, StateID.GAME, stateManager);
        this.camera = camera;
        turnsPassed = 0;
        currentCameraX = 0;
        currentCameraY = 0;

        bullets = new ArrayList<Bullet>();
    }

    /***
     * Sets up all objects in our game and gets the game ready to be played
     */
    public void initialise()
    {
        //Initialises all engines, fortress and stations in the game
        if (engines == null) {;
            initializeFireEngines();}
        if (fortresses == null) {initializeFortresses();}

        fireStation = new Station(72, 6, 256, 128, "fireStationTexture", 6);

        font.getData().setScale(Gdx.graphics.getWidth() / VP_WIDTH, Gdx.graphics.getHeight() / VP_HEIGHT);

        initializeUIManager();

        initializeGameMap();
        if (patrols == null) {
            initializePatrols();
        } else{
            //Otherwise we've loaded the patrols from serialization, and need to set the tileManager
            //now we have initialized it in GameState.
            for (Patrol patrol: patrols){
                patrol.setTileManager(this.tileManager);
            }
        }

        initializeEntityManager();

        initializeCamera();

        //Initialises the statusBarRenderer object
        statusBarGenerator = new StatusBarGenerator(engines, fortresses, patrols);

        //Initialise the AttackerManager
        attackerManager = new AttackerManager(engines, tiles, patrols, fortresses, this);

    }


    /***
     * Initializes all fire engines in the game and moves them to their starting positions
     */
    /*This changed*/
    private void initializeFireEngines(){
        //create all Fire Engine objects
        Engine engine1 = new Engine(0,0, "engineTexture1", 150, 40, 7, 10, 1500, 4);
        Engine engine2 = new Engine(0,0, "engineTexture2", 80, 15, 15, 15, 1100, 4);
        Engine engine3 = new Engine(0,0, "engineTexture3", 90, 28, 13, 20, 1300, 4);
        Engine engine4 = new Engine(0,0, "engineTexture4", 110, 16, 14, 22, 1200, 4);

        //Sets the engines positions so that they start from the fireStation
        engine1.setCol(77);
        engine1.setRow(5);

        engine2.setCol(75);
        engine2.setRow(4);

        engine3.setCol(72);
        engine3.setRow(5);

        engine4.setCol(73);
        engine4.setRow(4);

        //Adds all the engines to the ArrayList of engines
        engines = new ArrayList<Engine>();
        engines.add(engine1);
        engines.add(engine2);
        engines.add(engine3);
        engines.add(engine4);
        System.out.println(engine1);
    }

    /***
     * Initializes all fortresses in the game
     */
    private void initializeFortresses(){
        //creates all fortress objects
        Fortress fortressFire = new Fortress(34, 10, 256, 256, "fortressFireTexture", "defeatedFireTexture", 60, 13, 15);
        Fortress fortressMinister = new Fortress(41, 41, 256, 256, "fortressMinisterTexture", "defeatedMinsterTexture", 70, 8, 13);
        Fortress fortressStation = new Fortress(65, 30, 256, 256, "fortressStationTexture", "defeatedStationTexture", 90, 7, 9);
        Fortress fortressSavlos = new Fortress(9, 41, 256, 256, "fortressSalvoTexture", "defeatedSalvoTexture", 80, 13, 9);
        Fortress fortressCliffordsTower = new Fortress(1, 4, 256, 256, "fortressCliffordsTowerTexture", "defeatedCliffordsTowerTexture", 85, 11, 11);
        Fortress fortressCentralHall = new Fortress(9, 21, 256, 256, "fortressCentralHallTexture", "defeatedCentralHallTexture", 75, 12, 10);

        //Adds all the fortresses to the ArrayList of fortresses
        fortresses = new ArrayList<Fortress>();
        fortresses.add(fortressFire);
        fortresses.add(fortressMinister);
        fortresses.add(fortressStation);
        fortresses.add(fortressSavlos);
        fortresses.add(fortressCliffordsTower);
        fortresses.add(fortressCentralHall);
    }


    /***
     * initialises all patrols in the game
     */
    /*This is new*/
    private void initializePatrols() {
        //create preset paths for patrols
        ArrayList<Tile> path1 = initialisePath1();
        ArrayList<Tile> path2 = initialisePath2();
        ArrayList<Tile> path3 = initialisePath3();
        //create all Patrol objects
        Patrol patrol1 = new Patrol(10, 4, "creepyPatrol", 130, 20, 6, 5, this.path1, "path1", tileManager);
        Patrol patrol2 = new Patrol(44, 40, "creepyPatrol", 130, 20, 6, 5, this.path2, "path2", tileManager);
        Patrol patrol3 = new Patrol(37, 9, "creepyPatrol", 130, 20, 6, 5, this.path3, "path3", tileManager);


        //Adds all the patrols to the ArrayList of patrols
        patrols = new ArrayList<Patrol>();
        patrols.add(patrol1);
        patrols.add(patrol2);
        patrols.add(patrol3);

    }

    /***
     * initialise the path to be taken by patrol1
     * @return the node path of patrol1
     */
    protected ArrayList<Tile> initialisePath1(){

        ArrayList<Tile> path1 = new ArrayList<>();

        Tile tile1 = new Tile(10,18, null, false);
        Tile tile2 = new Tile(19,18, null, false);
        Tile tile3 = new Tile(19,33, null, false);
        Tile tile4 = new Tile(19,43, null, false);

        path1.add(tile1);
        path1.add(tile2);
        path1.add(tile3);
        path1.add(tile4);
        path1.add(tile3);
        path1.add(tile2);

        return path1;
    }

    /***
     * initialise the path to be taken by patrol2
     * @return the node path of patrol2
     */
    protected ArrayList<Tile> initialisePath2(){

        ArrayList<Tile> path2 = new ArrayList<>();

        Tile tile5 = new Tile(44,35, null, false);
        Tile tile6 = new Tile(57,35, null, false);
        Tile tile7 = new Tile(57,28, null, false);
        Tile tile8 = new Tile(70,28, null, false);

        path2.add(tile5);
        path2.add(tile6);
        path2.add(tile7);
        path2.add(tile8);
        path2.add(tile7);
        path2.add(tile6);

        return path2;
    }

    /***
     * initialise the path to be taken by patrol3
     * @return the node path of patrol3
     */
    protected ArrayList<Tile> initialisePath3(){

        ArrayList<Tile> path3 = new ArrayList<>();

        Tile tile9 = new Tile(37,2, null, false);
        Tile tile10 = new Tile(50,2, null, false);
        Tile tile11 = new Tile(45,12, null, false);
        Tile tile12 = new Tile(45,18, null, false);
        Tile tile13 = new Tile(57,18, null, false);
        Tile tile14 = new Tile(57,22, null, false);
        Tile tile15 = new Tile(64,22, null, false);
        Tile tile16 = new Tile(64,27, null, false);

        path3.add(tile9);
        path3.add(tile10);
        path3.add(tile11);
        path3.add(tile12);
        path3.add(tile13);
        path3.add(tile14);
        path3.add(tile15);
        path3.add(tile16);
        path3.add(tile15);
        path3.add(tile14);
        path3.add(tile13);
        path3.add(tile12);
        path3.add(tile11);
        path3.add(tile10);

        return path3;
    }


    /***
     * Method to initialise the entity manager
     */
    private void initializeEntityManager(){
        //Adds all the entities to the entity manager so all their updating and rendering can be handled
        entityManager = new EntityManager();
        entityManager.addEntity(fireStation);
        for(Fortress fortress: fortresses)
        {
            entityManager.addEntity(fortress);
        }
        for(Engine engine: engines)
        {
            entityManager.addEntity(engine);
        }
        for(Patrol patrol: patrols)
        {
            entityManager.addEntity(patrol);
        }
        entityManager.setPowerups(powerups);
        entityManager.getEntities().addAll(powerups);
        entityManager.initialise();
    }

    /***
     * Method to initialise the game camera
     */
    private void initializeCamera(){
        // Intialises the game viewport and spritebatch
        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);
        objectBatch = new SpriteBatch();
        objectBatch.setProjectionMatrix(camera.combined);

        //Sets up the camera parameters and moves it to its inital position
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.x = gameMap.getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2;
        camera.update();
    }

    /***
     * Method to initialise the game map
     */
    /*This is new*/
    private void initializeGameMap(){

        //Creates the gameMap instance that will be used to load the map from the tmx file
        gameMap = new TiledGameMap("extendedMap.tmx");

        //Create objects referring to all tiles in game
        for(int row = 0; row < gameMap.getMapHeight(); row++)
        {
            for(int col = 0; col < gameMap.getMapWidth(); col++)
            {
                if(gameMap.getTileByCoordinate(0, col, row) != null)
                    tiles.add(gameMap.getTileByCoordinate(0, col, row));
            }
        }

        //Sets up all the occupied tiles on the map so they cannot be moved to
        tileManager = new TileManager(engines, tiles);
        tileManager.setOccupiedTiles(gameMap);

        tileManager.createAdjacencyList(gameMap.getMapWidth(), gameMap.getMapHeight());
    }

    /*This is new*/
    private void initializeUIManager(){
        //Creates instance of uiManager which will be used to render and manage all UI elements
        uiManager = new UIManager(this, font);

        //Intialises all entities and all ui elements
        uiManager.initialise();
    }


    /***
     * Update method that is called every frame and will update and move all objects if required
     */
    public void update()
    {
        this.paused = uiManager.isPaused();
        //Update the bullets
        ArrayList<Bullet> bulletToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets)
        {
            float deltaTime = 1 / 60f;
            bullet.update(deltaTime);
            if (bullet.remove)
                bulletToRemove.add(bullet);
        }
        bullets.removeAll(bulletToRemove);

        //If we are in the process of waiting to change turn, just wait until the timer says we swap turns
        if(changingTurn){
            changeTurnCounter++;
            if(changeTurnCounter >= 30){
                changeTurnCounter = 0;
                playerTurn = !playerTurn;
                changingTurn = false;
            }
        }
        else if(!paused && playerTurn)
        {
            playerTurnUpdate();
        //If it is the enemies turn
        }else if(!paused)
        {
            enemyTurnUpdate();
        }
    }

    /**
     * Method that handles all of the updating for the player turn
     */
    private void playerTurnUpdate(){
        //Call the update method for all entities in our game
        entityManager.update();

        //If all the engines have been moved on the current turn, make it the enemies turn
        if (attackerManager.allEnginesMoved())
        {
            this.changingTurn = true;
            changeTurnCounter = 0;
            for(Engine e : engines){
                e.updatePowerup(entityManager, tileManager, this);
            }
        }

        //Updates the pointers to the current x and y positions of the camera
        currentCameraX = camera.position.x;
        currentCameraY = camera.position.y;

        //Checks if the player has destroyed all the fortresses
        boolean hasWon = true;
        for (Fortress fortress : fortresses)
        {
            if (fortress.getHealth() > 0) hasWon = false;
        }
        if (hasWon)
        {
            stateManager.changeState(new GameOverState(inputManager, font, stateManager, true));
        }

        //Checks if all the players fire engines have been destroyed
        boolean hasLost = attackerManager.checkIfAllEnginesDead();
        if (hasLost)
        {
            stateManager.changeState(new GameOverState(inputManager, font, stateManager, false));
        }
    }

    /**
     * Method that handles all the updating that should happen on an enemies turn
     */
    private void enemyTurnUpdate(){
        entityManager.checkPowerups(tileManager, this);
        boolean shouldShowFortress = false;

        //Work out what should happen if we need to display a new fortress
        if(!hasChangedFortress){
            //If all fortresses have been displayed, go back to the player turn
            if(currentFortressIndex >= fortresses.size()){
                patrolTurnUpdate();
                postAlienTurn();
                return;
            }
            //Get the current fortress that should be displayed
            Fortress currentFortress = fortresses.get(currentFortressIndex);

            //Work out if there is an engine near to the current fortress so we can display the fortress
            for(Engine fireEngine: engines){
                int xPosition = fireEngine.getX() + (fireEngine.getWidth() / 2) - (Gdx.graphics.getWidth() / 2);
                int yPosition = fireEngine.getY() + (fireEngine.getHeight() / 2) - (Gdx.graphics.getHeight() / 2);
                if(currentFortress.getX() >= xPosition && currentFortress.getX() <= xPosition + Gdx.graphics.getWidth() &&
                        currentFortress.getY() >= yPosition && currentFortress.getY() <= yPosition + Gdx.graphics.getHeight()){
                    shouldShowFortress = true;
                }
                else if(currentFortress.getX() + currentFortress.getWidth() >= xPosition && currentFortress.getX() +
                        currentFortress.getWidth() <= xPosition + Gdx.graphics.getWidth() && currentFortress.getY() +
                        currentFortress.getHeight() >= yPosition && currentFortress.getY() <= yPosition + Gdx.graphics.getHeight()){
                    shouldShowFortress = true;
                }
            }

            //If there is an engine near the fortress, show it and perform the fortresses attack
            if(shouldShowFortress)
            {
                attackerManager.snapToAttacker(currentFortress, gameMap, camera);
                attackerManager.BattleTurn(currentFortress);
            }
            else{
                hasChangedFortress = false;
            }
            hasChangedFortress = true;
        }
        //If we are already displaying a fortress, keep displaying until the timer has reached its limit
        else
        {
            counter++;
            if(counter >= 0){
                hasChangedFortress = false;
                currentFortressIndex++;
                counter = 0;
            }
        }
    }

    /***
     * Method that handles all the updating when patrols are moved
     */
    /*This is new*/
    public void patrolTurnUpdate(){
        for (Patrol patrol : patrols) {
            for (Engine engine : engines) {
                if(!patrol.isDead()){
                    if (patrol.inRange(engine)) {
                        patrol.shoot(engine,false);
                    } else {
                        patrol.move();
                    }
                    if (engine.inRange(patrol)){
                        engine.damageIfInRange(patrol, true);
                    }
                }
            }
        }
        attackerManager.handleDeadEngines();
        attackerManager.handleDeadPatrols();
    }

    /***
     * Method to handel all actions and checks to take place after an alien turn
     */
    /*This is new*/
    private void postAlienTurn(){
        boolean playMiniGame = false;
        currentFortressIndex = 0;
        turnsPassed++;

        strengthenFortresses();

        if(attackerManager.getTurnOfFirstAttack() != null){
            if ((turnsPassed - attackerManager.getTurnOfFirstAttack()) == 30){
                destroyStation();
            }
        }

        //If the fortresses have destroyed all engines, finish the game
        if(attackerManager.checkIfAllEnginesDead()){
            stateManager.changeState(new GameOverState(inputManager, font, stateManager, false));
            return;
        }

        attackerManager.snapToAttacker(engines.get(0), gameMap, camera);
        tileManager.resetMovableTiles();

            for(Engine fireEngine: engines){
                fireEngine.setMoved(false);
                if (!fireStation.isDead()){
                    if (fireEngine.ifInRangeFill(fireStation)){
                        playMiniGame = Difficulty.shouldTriggerMinigame();
                    }
                }
            }

            if (playMiniGame){
                stateManager.changeState(new MinigameState(inputManager, font, stateManager));
            }

        playerTurn = true;

    }

    /***
     * Method responsible for strengthening fortresses over time
     */
    /*This is new*/
    public void strengthenFortresses(){

        if (turnsPassed >= 10){
            for (Fortress fortress : fortresses) {
            fortress.setHealth(fortress.getHealth() + turnsPassed % 3);
            fortress.setMaxHealth(fortress.getMaxHealth() + turnsPassed % 3);

            fortress.setDamage(fortress.getDamage() + (turnsPassed % 5));
            }
        }

    }

    /***
     * Method that handles the destruction of the fire station
     */
    public void destroyStation(){
        fireStation.setDead();
        fireStation.setTexture(AssetManager.getDestroyedFireStationTexture());
    }


    /***
     * Method that will render everything in the game each frame
     * @param batch The batch which is used for all the rendering
     */
    public void render(SpriteBatch batch)
    {
        //Clear the background to red - the colour does not really matter
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render the map and all objects for our game
        gameMap.render(camera);
        objectBatch.setProjectionMatrix(camera.combined);
        objectBatch.begin();
        entityManager.render(objectBatch);
        for (Bullet bullet : bullets) {
            bullet.render(objectBatch);
        }

        //Renderers the movement grid for the currently touched engine
        if (inputManager.isHasBeenTouched() && this.playerTurn){
            attackerManager.renderMovementGrid(objectBatch);
        }

        //Ends the drawing of all the objects for the current frame
        objectBatch.end();
        statusBarGenerator.renderBars(camera);


        //renders all the ui elements
        uiManager.render();
    }

    //Unused method that is required since this is a child of State
    public void dispose(){

    }

    /***
     * Method that handles map resizing when the window size is changed
     */
    public void hasResized()
    {
        //Checks that the change in screen size has not caused the camera to show features off the map
        //If this is the case, clamp the camera position so that it sets it back to the edge of the map
        if(camera.position.x <= (Gdx.graphics.getWidth() / 2)) {
            camera.position.x = Gdx.graphics.getWidth() / 2;
        }
        if(camera.position.y <= (Gdx.graphics.getHeight() / 2)) {
            camera.position.y = Gdx.graphics.getHeight() / 2;
        }
        if(camera.position.x >= getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2){
            camera.position.x = getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2;
        }
        if(camera.position.y >= getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2){
            camera.position.x = getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2;
        }

        //uiManager.setupPositions();
    }

    //Getters and setters

    public int getTurnsPassed()
    {
        return turnsPassed;
    }

    public float getCurrentCameraX()
    {
        return currentCameraX;
    }

    public float getCurrentCameraY()
    {
        return currentCameraY;
    }

    public UIManager getUiManager()
    {
        return uiManager;
    }

    public TileManager getTileManager(){ return tileManager; }

    public AttackerManager getAttackerManager(){
        return attackerManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    public Station getStation(){
        return fireStation;
    }

    public ArrayList<Engine> getEngines(){
        return engines;
    }

    public boolean isPlayerTurn()
    {
        return playerTurn;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        uiManager.setPaused(paused);
    }

    public void setTurnsPassed(int turnsPassed) {
        this.turnsPassed = turnsPassed;
    }

    public int getMapWidth() {
        return gameMap.getMapWidth();
    }

    public int getMapHeight() {
        return gameMap.getMapHeight();
    }

    public ArrayList<Fortress> getFortresses() { return fortresses; }

    public ArrayList<Patrol> getPatrols() { return patrols; }

    public GameState(){
        super(null, null, StateID.GAME, null);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:-";
        this.font = generator.generateFont(parameter);
        

    }

    @Override
    public void write(Json json) {
        json.writeValue("turnsPassed", this.turnsPassed);
        json.writeValue("engines", this.engines);
        json.writeValue("fortresses", this.fortresses);
        json.writeValue("powerups", this.entityManager.getPowerups());
        json.writeValue("patrols", this.patrols);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue jsonEngines = jsonData.get("engines");
        this.engines = new ArrayList<>();
        for (int i = 0; i < jsonEngines.size; i++){
            Engine engine = new Engine();
            engine.read(json, jsonEngines.get(i));
            this.engines.add(engine);
        }

        JsonValue jsonFortresses = jsonData.get("fortresses");
        this.fortresses = new ArrayList<>();
        for (int i = 0; i < jsonFortresses.size; i++){
            Fortress fortress = new Fortress();
            fortress.read(json, jsonFortresses.get(i));
            this.fortresses.add(fortress);
        }

        JsonValue jsonPowerups = jsonData.get("powerups");
        ArrayList<Powerup> powerups = new ArrayList<>();
        for (int i = 0; i < jsonPowerups.size; i++){
            Powerup powerup = new Powerup();
            powerup.read(json, jsonPowerups.get(i));
            powerups.add(powerup);
        }
        this.powerups = powerups;

        JsonValue jsonPatrols = jsonData.get("patrols");
        this.patrols = new ArrayList<>();
        for (int i = 0; i < jsonPatrols.size; i++){
            Patrol patrol = new Patrol();
            patrol.read(json, jsonPatrols.get(i));
            this.patrols.add(patrol);
        }
    }

    public void setStateManager(StateManager sm){
        this.stateManager = sm;
    }
    public void setInputManager(InputManager im){
        this.inputManager = im;
    }
    public void setCamera(OrthographicCamera cam){
        this.camera = cam;
    }
}