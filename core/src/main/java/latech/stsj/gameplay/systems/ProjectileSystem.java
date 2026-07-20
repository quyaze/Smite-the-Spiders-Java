/*
//      ProjectileSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.templates.System;


/**
 * System for projectiles.
 */
public class ProjectileSystem extends System
{
    //  Field
    GameplayWorld world;
    Rectangle rectScreen;
    
    
    //  Concstructor
    public ProjectileSystem(GameplayWorld world)
    {
        this.world = world;
        rectScreen = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, int entity)
    {
        Collision collision = world.collisionStore.get(entity);
        if (collision.collision.overlaps(rectScreen))
        {
            //  Collision detected
        }
        else
        {
            
        }
        //  If (not screen contained) world.entityRemove.add(entity);
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
