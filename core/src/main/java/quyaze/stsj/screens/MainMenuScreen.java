package quyaze.stsj.screens;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.StarterScreen;
import quyaze.stsj.mainMenu.MainMenuWorld;

/**
 * Screen for the main menu.
 */
public class MainMenuScreen extends StarterScreen
{
    //  Fields
    final private MainMenuWorld world;
    
    
    //  Constructor
    public MainMenuScreen(SmiteTheSpiders game)
    {
        super(game);
        world = new MainMenuWorld(game, this);
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
    @Override public void pause() {}
    
    
    //  Resume
    @Override public void resume() {}
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        if (width <= 0 || height <= 0) return;
        game.getViewport().update(width, height, true);
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
