package com.septagon.states;

/*
Class that is used to manage all of the states within the game and manage the changes between
the different states
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class StateManager 
{
    //List that keeps track of all the states that have been seen in the game so far
    private ArrayList<State> states;
    private int currentIndex = 0;

    public StateManager()
    {
        states = new ArrayList<State>();
    }

    /**
     * Intialises the current state of the game
     */
    public void initialise()
    {
    	states.get(currentIndex).initialise();
    }

    /**
     * Updates the current state of the game
     */
    public void update()
    {
    	states.get(currentIndex).update();
    }

    /**
     * Renders the current state of the game
     * @param batch The batch that is used for rendering all entities in the game
     */
    public void render(SpriteBatch batch)
    {
    	states.get(currentIndex).render(batch);
    }

    /**
     * Dispose of all elements from all states when the game is closed
     */
    public void dispose(){
        for(State s: states){
            s.dispose();
        }
    }

    /**
     * Changes the current state of the game
     * @param newState the state that is going to become the current state
     */
    public void changeState(State newState)
    {
        newState.initialise();
        if (newState.id == State.StateID.GAME){
            //So we can't go back to a previous GameState
            deleteAllInstancesOf(State.StateID.GAME);
        }
    	states.add(newState);
    	currentIndex = states.indexOf(newState);
    }
    /*This is new*/
    public void changeToGameState(){
        currentIndex = getState(State.StateID.GAME);
    }

    /**
     * ASSESSMENT 4
     */
    public void deleteAllInstancesOf(State.StateID id){
        ArrayList<State> toRemove = new ArrayList<>();
        for (State state: this.states){
            if (state.id == id){
                toRemove.add(state);
            }
        }
        this.states.removeAll(toRemove);

    }
    //Getters
    public int getCurrentIndex() { return currentIndex; }
    public State getCurrentState() { return states.get(currentIndex); }
    public ArrayList<State> getStates() { return states; }

    /*This is new*/
    public int getState(State.StateID stateType) {
        for (int i = 0; i < states.size(); i++){
            if (states.get(i).getID() == stateType){
                return i;
            }
        }
        return 0;
    }


}
