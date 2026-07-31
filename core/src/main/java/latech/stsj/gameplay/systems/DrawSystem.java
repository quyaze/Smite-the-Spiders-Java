/*
//      DrawSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.structure.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * Draw/render entities to screen.
 */
public class DrawSystem implements System
{
    //  Fields
    private SpriteBatch batch;
    private GameplayWorld world;
    
    
    //  Constructor
    public DrawSystem(GameplayWorld world, SpriteBatch batch)
    {
        this.batch = batch;
        this.world = world;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity.getId());
        
        batch.setColor(1f, 1f, 1f, drawable.transparency);
        batch.draw(drawable.tex, drawable.position.x, drawable.position.y, drawable.getTrueWidth(), drawable.getTrueHeight());
    }
}
