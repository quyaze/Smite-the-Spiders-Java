package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;

public class SpiderSystem implements SystemForEW
{
    //  Fields
    final private GameplayEntityWorld world;
    
    
    //  Constructor
    public SpiderSystem(GameplayEntityWorld world)
    {
        this.world = world;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        final Avatar avatar = world.avatarDatastore.get(entity);
        final Mobility mobility = world.mobilityDatastore.get(entity);
        final Spider spider = world.spiderDatastore.get(entity);
        
        if (avatar.position.dst2(spider.destination) < 36f)
        {
            spider.destination.set(
                MathUtils.random(
                    0,
                    Gdx.graphics.getWidth() - avatar.getTrueWidth()
                ),
                MathUtils.random(
                    Gdx.graphics.getHeight() * 0.5f,
                    Gdx.graphics.getHeight() - avatar.getTrueHeight()
                )
            );
            mobility.setDirection(spider.destination.cpy().sub(avatar.position));
            mobility.setSpeed(Spider.randomSpeed());
        }
    }
}
