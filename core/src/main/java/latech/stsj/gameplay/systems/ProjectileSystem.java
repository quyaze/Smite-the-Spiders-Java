/*
//      ProjectileSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for projectiles.
 */
public class ProjectileSystem extends System
{
    //  Field
    GameplayWorld world;
    Rectangle rectScreen;
    
    
    //  Constructor
    public ProjectileSystem(GameplayWorld world)
    {
        stores = new Stores[] {
            world.textureDrawableStore,
            world.mobilityStore,
            world.collisionStore
        };
        this.world = world;
        rectScreen = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, int entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity);
        Collision collision = world.collisionStore.get(entity);
        collision.update(drawable);
        if (!collision.collision.overlaps(rectScreen)) world.deferEntityRemoval(entity);
    }
    
    
    /**
     * @param width
     * @param height
     */
    public void resize(int width, int height)
    {
        rectScreen.width = width;
        rectScreen.height = height;
    }
}
