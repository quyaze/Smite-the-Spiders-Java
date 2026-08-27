package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Spider;

/** System that enables {@link Spider} action. */
public class SpiderSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private boolean isGameOver;
    private float opacityOverride;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        getWorld().getScreen().core.onGameOver.bindDeferred(
            () -> {
                opacityOverride = 1f;
                isGameOver = true;
            }
        );
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = getWorld().avatarDatastore.get(entity);
        Mobility mobility = getWorld().mobilityDatastore.get(entity);
        Spider spider = getWorld().spiderDatastore.get(entity);
        
        ScreenViewport viewport = getWorld().getScreen().getGameInstance().getViewport();
        final float dS = Gdx.graphics.getDeltaTime();
        
        if (isGameOver && opacityOverride > 0)
        {
            opacityOverride = Math.max(
                opacityOverride - dS / GameplayCore.GAME_OVER_PHASE,
                0
            );
            avatar.opacity = opacityOverride;
        }
        
        if (avatar.position.dst2(spider.destination.cpy()) < 36f)
        {
            spider.newPath(
                viewport,
                avatar,
                mobility
            );
        }
    }
}
