package com.septagon.game;

import java.util.Random;

public class Difficulty {

    public enum Difficulties{
        EASY,
        MEDIUM,
        HARD
    }

    public static Difficulties current = Difficulties.MEDIUM; //Default difficulty

    private Difficulty(){}

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
     * Returns boolean of weather a minigame should trigger, value
     * has a different probability depending on current difficulty.
     * @return bool - weather minigame should trigger
     */
    public static boolean shouldTriggerMinigame(){
        Random rand = new Random();
        switch (current){
            case EASY:
                return rand.nextFloat() < 0.15f;
            case MEDIUM:
                return rand.nextFloat() < 0.28f;
            case HARD:
                return rand.nextFloat() < 0.4f;
        }
        System.err.println("Difficulty: " + current + " not configured for shouldTriggerMiniGame!");
        return true;
    }

    public static void setCurrent(Difficulties difficulty){
        current = difficulty;
    }

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
}
