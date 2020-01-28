package com.septagon.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;

/**
Child class of the State class that will be used to manage the system when the user is on the settings screen
 */
//TODO Empty useless class for a settings menu
public class SettingsState extends State
{
    public SettingsState(InputManager inputManager, BitmapFont font, StateManager stateManager)
    {
        super(inputManager, font, StateID.SETTINGS, stateManager);
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

    public void applyUserSettings()
    {
    }
}
