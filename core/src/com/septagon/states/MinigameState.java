package com.septagon.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;

/**
Child of State class that will be used to manage the system when the user is playing the minigame
 */
//TODO Empty minigame class, implement a minigame here
public class MinigameState extends State 
{
    //Used to keep track of the score in the minigame
    private int score;

    public MinigameState(InputManager inputManager, BitmapFont font, StateManager stateManager)
    {
        super(inputManager, font, StateID.MINIGAME, stateManager);
        score = 0;
    }

    public void initialise()
    {
    }

    public void update()
    {
    }

    public void render(SpriteBatch batch)
    {
    }

    public void dispose(){

    }

    public void handleInputForMinigame() {}

    private void returnToMainGame() {}
}
