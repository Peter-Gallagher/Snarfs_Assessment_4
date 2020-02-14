package com.septagon.helperClasses;

/**
 * Class used to load in all the textures to the game so that they only have to loaded once and
 *  can be accessed anywhere in the program (since all the textures are static)
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    //TODO add extra fire engine textures
    private static final Texture engineTexture1 = new Texture(Gdx.files.internal("images/engineSF.png"));
    private static final Texture engineTexture2 = new Texture(Gdx.files.internal("images/engineRS.png"));
    private static final Texture engineTexture3 = new Texture(Gdx.files.internal("images/engineMCL.png"));
    private static final Texture engineTexture4 = new Texture(Gdx.files.internal("images/engineRP.png"));
    private static final Texture destroyedEngineTexture = new Texture(Gdx.files.internal("images/destroyedFE.png"));
    private static final Texture moveSpaceTexture = new Texture(Gdx.files.internal("move_square.png"));

    //TODO add extra fortress textures
    private static final Texture fortressFireTexture = new Texture(Gdx.files.internal("images/FortressFire.png"));
    private static final Texture fortressMinisterTexture = new Texture(Gdx.files.internal("images/FortressMinister.png"));
    private static final Texture fortressStationTexture = new Texture(Gdx.files.internal("images/FortressStation.png"));
    private static final Texture fortressPlaceHolderTexture = new Texture(Gdx.files.internal("images/FortressPlaceHolder.png"));
    private static final Texture fortressSalvoTexture = new Texture(Gdx.files.internal("images/salvo.png"));
    private static final Texture fortressCliffordsTowerTexture = new Texture(Gdx.files.internal("images/cliffordsTower.png"));

    private static final Texture defeatedFireTexture = new Texture(Gdx.files.internal("images/DefeatedOldStation.png"));
    private static final Texture defeatedMinsterTexture = new Texture(Gdx.files.internal("images/DefeatedMinster.png"));
    private static final Texture defeatedStationTexture = new Texture(Gdx.files.internal("images/DefeatedRailStation.png"));

    private static final Texture fireStationTexture = new Texture(Gdx.files.internal("images/fireStation.png"));
    private static final Texture destroyedStationTexture = new Texture(Gdx.files.internal("images/destroyedFS.png"));
    private static final Texture creepyPatrol = new Texture(Gdx.files.internal("images/creepyPatrol.png"));
    private static final Texture deadPatrol = new Texture(Gdx.files.internal("images/deadPatrol.png"));

    private static final Texture fortressBoundaryImage = new Texture(Gdx.files.internal("selected fortress.png"));

    //Getters
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

    public static Texture getFireStationTexture() {
        return fireStationTexture;
    }

    public static Texture getFortressBoundaryImage() {
        return fortressBoundaryImage;
    }

    public static Texture getDefeatedFireTexture() {
        return defeatedFireTexture;
    }

    public static Texture getDefeatedMinsterTexture(){ return  defeatedMinsterTexture; }

    public static  Texture getDefeatedStationTexture() { return defeatedStationTexture; }

    public static Texture getfortressPlaceHolderTexture() {
        return fortressPlaceHolderTexture;
    }

    public static Texture getfortressSalvoTexture() { return fortressSalvoTexture; }

    public static Texture getfortressCliffordsTowerTexture() { return fortressCliffordsTowerTexture; }

    public static Texture getCreepyPatrol() { return creepyPatrol; }
}
