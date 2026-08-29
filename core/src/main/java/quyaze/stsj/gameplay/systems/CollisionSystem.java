package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.Utility;
import quyaze.stsj.core.WorldContext;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Projectile;
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
        world.getScreen().solver.onSolverCleanup.bindDeferred(
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
        Collision collision = world.getScreen().world.collisionDatastore.get(entity);
        Projectile projectile = world.getScreen().world.projectileDatastore.get(entity);
        
        collision.updatePosition();
        
        /*  Cull projectiles that have left the screen
        */
        if (projectile != null && !collision.collisionBox.overlaps(screen))
        {
            world.getScreen().world.removeEntityRequest(entity);
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
            Utility.getScreenWorldWidth(GameplayWorld.UNITS_PER_PIXEL),
            Utility.getScreenWorldHeight(GameplayWorld.UNITS_PER_PIXEL)
        );
        /*  Or multiply width and height by unitsPerPixel
        */
    }
}
