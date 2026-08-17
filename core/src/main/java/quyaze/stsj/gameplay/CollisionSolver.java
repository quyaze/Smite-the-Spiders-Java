package quyaze.stsj.gameplay;

import com.badlogic.gdx.utils.IntArray;

import quyaze.stsj.gameplay.architecture.Collision;

public abstract class CollisionSolver
{
    //  Fields
    final private GameplayEntityWorld world;
    final private IntArray collidableEntities;
    private Collision collisionA, collisionB;
    private int entityA, entityB;
    
    
    //  Constructor
    
    /** Keep a reference to the CollisionSystem's entities. */
    public CollisionSolver(GameplayEntityWorld world, IntArray collidableEntities)
    {
        this.world = world;
        this.collidableEntities = collidableEntities;
    }
    
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
        onCleanup();
    }
    
    
    /** Event on solver cleanup. */
    public abstract void onCleanup();
}
