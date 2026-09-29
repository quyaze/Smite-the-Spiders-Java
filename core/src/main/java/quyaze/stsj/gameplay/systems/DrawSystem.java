package quyaze.stsj.gameplay.systems;

import static quyaze.stsj.gameplay.GameplayCore.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.core.architecture.Avatar;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;

/** System that draws and renders {@link Avatar}s to the screen. */
public class DrawSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    private boolean isGameOver;
    private float opacityOverride;
    /*  Opacity override is set in render() and iterate(). It is set
    */
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
        
        /*  There are three ways of seeing if it is game over. Below is one
            way
        */
        world.getScreen().core.onGameOver.addBinding(
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
                    GAME_OVER_PHASE
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
        
        SpriteBatch batch = getGameInstance().getBatch();
        GameplayState state = world.getScreen().state;
        
        if (state.isPaused()) batch.setColor(1f, 1f, 1f, MathUtils.clamp(avatar.opacity * 0.2f, 0f, 1f));
        else if (isGameOver && avatar.gameOverFade) batch.setColor(1f, 1f, 1f, opacityOverride);
        else batch.setColor(1f, 1f, 1f, MathUtils.clamp(avatar.opacity, 0f, 1f));
        
        batch.draw(
            avatar.texture,
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
        batch.setColor(Color.WHITE);
    }
    
    
    /** On {@link GameplayWorld#render(float)}. */
    public void render(float dS)
    {
        if (isGameOver && opacityOverride > 0)
        {
            opacityOverride = Math.max(
                opacityOverride - dS / GAME_OVER_PHASE,
                0f
            );
        }
    }
}
