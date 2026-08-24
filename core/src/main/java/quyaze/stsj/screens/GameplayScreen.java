package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;

/**
 * Screen for the gameplay.
 */
public class GameplayScreen implements Screen
{
    /*  Fields  */
    static public GameplayWorld world;
    static public GameplayCore core;
    static public GameplayState state;
    static public CollisionSolver solver;
    
    
    /*  Constructor  */
    public GameplayScreen()
    {
        final boolean postWorld = world == null;
        
        if (world == null) world = new GameplayWorld();
        if (core == null) core = new GameplayCore();
        if (state == null) state = new GameplayState();
        if (solver == null) solver = new CollisionSolver();
        
        if (postWorld) world.postConstruct();
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
        SmiteTheSpiders.getViewport().update(width, height, true);
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
