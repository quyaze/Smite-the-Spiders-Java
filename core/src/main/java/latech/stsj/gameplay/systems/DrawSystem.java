/*
//      DrawSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for drawing or screen rendering.
 */
public class DrawSystem extends System
{
    //  Fields
    private SpriteBatch batch;
    private Stores<TextureDrawable> textureDrawableStore;
    
    
    //  Constructor
    public DrawSystem(GameplayWorld world, SpriteBatch batch)
    {
        this.batch = batch;
        textureDrawableStore = world.textureDrawableStore;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = textureDrawableStore.get(entity.getId());
        
        batch.draw(drawable.tex, drawable.position.x, drawable.position.y, drawable.getTrueWidth(), drawable.getTrueHeight());
    }
}
