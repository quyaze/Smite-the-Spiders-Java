package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.SystemForEW;
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
        solver = new CollisionSolver();
        collidableEntities = new IntArray(false, initialCapacity);
        collidableEntityToIndex = new IntIntMap(initialCapacity);
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
    
    
    /** Call to solve collision. */
    public void solve()
    {
        solver.solve();
    }
    
    
    /** Call to clean the solver. */
    public void clean()
    {
        solver.clean();
    }
    
    
    /** Helper class in charge of collision detection. */
    final private class CollisionSolver
    {
        //  Fields
        private Collision collisionA, collisionB;
        private int entityA, entityB;
        
        
        /** Solve collision. */
        public void solve()
        {
            for (int i = 0; i < collidableEntities.size; i++)
            {
                entityA = collidableEntities.get(i);
                collisionA = world.collisionDatastore.get(entityA);
                
                if (collisionA.skipSolving) continue;
                for (int j = i + 1; j < collidableEntities.size; j++)
                {
                    entityB = collidableEntities.get(j);
                    collisionB = world.collisionDatastore.get(entityB);
                    
                    if (collisionB.skipSolving) continue;
                    if (collisionA.collisionBox.overlaps(collisionB.collisionBox))
                    {
                        collisionA.onCollided(entityB, collisionB);
                        collisionB.onCollided(entityA, collisionA);
                    }
                }
            }
            clean();
        }
        
        
        /** Solver cleanup. */
        public void clean()
        {
            entityA = 0; entityB = 0;
            collisionA = null; collisionB = null;
            collidableEntities.clear();
            collidableEntityToIndex.clear(defaultCapacity);
        }
    }
}
