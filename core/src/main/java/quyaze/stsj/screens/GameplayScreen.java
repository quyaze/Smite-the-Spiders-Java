package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;

import quyaze.stsj.core.GameContext;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;

/** {@link Screen} for the gameplay. */
public class GameplayScreen extends GameContext implements Screen
{
    /*  Fields  */
    public GameplayWorld world;
    public GameplayCore core;
    public GameplayState state;
    public CollisionSolver solver;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = new GameplayWorld();
        core = new GameplayCore();
        state = new GameplayState();
        solver = new CollisionSolver();
        
        world.setOwner(this);
        core.setOwner(this);
        state.setOwner(this);
        solver.setOwner(this);
    }
    
    
    /*  Show  */
    @Override
    public void show()
    {
        world.show();
    }
    
    
    /*  Hide  */
    @Override
    public void hide()
    {
        world.hide();
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
        getGameInstance().getViewport().update(width, height, true);
        world.resize(width, height);
    }
    
    
    /*  Render  */
    @Override
    public void render(float delta)
    {
        world.render(delta);
    }
    
    
    /*  Dispose  */
    @Override public void dispose() {}
}
