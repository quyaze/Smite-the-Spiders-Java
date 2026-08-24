package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.screens.GameplayScreen;
/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection.
 */
final public class CollisionSystem implements EWSystem
{
    /*  Fields  */
    private IntArray collidableEntities;
    private IntIntMap collidableEntityToIndex;
    private Rectangle screen;
    
    
    /*  Constructor  */
    
    /** Start system with an initial capacity. */
    public CollisionSystem(int initialCapacity)
    {
        collidableEntities = new IntArray(false, initialCapacity);
        collidableEntityToIndex = new IntIntMap(initialCapacity);
        screen = new Rectangle();
    }
    
    
    /** Post-construct. */
    public void postConstruct()
    {
        GameplayScreen.solver.setCollisionEntitiesReference(collidableEntities);
        GameplayScreen.solver.onSolverCleanup.bindDeferred(
            () -> {
                collidableEntityToIndex.clear();
                collidableEntities.clear();
            }
        );
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Collision collision = GameplayScreen.world.collisionDatastore.get(entity);
        Projectile projectile = GameplayScreen.world.projectileDatastore.get(entity);
        
        collision.updatePosition();
        
        /*  Cull projectiles that have left the screen
        */
        if (projectile != null && !collision.collisionBox.overlaps(screen))
        {
            GameplayScreen.world.removeEntityRequest(entity);
            return;
        }
        
        if (collidableEntityToIndex.containsKey(entity)) return;
        
        collidableEntityToIndex.put(entity, collidableEntities.size);
        collidableEntities.add(entity);
    }
    
    
    /**
     * On {@link GameplayScreen#resize(int, int)}.
    */
    public void resize(int width, int height)
    {
        screen.setSize(width, height);
    }
}
