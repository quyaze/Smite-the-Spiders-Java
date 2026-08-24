package quyaze.stsj.gameplay.systems;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;
import quyaze.stsj.screens.GameplayScreen;

/** System that enables {@link Spider} action. */
public class SpiderSystem implements EWSystem
{
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = GameplayScreen.world.avatarDatastore.get(entity);
        Mobility mobility = GameplayScreen.world.mobilityDatastore.get(entity);
        Spider spider = GameplayScreen.world.spiderDatastore.get(entity);
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f) spider.newPath(avatar, mobility);
    }
}
