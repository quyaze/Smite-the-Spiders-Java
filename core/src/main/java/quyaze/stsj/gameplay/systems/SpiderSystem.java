package quyaze.stsj.gameplay.systems;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;

/** System that enables {@link Spider} action. */
public class SpiderSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Create  */
    @Override
    public void create() {}
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = getWorld().getOwner().world.avatarDatastore.get(entity);
        Mobility mobility = getWorld().getOwner().world.mobilityDatastore.get(entity);
        Spider spider = getWorld().getOwner().world.spiderDatastore.get(entity);
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f) spider.newPath(avatar, mobility);
    }
}
