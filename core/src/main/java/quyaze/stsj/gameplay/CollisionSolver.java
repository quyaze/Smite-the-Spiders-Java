package quyaze.stsj.gameplay;

import com.badlogic.gdx.utils.IntArray;

import quyaze.stsj.core.Event;
import quyaze.stsj.core.Signal;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.eventDefs.OnCollided;
import quyaze.stsj.gameplay.systems.CollisionSystem;
import quyaze.stsj.screens.GameplayScreen;

/**
 * A static subsystem for {@link GameplayScreen}.
 * <p></p>
 * An engine that detects collision. Must access
 * {@link CollisionSystem#collidableEntities}.
*/
public class CollisionSolver
{
    /*  Fields  */
    private IntArray targetEntities;
    private Collision collisionA, collisionB;
    private int entityA, entityB;
    
    public Event<OnCollided> onCollided;
    public Signal onSolverCleanup;
    
    
    /*  Constructor  */
    public CollisionSolver()
    {
        onCollided = new Event<>();
        onSolverCleanup = new Signal();
    }
    
    
    /** Set the reference to the collidable entities. */
    public void setCollisionEntitiesReference(IntArray reference)
    {
        targetEntities = reference;
    }
    
    
    /** Solve collision. */
    public void solve()
    {
        if (targetEntities == null) throw new IllegalStateException("reference to collision entities is not assigned");
        
        /*  Null guards exist because collidableEntities is not yet designed
            to be fully in sync with World entities. This allows the solver
            to receive deleted entities and deleted Collision data.
        */
        
        for (int i = 0; i < targetEntities.size; i++)
        {
            entityA = targetEntities.get(i);
            collisionA = GameplayScreen.world.collisionDatastore.get(entityA);
            if (collisionA == null || collisionA.skipSolving) continue;
            
            for (int j = i + 1; j < targetEntities.size; j++)
            {
                entityB = targetEntities.get(j);
                collisionB = GameplayScreen.world.collisionDatastore.get(entityB);
                if (collisionB == null || collisionB.skipSolving) continue;
                
                /*  Collision detection is calculated here.
                */
                if (collisionA.collisionBox.overlaps(collisionB.collisionBox))
                {
                    onCollided.fire(
                        new OnCollided(entityA, entityB, collisionA, collisionB)
                    );
                    onCollided.fire(
                        new OnCollided(entityB, entityA, collisionB, collisionA)
                    );
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
        onSolverCleanup.fire();
    }
}
