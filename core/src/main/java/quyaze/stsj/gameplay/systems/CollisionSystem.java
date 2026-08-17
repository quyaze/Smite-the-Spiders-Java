package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Collision;

/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection.
 */
final public class CollisionSystem implements SystemForEW
{
    //  Fields
    final private GameplayEntityWorld world;
    final private CollisionSolver solver;
    
    public int defaultCapacity;
    final private IntArray collidableEntities;
    final private IntIntMap collidableEntityToIndex;
    
    
    //  Constructor
    
    /** Start system with an initial capacity. */
    public CollisionSystem(GameplayEntityWorld world, int initialCapacity)
    {
        this.world = world;
        collidableEntities = new IntArray(false, initialCapacity);
        collidableEntityToIndex = new IntIntMap(initialCapacity);
        solver = new CollisionSolver(world, collidableEntities)
        {
            @Override public void onCleanup()
            {
                collidableEntityToIndex.clear(defaultCapacity);
            }
        };
        defaultCapacity = initialCapacity;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        boolean pass = true;
        
        if (collidableEntityToIndex.containsKey(entity)) pass = false;
        if (!world.collisionDatastore.contains(entity)) pass = false;
        
        if (!pass)
        {
            if (collidableEntityToIndex.containsKey(entity))
            {
                collidableEntities.removeIndex(
                    collidableEntityToIndex.remove(entity, entity)
                );
            }
            return;
        }
        
        Collision collision = world.collisionDatastore.get(entity);
        
        collidableEntityToIndex.put(entity, collidableEntities.size);
        collidableEntities.add(entity);
        collision.update();
    }
    
    
    public CollisionSolver getSolver()
    {
        return solver;
    }
}
