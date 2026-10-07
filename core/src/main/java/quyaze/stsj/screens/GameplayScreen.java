package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;

import quyaze.stsj.core.template.GameContext;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;

/**
 * <hr>
 * {@link Screen} for the gameplay.
*/
final public class GameplayScreen extends GameContext implements Screen
{
    /*  Fields  */
    private GameplayWorld world;
    private GameplayCore core;
    private GameplayState state;
    private CollisionSolver solver;
    
    private boolean resizedOnShow = true;
    
    
    /*  Constructor  */
    /** <hr> */
    public GameplayScreen()
    {
        world = new GameplayWorld();
        core = new GameplayCore();
        state = new GameplayState();
        solver = new CollisionSolver();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world.setScreen(this);
        core.setScreen(this);
        state.setScreen(this);
        solver.setScreen(this);
    }
    
    
    /*  Show  */
    @Override
    public void show()
    {
        state.setGamePaused(false);
        core.show();
    }
    
    
    /*  Hide  */
    @Override
    public void hide()
    {
        state.setGamePaused(false);
        world.hide();
        state.hide();
        resizedOnShow = true;
    }
    
    
    /*  Pause  */
    @Override
    public void pause()
    {
        state.setGamePaused(true);
    }
    
    
    /*  Resume  */
    @Override public void resume() {}
    
    
    /*  Resize  */
    @Override
    public void resize(int width, int height)
    {
        if (width <= 0 || height <= 0) return;
        
        var viewport = getGameInstance().getViewport();
        
        viewport.update(width, height, true);
        world.resize(width, height);
        if (resizedOnShow) resizedOnShow = false;
        else state.setGamePaused(true);
    }
    
    
    /*  Render  */
    @Override
    public void render(final float dS)
    {
        world.render(dS);
        core.render(dS);
    }
    
    
    /*  Dispose  */
    @Override public void dispose() {}
    
    
    /**
     * <hr>
     * @return Dedicated {@link CollisionSolver}
    */
    public CollisionSolver getGCollisionSolver()
    {
        return solver;
    }
    
    
    /**
     * <hr>
     * @return Dedicated {@link GameplayCore}
    */
    public GameplayCore getGCore()
    {
        return core;
    }
    
    
    /**
     * <hr>
     * @return Dedicated {@link GameplayState}
    */
    public GameplayState getGState()
    {
        return state;
    }
    
    
    /**
     * <hr>
     * @return Dedicated {@link GameplayWorld}
    */
    public GameplayWorld getGWorld()
    {
        return world;
    }
}
