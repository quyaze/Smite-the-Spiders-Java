package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.architecture.Collision;
import quyaze.stsj.core.architecture.Projectile;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.core.utility.Utility;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.screens.GameplayScreen;

/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection.
 */
final public class CollisionSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    private IntArray collidableEntities;
    private IntIntMap collidableEntityToIndex;
    private Rectangle screen;
    
    
    /*  Constructor  */
    public CollisionSystem(int initialCapacity)
    {
        collidableEntities = new IntArray(false, initialCapacity);
        collidableEntityToIndex = new IntIntMap(initialCapacity);
        screen = new Rectangle();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
        world.getScreen().solver.targetEntities = collidableEntities;
        world.getScreen().solver.onSolverCleanup.addBinding(
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
        Collision collision = world.collisionDatastore.get(entity);
        Projectile projectile = world.projectileDatastore.get(entity);
        
        collision.updatePosition();
        
        /*  Cull projectiles that have left the screen
        */
        if (projectile != null && !collision.collisionBox.overlaps(screen))
        {
            world.removeEntityRequest(entity);
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
        screen.setSize(
            Utility.getWorldViewWidth(world),
            Utility.getWorldViewHeight(world)
        );
    }
}
