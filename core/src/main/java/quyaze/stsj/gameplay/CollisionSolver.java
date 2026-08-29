package quyaze.stsj.gameplay;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.core.Event;
import quyaze.stsj.core.ScreenContext;
import quyaze.stsj.core.Signal;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.eventDefs.OnCollided;
import quyaze.stsj.screens.GameplayScreen;

/**
 * A subsystem for {@link GameplayScreen}.
 * <p></p>
 * An engine that detects collision. Must access
 * {@code CollisionSystem.collidableEntities}.
*/
public class CollisionSolver extends ScreenContext<GameplayScreen>
{
    /*  Fields  */
    private GameplayScreen screen;
    
    public IntArray targetEntities;
    private Collision collisionA, collisionB;
    private int entityA, entityB;
    
    public Event<OnCollided> onCollided;
    public Signal onSolverCleanup;
    
    final static private float SOLVE_INTERVAL = 1 / 24f;
    private Task collisionSolveTask;
    private float timeToNextSolve;
    
    
    /*  Constructor  */
    public CollisionSolver()
    {
        onCollided = new Event<>();
        onSolverCleanup = new Signal();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        screen = getScreen();
        collisionSolveTask = new Task()
        {
            @Override
            public void run()
            {
                solve();
            }
        };
        screen.state.onPausedStateChanged.bindDeferred(
            arg -> {
                setSolverEnabled(!arg);
            }
        );
    }
    
    
    /** Enable to disable the solver. */
    private void setSolverEnabled(boolean enabled)
    {
        /*  Some functionality below makes the solver appear to freeze when
            the player is pausing during gameplay. When unpausing, the
            solver resumes not with the full interval delay, but rather, the
            time after when it was supposed to run next. A small detail
            although it is highly unnoticeable since the solver is frame-
            scheduled.
        */
        
        if (enabled == collisionSolveTask.isScheduled()) return;
        if (enabled)
        {
            Timer.schedule(
                collisionSolveTask,
                timeToNextSolve,
                SOLVE_INTERVAL
            );
        }
        else
        {
            final long then = collisionSolveTask.getExecuteTimeMillis();
            final long now = System.nanoTime() / 1000000;
            timeToNextSolve = (then - now) * 0.001f;
            collisionSolveTask.cancel();
        }
    }
    
    
    /** Solve collision. */
    private void solve()
    {
        if (targetEntities == null)
            throw new IllegalStateException("reference to collision entities is not assigned");
        
        /*  Null guards exist because collidableEntities is not yet designed
            to be fully in sync with World entities. This allows the solver
            to receive deleted entities and deleted Collision data.
        */
        
        for (int i = 0; i < targetEntities.size; i++)
        {
            entityA = targetEntities.get(i);
            collisionA = screen.world.collisionDatastore.get(entityA);
            if (collisionA == null || collisionA.skipSolving) continue;
            
            for (int j = i + 1; j < targetEntities.size; j++)
            {
                entityB = targetEntities.get(j);
                collisionB = screen.world.collisionDatastore.get(entityB);
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
