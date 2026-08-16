package quyaze.stsj.core;

/**
 * A base class for a StarterScreen and seperate from it. Meant to
 * handle input, logic, drawing, etc.
 */
public abstract class World
{
    //  Fields
    final protected StarterScreen owner;
    
    
    //  Constructor
    public World(StarterScreen owner)
    {
        this.owner = owner;
    }
    
    
    /**
     * @param deltaSeconds
     */
    public abstract void render();
}
