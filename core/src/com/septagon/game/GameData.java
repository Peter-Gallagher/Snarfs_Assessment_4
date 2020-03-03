package com.septagon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.septagon.entites.Engine;
import com.septagon.entites.Fortress;
import com.septagon.entites.Patrol;
import com.septagon.entites.Station;
import com.septagon.states.GameState;
import com.septagon.states.State;
import com.septagon.states.StateManager;

import java.util.ArrayList;


public class GameData {

    private FileHandle file = Gdx.files.local("core/assets/data.json");

    private int turnsPassed;
    private boolean playerTurn = true;
    private ArrayList<Fortress> fortresses;
    private ArrayList<Patrol> patrols;
    private Station fireStation;
    private ArrayList<Engine> engines;
    protected StateManager stateManager;
    protected State.StateID id;
    protected InputManager inputManager;
    protected BitmapFont font;
    private OrthographicCamera gameCamera;

    public GameData newGameData(GameState gameState){
        GameData gamedata = new GameData();
        gamedata.engines = gameState.getEngines();
        gamedata.fortresses = gameState.getFortresses();
        gamedata.patrols = gameState.getPatrols();
        gamedata.turnsPassed = gameState.getTurnsPassed();
        gamedata.playerTurn = gameState.isPlayerTurn();
        return gamedata;
    }
    public void save(GameData gamedata) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(json.prettyPrint(gamedata),false);
    }
    public void load(){
        Json json = new Json();
        ArrayList<Engine> engines = json.fromJson(ArrayList.class,Engine.class, file);
        //System.out.println(gamedata);
        //stateManager.changeState(new GameState(inputManager, font, stateManager, gameCamera));
    }
    }

