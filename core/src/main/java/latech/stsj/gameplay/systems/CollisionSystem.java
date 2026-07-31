/*
//      CollisionSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.structure.Collision;
import latech.stsj.gameplay.structure.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * TODO: javadoc
 */
public class CollisionSystem implements System
{
    //  Fields
    private GameplayWorld world;
    private Array<Entity> entities;
    private IntIntMap idToIndex;
    
    private Rectangle screen;
    
    
    //  Constructor
    public CollisionSystem(GameplayWorld world)
    {
        this.world = world;
        entities = new Array<>(false, 64);
        idToIndex = new IntIntMap(64);
        screen = new Rectangle(
            0f,
            0f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        int id = entity.getId();
        
        TextureDrawable drawable = world.textureDrawableStore.get(id);
        Collision collision = world.collisionStore.get(id);
        
        /*  Cull anything that is outside the screen,
            particularly projectiles.
        */
        
        collision.update(drawable);
        
        if (collision.screenCullable && shouldCull(collision.rectangle))
        {
            if (idToIndex.containsKey(id))
            {
                entities.removeIndex(idToIndex.get(id, -1));
                world.removeEntities(entity);
            }
            return;
        }
        
        if (idToIndex.containsKey(id)) return;
        
        idToIndex.put(id, entities.size);
        entities.add(entity);
    }
    
    
    /**
     * Calculate collisions.
    */
    public void solve()
    {
        for (int i = 0; i < entities.size; i++)
        {
            Entity entityA = entities.get(i);
            Collision collisionA = world.collisionStore.get(entityA.getId());
            
            collisionA.collidingEntities.clear();
            for (int j = i + 1; j < entities.size; j++)
            {
                Entity entityB = entities.get(j);
                Collision collisionB = world.collisionStore.get(entityB.getId());
                
                if (!collisionA.rectangle.overlaps(collisionB.rectangle)) continue;
                
                collisionA.collidingEntities.add(entityB);
                collisionB.collidingEntities.add(entityA);
            }
        }
        clean();
    }
    
    
    /**
     * Post-solve cleanup or when exiting to the main menu.
    */
    public void clean()
    {
        entities.clear();
        idToIndex.clear(64);
    }
    
    
    /**
     * @param projectile {@link Rectangle}
     * @return Is projectile completely off the screen
     */
    private boolean shouldCull(Rectangle projectile)
    {
        return !projectile.overlaps(screen);
    }
}
