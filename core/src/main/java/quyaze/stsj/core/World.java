package quyaze.stsj.core;

import quyaze.stsj.GameMaster;

/**
 * A base class for a StarterScreen and seperate from it. Meant to
 * handle input, logic, drawing, etc.
 */
public abstract class World
{
    //  Fields
    final protected GameMaster gameMaster;
    final protected StarterScreen owner;
    
    
    //  Constructor
    public World(GameMaster gameMaster, StarterScreen owner)
    {
        this.gameMaster = gameMaster;
        this.owner = owner;
    }
    
    
    /** For an owning {@code Screen.render()}. */
    public abstract void render(float delta);
}
