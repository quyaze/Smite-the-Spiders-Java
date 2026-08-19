package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Projectile;

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
    final private Rectangle screen;
    
    
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
        screen = new Rectangle();
        screen.setSize(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        final Collision collision = world.collisionDatastore.get(entity);
        final Projectile projectile = world.projectileDatastore.get(entity);
        
        collision.update();
        
        /*  Cull projectiles that have left the screen.
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
     * @return Collision solver
     */
    public CollisionSolver getSolver()
    {
        return solver;
    }
    
    
    /**
     * On owning {@code GameplayScreen.resize()}.
     * <p></p>
     * Adjusts screen culling parameters.
    */
    public void resize(int width, int height)
    {
        screen.setSize(width, height);
    }
}
