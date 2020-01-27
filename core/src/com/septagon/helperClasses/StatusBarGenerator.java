package com.septagon.helperClasses;

/**
 * Class used to render all the health bars and the water meter bars for all entites
 * that require them
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.septagon.entites.Attacker;
import com.septagon.entites.Engine;
import com.septagon.entites.Fortress;

import java.util.ArrayList;

public class StatusBarGenerator
{
    //Renderer that is used to draw all the shapes of the bars
    private ShapeRenderer barRenderer;

    private ArrayList<Engine> engines;
    private ArrayList<Fortress> fortresses;

    public StatusBarGenerator(ArrayList<Engine> engines, ArrayList<Fortress> fortresses){
        this.engines = engines;
        this.fortresses = fortresses;
        barRenderer = new ShapeRenderer();
    }

    /***
     * Method that will render the health bars for all the fortresses and engines in the game
     */
    public void renderBars(OrthographicCamera camera) {
        barRenderer.setProjectionMatrix(camera.combined);

        //Render the health bar for all entities in the game
        for(Engine fireEngine: engines){
            renderHealthBarForAttacker(fireEngine);
            renderWaterBarForEngine(fireEngine);
        }
        for(Fortress fortress: fortresses){
            renderHealthBarForAttacker(fortress);
        }
    }

    /**
     * Method called for each attacker which will render its health bar just above it
     * @param attacker The Attacker which the health bar is being rendered for
     */
    private void renderHealthBarForAttacker(Attacker attacker){
        barRenderer.begin(ShapeRenderer.ShapeType.Filled);

        barRenderer.setColor(169.0f/255.0f, 169.0f/255.0f, 169.0f/255.0f, 1);
        barRenderer.rect(attacker.getX() - 2, attacker.getY() + attacker.getHeight(), attacker.getWidth() + 4, 9);

        //Work out whether the current health meter should show in red, yellow or green depending on health value
        int healthBoundary1 = attacker.getMaxHealth() / 2;
        int healthBoundary2 = attacker.getMaxHealth() / 4;

        if(attacker.getHealth() >= healthBoundary1){
            barRenderer.setColor(Color.GREEN);
        }else if(attacker.getHealth() >= healthBoundary2){
            barRenderer.setColor(Color.YELLOW);
        }else{
            barRenderer.setColor(Color.RED);
        }

        //Works out the size of the health bar and renderers it to the screen
        float healthBarLength = ((float)attacker.getWidth() / (float)attacker.getMaxHealth()) * attacker.getHealth();
        barRenderer.rect(attacker.getX(), attacker.getY() + attacker.getHeight() + 2, healthBarLength, 5);

        barRenderer.end();
    }

    /**
     * Method that is used to render the water meter for each engine just underneath the engine
     * @param fireEngine The engine which the health bar is being rendered for
     */
    private void renderWaterBarForEngine(Engine fireEngine){
        barRenderer.begin(ShapeRenderer.ShapeType.Filled);

        barRenderer.setColor(169.0f/255.0f, 169.0f/255.0f, 169.0f/255.0f, 1);
        barRenderer.rect(fireEngine.getX() - 2, fireEngine.getY() - 9, fireEngine.getWidth() + 4, 9);

        barRenderer.setColor(0.0f, 167.0f/255.0f, 190.0f/255.0f, 1.0f);

        float waterBarLength = ((float)fireEngine.getWidth() / (float)fireEngine.getMaxVolume()) * fireEngine.getVolume();
        barRenderer.rect(fireEngine.getX(), fireEngine.getY() - 7, waterBarLength, 5);

        barRenderer.end();
    }

    /**
     * Method useed to clean up the barRenderer
     */
    public void dispose(){
        barRenderer.dispose();
    }
}
