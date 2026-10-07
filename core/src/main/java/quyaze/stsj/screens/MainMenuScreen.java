package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.template.GameContext;
import quyaze.stsj.mainMenu.MainMenuWorld;

/**
 * <hr>
 * {@link Screen} for the main menu.
*/
final public class MainMenuScreen extends GameContext implements Screen
{
    /*  Fields  */
    private MainMenuWorld world;
    
    
    /*  Constructor  */
    /** <hr> */
    public MainMenuScreen()
    {
        world = new MainMenuWorld();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world.setScreen(this);
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
        
        ScreenViewport viewport = getGameInstance().getViewport();
        
        viewport.update(width, height, true);
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
     * <hr>
     * @return Dedicated {@link MainMenuWorld}
    */
    public MainMenuWorld getMMWorld()
    {
        return world;
    }
}
