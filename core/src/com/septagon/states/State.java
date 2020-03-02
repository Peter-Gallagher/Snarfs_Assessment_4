package com.septagon.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;

/**
Abstract class that defines what all states within the game should contain
 */

public abstract class State
{
    //Enum used to keep track of what type of state each state is
    public enum StateID { GAME_OVER, GAME, MENU, MINIGAME ,PAUSED}

    protected StateID id;
    protected InputManager inputManager;
    protected BitmapFont font;
    protected StateManager stateManager;

    protected State(InputManager inputManager, BitmapFont font, StateID id, StateManager stateManager)
    {
        this.inputManager = inputManager;
        this.font = font;
        this.id = id;
        this.stateManager = stateManager;
    }

    //Abstract methods that are used to handle all actions within a state
    public abstract void initialise();
    public abstract void update();
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();

    public StateID getID() { return id; }
}
