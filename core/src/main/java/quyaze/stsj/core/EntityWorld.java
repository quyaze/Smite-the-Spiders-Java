package quyaze.stsj.core;

import quyaze.stsj.GameMaster;

/**
 * A World with entities (integer).
 * <p></p>
 * Within {@code render()}, The World should iterate over all entities and pass each into a
 * {@link EWSystem}. {@link EWDatastore}
 */
public abstract class EntityWorld extends World
{
    //  Fields
    protected int entities;
    
    
    //  Constructor
    public EntityWorld(GameMaster gameMaster, StarterScreen owner)
    {
        super(gameMaster, owner);
    }
}
