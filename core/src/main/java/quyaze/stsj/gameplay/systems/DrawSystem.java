package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;

/** System that draws and renders {@link Avatar}s to the screen. */
public class DrawSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Create  */
    @Override
    public void create()
    {
        // getWorld().getScreen().core.onGameOver.bindDeferred(
        //     () -> {
                
        //     }
        // );
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = getWorld().getScreen().world.avatarDatastore.get(entity);
        
        SpriteBatch batch = getWorld().getScreen().getGameInstance().getBatch();
        final float opacity = MathUtils.clamp(
            getWorld().getScreen().state.isPaused() ? avatar.opacity * 0.2f : avatar.opacity,
            0,
            1f
        );
        
        batch.setColor(1f, 1f, 1f, opacity);
        batch.draw(
            avatar.texture,
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
        batch.setColor(Color.WHITE);
    }
}
