package quyaze.stsj.screens;

import quyaze.stsj.GameMaster;
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
    public MainMenuScreen(GameMaster gameMaster)
    {
        super(gameMaster);
        world = new MainMenuWorld(gameMaster, this);
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
    {}
    
    
    //  Resume
    @Override
    public void resume()
    {}
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        if (width <= 0 || height <= 0) return;
        gameMaster.getViewport().update(width, height, true);
        world.resize(width, height);
    }
    
    
    //  Render
    @Override
    public void render(float delta)
    {
        world.render();
    }
    
    
    //  Dispose
    @Override
    public void dispose()
    {}
}
