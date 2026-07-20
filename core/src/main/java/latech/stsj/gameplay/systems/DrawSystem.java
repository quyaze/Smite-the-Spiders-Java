/*
//      DrawSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for drawing or screen rendering.
 */
public class DrawSystem extends System
{
    //  Fields
    private GameplayWorld world;
    private SpriteBatch batch;
    
    
    //  Constructor
    public DrawSystem(GameplayWorld world, SpriteBatch batch)
    {
        stores = new Stores[] {
            world.textureDrawableStore
        };
        this.world = world;
        this.batch = batch;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, int entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity);
        
        batch.draw(drawable.tex, drawable.position.x, drawable.position.y, drawable.getTrueWidth(), drawable.getTrueHeight());
    }
}
