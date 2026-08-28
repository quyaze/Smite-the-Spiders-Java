package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.WorldContext;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.GameplayState.State;
import quyaze.stsj.gameplay.architecture.Avatar;

/** System that draws and renders {@link Avatar}s to the screen. */
public class DrawSystem extends WorldContext<GameplayWorld> implements EWSystem
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
        
        SpriteBatch batch = getGameInstance().getBatch();
        GameplayState state = getWorld().getScreen().state;
        final boolean gameOver = state.getState() == State.GAME_OVER;
        
        final float opacity = MathUtils.clamp(
            state.isPaused() && !gameOver ? avatar.opacity * 0.2f : avatar.opacity,
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
