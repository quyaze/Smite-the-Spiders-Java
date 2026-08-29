package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.WorldContext;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;

/** System that enables {@link Spider} action. */
public class SpiderSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    private boolean isGameOver;
    private float opacityOverride;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
        world.getScreen().core.onGameOver.bindDeferred(
            () -> {
                Timer.schedule(
                    new Task()
                    {
                        @Override public void run()
                        {
                            isGameOver = false;
                            opacityOverride = 1f;
                        }
                    },
                    GameplayCore.GAME_OVER_PHASE
                );
                
                isGameOver = true;
                opacityOverride = 1f;
            }
        );
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        Spider spider = world.spiderDatastore.get(entity);
        
        if (isGameOver) avatar.opacity = opacityOverride;
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f)
        {
            spider.newPath(
                avatar,
                mobility,
                GameplayWorld.UNITS_PER_PIXEL
            );
        }
    }
    
    
    /** On {@link GameplayWorld#render(float)}. */
    public void render(float dS)
    {
        if (isGameOver && opacityOverride > 0)
        {
            opacityOverride = Math.max(
                opacityOverride - dS / GameplayCore.GAME_OVER_PHASE,
                0
            );
        }
    }
}
