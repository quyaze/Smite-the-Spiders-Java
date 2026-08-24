package quyaze.stsj.gameplay.systems;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;

/** Handles the spider. */
public class SpiderSystem implements EWSystem
{
    //  Fields
    final private GameplayWorld world;
    
    
    //  Constructor
    public SpiderSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        Spider spider = world.spiderDatastore.get(entity);
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f) spider.newPath(avatar, mobility);
    }
}
