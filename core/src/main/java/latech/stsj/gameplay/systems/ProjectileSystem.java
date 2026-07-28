/*
//      ProjectileSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for projectiles.
 */
public class ProjectileSystem implements System
{
    //  Field
    private GameplayWorld world;
    private Stores<TextureDrawable> textureDrawableStore;
    private Stores<Collision> collisionStore;
    private Rectangle rectScreen;
    
    
    //  Constructor
    public ProjectileSystem(GameplayWorld world)
    {
        this.world = world;
        textureDrawableStore = world.textureDrawableStore;
        collisionStore = world.collisionStore;
        rectScreen = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = textureDrawableStore.get(entity.getId());
        Collision collision = collisionStore.get(entity.getId());
        
        collision.update(drawable);
        if (!collision.collision.overlaps(rectScreen)) world.removeEntity(entity);
    }
}
