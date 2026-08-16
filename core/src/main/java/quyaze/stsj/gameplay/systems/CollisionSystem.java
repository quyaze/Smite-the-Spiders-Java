package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.utils.IntArray;

import quyaze.stsj.core.EWSystem;

/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection, etc.
 */
public class CollisionSystem implements EWSystem
{
    //  Fields
    public int defaultCapacity;
    public IntArray collidableEntities;
    
    
    //  Constructor
    
    /** Start system with an initial capacity. */
    public CollisionSystem(int initialCapacity)
    {
        collidableEntities = new IntArray(false, initialCapacity);
        defaultCapacity = initialCapacity;
    }
}
