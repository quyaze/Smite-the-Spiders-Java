/*
//      SpiderSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.Spider;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.System;

public class SpiderSystem extends System
{
    //  Fields
    GameplayWorld world;
    
    
    //  Constructor
    public SpiderSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, int entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity);
        Mobility mobility = world.mobilityStore.get(entity);
        Spider spider = world.spiderStore.get(entity);
        
        if (drawable.position.dst2(spider.destination) < 36f)
        {
            spider.destination.set(
                MathUtils.random(0, Gdx.graphics.getWidth() - drawable.getTrueWidth()),
                MathUtils.random(Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getHeight() - drawable.getTrueHeight())
            );
            mobility.setDirection(spider.destination.cpy().sub(drawable.position));
            mobility.setSpeed(spider.randomSpeed());
        }
    }
}
