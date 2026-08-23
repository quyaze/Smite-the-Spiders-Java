package quyaze.stsj.core;

import quyaze.stsj.SmiteTheSpiders;

/**
 * A base class for a StarterScreen and seperate from it. Meant to
 * handle input, logic, drawing, etc.
 */
public abstract class World
{
    //  Fields
    final protected SmiteTheSpiders game;
    final protected StarterScreen owner;
    
    
    //  Constructor
    public World(SmiteTheSpiders game, StarterScreen owner)
    {
        this.game = game;
        this.owner = owner;
    }
    
    
    /** For an owning {@code Screen.render()}. */
    public abstract void render(float delta);
}
