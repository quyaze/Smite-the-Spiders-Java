package quyaze.stsj.gameplay.systems;

import quyaze.stsj.core.architecture.Avatar;
import quyaze.stsj.core.architecture.Mobility;
import quyaze.stsj.core.architecture.Spider;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.gameplay.GameplayWorld;

/** System that enables {@link Spider} action. */
public class SpiderSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        Spider spider = world.spiderDatastore.get(entity);
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f)
        {
            spider.newPath(
                world,
                avatar,
                mobility
            );
        }
    }
    
    
    /*  Render  */
    @Override public void render(float dS) {}
}
