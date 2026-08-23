package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.screens.GameplayScreen;
/**
 * Responsible for tracking all collidable entities.
 * {@link CollisionSolver} does the actual collision detection.
 */
final public class CollisionSystem implements SystemForEW
{
    //  Fields
    final private GameplayWorld world;
    
    public int defaultCapacity;
    public IntArray collidableEntities;
    private IntIntMap collidableEntityToIndex;
    private Rectangle screen;
    
    
    //  Constructor
    
    /** Start system with an initial capacity. */
    public CollisionSystem(GameplayScreen owner, int initialCapacity)
    {
        this.world = owner.world;
        collidableEntities = new IntArray(false, initialCapacity);
        collidableEntityToIndex = new IntIntMap(initialCapacity);
        defaultCapacity = initialCapacity;
        screen = new Rectangle();
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Collision collision = world.collisionDatastore.get(entity);
        Projectile projectile = world.projectileDatastore.get(entity);
        
        collision.updatePosition();
        
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
     * On owning {@code GameplayScreen.resize()}.
     * <p></p>
     * Adjusts screen culling parameters.
    */
    public void resize(int width, int height)
    {
        screen.setSize(width, height);
    }
}
