package com.septagon.game;

import java.util.Random;

public class Difficulty {

    /**
     * ASSESSMENT 4
     * Class that saves the current game difficulty as a static field
     * and method to affect the difficulty of the game.
     */
    public enum Difficulties{
        EASY,
        MEDIUM,
        HARD
    }

    public static Difficulties current = Difficulties.MEDIUM; //Default difficulty

    private Difficulty(){}//Do not initialize

    /**
     * Returns a float that the damage of a fortress can be times by
     * to scale with difficulty
     * @return Damage modifier
     */
    public static float getFortressDamageMod(){
        switch (current){
            case EASY:
                return 0.85f;
            case MEDIUM:
                return 1f;
            case HARD:
                return 1.15f;
        }
        System.err.println("Difficulty: " + current + " not configured for getFortressDamageMod");
        return 1f;
    }

    /**
     * Returns a float that the damage of Engines can be scaled by.
     * @return Damage modifier
     */
    public static float getEngineDamageMod(){
        switch (current){
            case EASY:
                return 0.75f;
            case MEDIUM:
                return 1f;
            case HARD:
                return 1.25f;
        }
        System.err.println("Difficulty: " + current + " not configured for getEngineDamageMod!");
        return 1f;
    }

    /**
     * Returns a float that the Engine volume can be scaled by.
     * @return Volume modifier
     */
    public static float getEngineVolumeMod(){
        switch(current){
            case EASY:
                return 0.8f;
            case MEDIUM:
                return 1f;
            case HARD:
                return 1.2f;

        }
        System.err.println("Difficulty: " + current + " not configured for getEngineVolumeMod()!");
        return 1f;
    }

    /**
     * Returns the amount of turns that should pass until the Station is destroyed.
     */
    public static int turnsToDestroyStation(){
        switch(current){
            case EASY:
                return 35;
            case MEDIUM:
                return 30;
            case HARD:
                return 25;
            default:
                return 30;
        }
    }
    /**
     * Returns boolean of weather a minigame should trigger, value
     * has a different probability depending on current difficulty.
     * @return bool - weather minigame should trigger
     */
    public static boolean shouldTriggerMinigame(){
        Random rand = new Random();
        switch (current){
            case EASY:
                return rand.nextFloat() < 0.2f;
            case MEDIUM:
                return rand.nextFloat() < 0.35f;
            case HARD:
                return rand.nextFloat() < 0.6f;
        }
        System.err.println("Difficulty: " + current + " not configured for shouldTriggerMiniGame!");
        return true;
    }

    /**
     * Sets the difficulty to the next along, used in the main menu
     */
    public static void nextDifficulty(){
        switch(current){
            case EASY:
                current = Difficulties.MEDIUM;
                break;
            case MEDIUM:
                current = Difficulties.HARD;
                break;
            case HARD:
                current = Difficulties.EASY;
                break;
            default:
                System.err.println("Difficulty not considered in nextDifficulty()!");
        }
    }

    /**
     * Sets the difficulty to the previous along, used in the main menu
     */
    public static void previousDifficulty(){
        switch(current){
            case EASY:
                current = Difficulties.HARD;
                break;
            case MEDIUM:
                current = Difficulties.EASY;
                break;
            case HARD:
                current = Difficulties.MEDIUM;
                break;
            default:
                System.err.println("Difficulty not considered in previousDifficulty()!");
        }
    }

    public static Difficulties getCurrent(){
        return current;
    }

    public static void setCurrent(Difficulties difficulty){
        current = difficulty;
    }
}
