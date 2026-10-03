package quyaze.stsj.gameplay.systems;

import static quyaze.stsj.gameplay.GameplayCore.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.architecture.Avatar;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.GameplayState.State;

/** System that draws and renders {@link Avatar}s to the screen. */
public class DrawSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    private SpriteBatch batch;
    private ScreenViewport viewport;
    
    private boolean isGameOver;
    private float opacityFade;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
        
        batch = getGameInstance().getBatch();
        viewport = getGameInstance().getViewport();
        
        /*  There are three ways of seeing if it is game over. Below is one
            way
        */
        world.getScreen().state.onGameStateChanged.addBinding(
            arg -> {
                if (arg != State.GAME_OVER) return;
                
                Timer.schedule(
                    new Task()
                    {
                        @Override public void run()
                        {
                            isGameOver = false;
                            opacityFade = 1f;
                        }
                    },
                    GAME_OVER_PHASE
                );
                isGameOver = true;
                opacityFade = 1f;
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
        
        final float opacity = (
            //  Game paused?
            state.isPaused() ? MathUtils.clamp(avatar.opacity * 0.2f, 0f, 1f) :
            
            //  Game over?
            isGameOver && avatar.gameOverFade ? opacityFade :
            
            //  Default
            MathUtils.clamp(avatar.opacity, 0f, 1f)
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
    
    
    /*  Render  */
    @Override
    public void render(float dS)
    {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        /* Game over fade-out
        */
        if (isGameOver && opacityFade > 0)
        {
            opacityFade = Math.max(
                opacityFade - dS / GAME_OVER_PHASE,
                0f
            );
        }
    }
    
    
    /** Called after {@link GameplayWorld} entity iteration. */
    public void postRender()
    {
        getGameInstance().getBatch().end();
    }
}
