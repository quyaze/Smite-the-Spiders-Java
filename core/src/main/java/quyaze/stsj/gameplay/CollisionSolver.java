package quyaze.stsj.gameplay;

import com.badlogic.gdx.utils.IntArray;

import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.eventDefs.OnCollided;
import quyaze.stsj.screens.GameplayScreen;

/** An engine that detects collision. */
public class CollisionSolver
{
    //  Fields
    final private GameplayScreen owner;
    
    private IntArray targetEntities;
    private Collision collisionA, collisionB;
    private int entityA, entityB;
    
    /*  Target entities is a pointer to collisionSystem's
        `collidableEntities` (IntArray). This is an array of all
        entities with Collision data.
    */
    
    
    //  Constructor
    /**
     * Create the solver pending the reference to all collidable
     * entites.
     * <p></p>
     * Should call {@link #setCollisionEntitiesReference(IntArray)}.
    */
    public CollisionSolver(GameplayScreen owner)
    {
        this.owner = owner;
    }
    
    /** Keep a reference to the CollisionSystem's entities. */
    public CollisionSolver(GameplayScreen owner, IntArray collidableEntities)
    {
        this(owner);
        setCollisionEntitiesReference(collidableEntities);
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
            to receive deleted entities and deleted collision data.
        */
        
        for (int i = 0; i < targetEntities.size; i++)
        {
            entityA = targetEntities.get(i);
            collisionA = owner.world.collisionDatastore.get(entityA);
            if (collisionA == null || collisionA.skipSolving) continue;
            
            for (int j = i + 1; j < targetEntities.size; j++)
            {
                entityB = targetEntities.get(j);
                collisionB = owner.world.collisionDatastore.get(entityB);
                if (collisionB == null || collisionB.skipSolving) continue;
                
                /*  Collision detection is calculated here.
                */
                if (collisionA.collisionBox.overlaps(collisionB.collisionBox))
                {
                    owner.eventHub.fireEvent(
                        "onColliding",
                        new OnCollided(entityA, entityB, collisionA, collisionB)
                    );
                    owner.eventHub.fireEvent(
                        "onColliding",
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
        owner.eventHub.fireEvent("onSolverCleanup", null);
    }
}
