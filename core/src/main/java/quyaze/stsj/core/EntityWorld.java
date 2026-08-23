package quyaze.stsj.core;

import quyaze.stsj.SmiteTheSpiders;

/**
 * A World with entities (integer).
 * <p></p>
 * Within {@code render()}, The World should iterate over all entities and pass each into a
 * {@link SystemForEW}. {@link DatastoreForEW}
 */
public abstract class EntityWorld extends World
{
    //  Fields
    protected int entities;
    
    
    //  Constructor
    public EntityWorld(SmiteTheSpiders game, StarterScreen owner)
    {
        super(game, owner);
    }
}
