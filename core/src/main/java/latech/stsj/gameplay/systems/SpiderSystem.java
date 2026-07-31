/*
//      SpiderSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.structure.Collision;
import latech.stsj.gameplay.structure.Mobility;
import latech.stsj.gameplay.structure.Spider;
import latech.stsj.gameplay.structure.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * System for spider pathfinding (random).
 */
public class SpiderSystem implements System
{
    //  Fields
    private GameplayWorld world;
    
    
    //  Constructor
    public SpiderSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        int id = entity.getId();
        
        TextureDrawable drawable = world.textureDrawableStore.get(id);
        Mobility mobility = world.mobilityStore.get(id);
        Collision collision = world.collisionStore.get(id);
        Spider spider = world.spiderStore.get(id);
        
        /*
        //  Is hit by the player's spell
        if (collision.collidingEntities.notEmpty())
        {
            drawable.transparency -= deltaSeconds * 1f;
            world.onSpiderHit(entity);
        }
        */
        
        
        if (drawable.position.dst2(spider.destination) < 36f)
        {
            spider.destination.set(
                MathUtils.random(0, Gdx.graphics.getWidth() - drawable.getTrueWidth()),
                MathUtils.random(Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getHeight() - drawable.getTrueHeight())
            );
            mobility.setDirection(spider.destination.cpy().sub(drawable.position));
            mobility.setSpeed(Spider.randomSpeed());
        }
    }
}
