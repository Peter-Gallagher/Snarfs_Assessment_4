package com.septagon.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;

/**
Child of the state class that will be used to manage the system when the user is on the help screen
 */
//TODO Empty, useless class
public class HelpState extends State
{
    private String helpLabel;

    public HelpState(InputManager inputManager, BitmapFont font, StateManager stateManager)
    {
        super(inputManager, font, StateID.HELP, stateManager);
        helpLabel = "";
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

    public void dispose(){}
}
