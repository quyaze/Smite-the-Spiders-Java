package quyaze.stsj.screens;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.StarterScreen;
import quyaze.stsj.gameplay.GameplayEntityWorld;

/**
 * Screen for the gameplay.
 */
public class GameplayScreen extends StarterScreen
{
    //  Fields
    final private GameplayEntityWorld world;
    
    
    //  Constructor
    public GameplayScreen(GameMaster gameMaster)
    {
        super(gameMaster);
        world = new GameplayEntityWorld(this, gameMaster);
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
        world.pause();
    }
    
    
    //  Resume
    @Override public void resume() {}
    
    
    //  Resize
    @Override public void resize(int width, int height) {}
    
    
    //  Render
    @Override
    public void render(float delta)
    {
        world.render();
    }
    
    
    //  Dispose
    @Override public void dispose() {}
}
