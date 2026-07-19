/*
//      GameplayScreen.java
*/


package latech.stsj.screens;

import latech.stsj.Main;
import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.templates.StarterScreen;

public class GameplayScreen extends StarterScreen
{
    //  Fields
    private GameplayWorld world;
    
    
    //  Constructor
    public GameplayScreen(Main main)
    {
        this.main = main;
        
        world = new GameplayWorld(main);
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds)
    {
        world.render(deltaSeconds);
    }
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        main.getViewport().update(width, height, true);
        world.resize(width, height);
    }
    
    
    //  Show
    @Override
    public void show()
    {}
    
    
    //  Hide
    @Override
    public void hide()
    {}
    
    
    //  Pause
    @Override
    public void pause()
    {}
    
    
    //  Resume
    @Override
    public void resume()
    {}
    
    
    //  Dispose
    @Override
    public void dispose()
    {}
}
