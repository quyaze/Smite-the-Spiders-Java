package quyaze.stsj.screens;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.StarterScreen;

/**
 * Screen for the main menu.
 */
public class MainMenuScreen extends StarterScreen
{
    //  Fields
    
    
    //  Constructor
    public MainMenuScreen(GameMaster gameMaster)
    {
        super(gameMaster);
    }
    
    
    //  Show
    @Override
    public void show()
    {
        
    }
    
    
    //  Hide
    @Override
    public void hide()
    {
        
    }
    
    
    //  Pause
    @Override
    public void pause()
    {
        
    }
    
    
    //  Resume
    @Override
    public void resume()
    {
        
    }
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        if (width <= 0 || height <= 0) return;
        gameMaster.getViewport().update(width, height, true);
    }
    
    
    //  Render
    @Override
    public void render(float delta)
    {
        
    }
    
    
    //  Dispose
    @Override
    public void dispose()
    {
        
    }
}
