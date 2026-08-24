package quyaze.stsj.screens;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.EventHub;
import quyaze.stsj.core.StarterScreen;
import quyaze.stsj.gameplay.CollisionSolver;
import quyaze.stsj.gameplay.GameplayCore;
import quyaze.stsj.gameplay.GameplayState;
import quyaze.stsj.gameplay.GameplayWorld;

/**
 * Screen for the gameplay.
 */
public class GameplayScreen extends StarterScreen
{
    //  Fields
    final public GameplayWorld world;
    final public GameplayCore core;
    final public GameplayState state;
    final public CollisionSolver solver;
    final public EventHub eventHub;
    
    
    //  Constructor
    public GameplayScreen()
    {
        super(game);
        world = new GameplayWorld(game, this);
        core = new GameplayCore(game);
        state = new GameplayState(this);
        solver = new CollisionSolver(this);
        eventHub = new EventHub(12, SmiteTheSpiders.getTimer());
    }
    
    
    //  Show
    @Override
    public void show()
    {
        world.show();
    }
    
    
    //  Hide
    @Override
    public void hide()
    {
        world.hide();
    }
    
    
    //  Pause
    @Override
    public void pause()
    {
        state.setGamePaused(true);
    }
    
    
    //  Resume
    @Override public void resume() {}
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        if (width <= 0 || height <= 0) return;
        SmiteTheSpiders.getViewport().update(width, height, true);
        world.resize(width, height);
    }
    
    
    //  Render
    @Override
    public void render(float delta)
    {
        world.render(delta);
    }
    
    
    //  Dispose
    @Override public void dispose() {}
}
