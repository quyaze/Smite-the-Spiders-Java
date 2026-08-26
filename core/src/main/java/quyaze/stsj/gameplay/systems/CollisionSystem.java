package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.screens.GameplayScreen;
/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection.
 */
final public class CollisionSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    final private int cap;
    private IntArray collidableEntities;
    private IntIntMap collidableEntityToIndex;
    private Rectangle screen;
    
    
    /*  Constructor  */
    public CollisionSystem(int initialCapacity)
    {
        cap = initialCapacity;
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        collidableEntities = new IntArray(false, cap);
        collidableEntityToIndex = new IntIntMap(cap);
        screen = new Rectangle();
        getWorld().getOwner().solver.setCollisionEntitiesReference(collidableEntities);
        getWorld().getOwner().solver.onSolverCleanup.bindDeferred(
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
        Collision collision = getWorld().getOwner().world.collisionDatastore.get(entity);
        Projectile projectile = getWorld().getOwner().world.projectileDatastore.get(entity);
        
        collision.updatePosition();
        
        /*  Cull projectiles that have left the screen
        */
        if (projectile != null && !collision.collisionBox.overlaps(screen))
        {
            getWorld().getOwner().world.removeEntityRequest(entity);
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
