package quyaze.stsj.gameplay.systems;

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
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f) spider.newPath(avatar, mobility);
    }
}
