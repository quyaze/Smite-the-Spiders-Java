package quyaze.stsj.core;

import com.badlogic.gdx.Screen;

/**
 * A {@link World} with entities (integer), allowing data-oriented
 * design with {@link EWDatastore} and {@link EWSystem}.
 */
public abstract class EntityWorld<T extends Screen> extends World<T>
{
    /*  Fields  */
    protected int entities;
    
    
    /**
     * @return The number of entites that exist
     */
    public int getEntities()
    {
        return entities;
    }
    
    
    /**
     * @return The last entity
    */
    public int lastEntity()
    {
        return entities - 1;
    }
}
