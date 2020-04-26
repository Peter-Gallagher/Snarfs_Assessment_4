package com.septagon.helperClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.septagon.states.GameState;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ASSESSMENT 4
 * Class that manages making and loading saves
 */
public class SaveManager {

    private SaveManager(){}


    public static void makeNewSave(GameState gameState){
        Json json = new Json();
        String fileName = getNewSaveName();

        FileHandle file = Gdx.files.local("saves//" + fileName + ".save"); //make file in folder "saves"
        file.writeString(json.toJson(gameState), false);
        System.out.println(file.path());
        System.out.println("Made a save at: " + file.path());
        getAllSaveNames();
    }

    public static ArrayList<String> getAllSaveNames(){
        ArrayList<String> saveNames = new ArrayList<>();
        FileHandle[] files = Gdx.files.local("saves/").list();
        for(FileHandle file: files){
            saveNames.add(file.name());
        }
        return saveNames;
    }

    public static GameState loadSave(String saveName){
        FileHandle file = Gdx.files.local("saves//" + saveName);
        String saveAsString = file.readString();
        Json json = new Json();
        GameState save = json.fromJson(GameState.class, saveAsString);
        return save;
    }

    public static GameState loadMostRecentSave(){
        ArrayList<String> saveNames = getAllSaveNames();
        return loadSave(saveNames.get(saveNames.size() - 1));
    }

    private static String getNewSaveName(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(formatter);
    }
}
