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
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for spiders.
 */
public class SpiderSystem implements System
{
    //  Fields
    private Stores<TextureDrawable> textureDrawableStore;
    private Stores<Mobility> mobilityStore;
    private Stores<Spider> spiderStore;
    
    
    //  Constructor
    public SpiderSystem(GameplayWorld world)
    {
        textureDrawableStore = world.textureDrawableStore;
        mobilityStore = world.mobilityStore;
        spiderStore = world.spiderStore;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = textureDrawableStore.get(entity.getId());
        Mobility mobility = mobilityStore.get(entity.getId());
        Spider spider = spiderStore.get(entity.getId());
        
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
