package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.mainMenu.MainMenuWorld;

/** {@link Screen} for the main menu. */
public class MainMenuScreen implements Screen
{
    /*  Fields  */
    private MainMenuWorld world;
    
    
    /*  Constructor  */
    public MainMenuScreen()
    {
        world = new MainMenuWorld();
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
    @Override public void pause() {}
    
    
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
    
    
    /**
     * @return {@link World} of the main menu
     */
    public MainMenuWorld getWorld()
    {
        return world;
    }
}
