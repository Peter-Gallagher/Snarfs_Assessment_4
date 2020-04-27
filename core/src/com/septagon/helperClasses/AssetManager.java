package com.septagon.helperClasses;

/**
 * Class used to load in all the textures to the game so that they only have to loaded once and
 *  can be accessed anywhere in the program (since all the textures are static)
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class AssetManager {


    private static final Texture engineTexture1 = new Texture(Gdx.files.internal("images/engineSF.png"));
    private static final Texture engineTexture2 = new Texture(Gdx.files.internal("images/engineRS.png"));
    private static final Texture engineTexture3 = new Texture(Gdx.files.internal("images/engineMCL.png"));
    private static final Texture engineTexture4 = new Texture(Gdx.files.internal("images/engineRP.png"));

    private static final Texture destroyedEngineTexture1 = new Texture(Gdx.files.internal("images/destroyedEngineSF.png"));
    private static final Texture destroyedEngineTexture2 = new Texture(Gdx.files.internal("images/destroyedEngineRS.png"));
    private static final Texture destroyedEngineTexture3 = new Texture(Gdx.files.internal("images/destroyedEngineMCL.png"));
    private static final Texture destroyedEngineTexture4 = new Texture(Gdx.files.internal("images/destroyedEngineRP.png"));

    private static final Texture moveSpaceTexture = new Texture(Gdx.files.internal("move_square.png"));

    private static final Texture fortressFireTexture = new Texture(Gdx.files.internal("images/FortressFire.png"));
    private static final Texture fortressMinisterTexture = new Texture(Gdx.files.internal("images/FortressMinister.png"));
    private static final Texture fortressStationTexture = new Texture(Gdx.files.internal("images/FortressStation.png"));
    private static final Texture fortressCentralHallTexture = new Texture(Gdx.files.internal("images/centralHall.png"));
    private static final Texture fortressSalvoTexture = new Texture(Gdx.files.internal("images/salvo.png"));
    private static final Texture fortressCliffordsTowerTexture = new Texture(Gdx.files.internal("images/cliffordsTower.png"));

    private static final Texture defeatedFireTexture = new Texture(Gdx.files.internal("images/DefeatedOldStation.png"));
    private static final Texture defeatedMinsterTexture = new Texture(Gdx.files.internal("images/DefeatedMinster.png"));
    private static final Texture defeatedStationTexture = new Texture(Gdx.files.internal("images/DefeatedRailStation.png"));
    private static final Texture defeatedSalvoTexture = new Texture(Gdx.files.internal("images/salvoDefeated.png"));
    private static final Texture defeatedCentralHallTexture = new Texture(Gdx.files.internal("images/centralHallWithoutPlant.png"));
    private static final Texture defeatedCliffordsTowerTexture = new Texture(Gdx.files.internal("images/cliffordsTowerWithoutPlant.png"));


    private static final Texture fireStationTexture = new Texture(Gdx.files.internal("images/fireStation.png"));
    private static final Texture destroyedStationTexture = new Texture(Gdx.files.internal("images/fireStationDestroyed.png"));
    private static final Texture creepyPatrol = new Texture(Gdx.files.internal("images/xeno.png"));

    private static final Texture fortressBoundaryImage = new Texture(Gdx.files.internal("selected fortress.png"));

    private static final Texture powerup1 = new Texture(Gdx.files.internal("images/powerups/1.png")); //hp
    private static final Texture powerup2 = new Texture(Gdx.files.internal("images/powerups/2.png")); //refill
    private static final Texture powerup3 = new Texture(Gdx.files.internal("images/powerups/3.png")); //damage
    private static final Texture powerup4 = new Texture(Gdx.files.internal("images/powerups/4.png")); //sped
    private static final Texture powerup5 = new Texture(Gdx.files.internal("images/powerups/5.png")); //invuln

    private static final Texture nullTex = new Texture(Gdx.files.internal("images/nullTex.png"));
    private static final Texture missingTexture = new Texture(Gdx.files.internal("images/missingTexture.png"));

    //Getters
    public static Texture getTextureFromId(String textureId){
        if (textureId == null){
            return missingTexture;
        }
        switch(textureId) {
            case "engineTexture1": return engineTexture1;
            case "engineTexture2": return engineTexture2;
            case "engineTexture3": return engineTexture3;
            case "engineTexture4": return engineTexture4;

            case "destroyedEngineTexture1": return destroyedEngineTexture1;
            case "destroyedEngineTexture2": return destroyedEngineTexture2;
            case "destroyedEngineTexture3": return destroyedEngineTexture3;
            case "destroyedEngineTexture4": return destroyedEngineTexture4;

            case "moveSpaceTexture": return moveSpaceTexture;

            case "fortressFireTexture": return fortressFireTexture;
            case "fortressMinisterTexture": return fortressMinisterTexture;
            case "fortressStationTexture": return fortressStationTexture;
            case "fortressCentralHallTexture": return fortressCentralHallTexture;
            case "fortressSalvoTexture": return fortressSalvoTexture;
            case "fortressCliffordsTowerTexture": return fortressCliffordsTowerTexture;

            case "defeatedFireTexture": return defeatedFireTexture;
            case "defeatedMinsterTexture": return defeatedMinsterTexture;
            case "defeatedStationTexture": return defeatedStationTexture;
            case "defeatedSalvoTexture": return defeatedSalvoTexture;
            case "defeatedCentralHallTexture": return defeatedCentralHallTexture;
            case "defeatedCliffordsTowerTexture": return defeatedCliffordsTowerTexture;

            case "fireStationTexture": return fireStationTexture;
            case "destroyedStationTexture": return destroyedStationTexture;
            case "creepyPatrol": return creepyPatrol;

            case "fortressBoundaryImage": return fortressBoundaryImage;

            case "powerup1": return powerup1;
            case "powerup2": return powerup2;
            case "powerup3": return powerup3;
            case "powerup4": return powerup4;
            case "powerup5": return powerup5;

            default: return missingTexture;
        }
    }

    public static Texture getNull(){ return nullTex; }
    public static Texture getEngineTexture1() {
        return engineTexture1;
    }

    public static Texture getEngineTexture2() {
        return engineTexture2;
    }

    public static Texture getEngineTexture3() {
        return engineTexture3;
    }

    public static Texture getEngineTexture4() {
        return engineTexture4;
    }

    public static Texture getDestroyedEngineTexture1() {
        return destroyedEngineTexture1;
    }

    public static Texture getDestroyedEngineTexture2() {
        return destroyedEngineTexture2;
    }

    public static Texture getDestroyedEngineTexture3() {
        return destroyedEngineTexture3;
    }

    public static Texture getDestroyedEngineTexture4() {
        return destroyedEngineTexture4;
    }

    public static Texture getMoveSpaceTexture() {
        return moveSpaceTexture;
    }

    public static Texture getFortressFireTexture() {
        return fortressFireTexture;
    }

    public static Texture getFortressStationTexture() {
        return fortressStationTexture;
    }

    public static Texture getFortressMinisterTexture() {
        return fortressMinisterTexture;
    }

    public static Texture getFortressCentralHallTexture() { return fortressCentralHallTexture; }

    public static Texture getFireStationTexture() {
        return fireStationTexture;
    }

    public static Texture getFortressBoundaryImage() {
        return fortressBoundaryImage;
    }

    public static Texture getDefeatedFireTexture() {
        return defeatedFireTexture;
    }

    public static Texture getDefeatedCliffordsTowerTexture () { return defeatedCliffordsTowerTexture; }

    public static Texture getDefeatedSalvoTexture () { return defeatedSalvoTexture; }

    public static Texture getDefeatedCentralHallTexture () { return defeatedCentralHallTexture; }

    public static Texture getDefeatedMinsterTexture(){ return  defeatedMinsterTexture; }

    public static  Texture getDefeatedStationTexture() { return defeatedStationTexture; }

    public static  Texture getDestroyedFireStationTexture() { return destroyedStationTexture; }

    public static Texture getfortressSalvoTexture() { return fortressSalvoTexture; }

    public static Texture getfortressCliffordsTowerTexture() { return fortressCliffordsTowerTexture; }

    public static Texture getCreepyPatrol() { return creepyPatrol; }

    public static String getPowerup(int power) {
        switch(power){
            case(0): return "powerup1";
            case(1): return "powerup2";
            case(2): return "powerup3";
            case(3): return "powerup4";
            default: return "powerup5";
        }
    }
}
