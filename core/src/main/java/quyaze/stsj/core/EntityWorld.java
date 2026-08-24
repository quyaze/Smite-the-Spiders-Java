package quyaze.stsj.core;

import quyaze.stsj.SmiteTheSpiders;

/**
 * A {@link World} with entities (integer), allowing data-oriented
 * design with {@link EWDatastore} and {@link EWSystem}.
 */
public abstract class EntityWorld extends World
{
    /*  Fields  */
    protected int entities;
    
    
    /*  Constructor  */
    public EntityWorld(SmiteTheSpiders game, StarterScreen owner)
    {
        super(game, owner);
    }
}
