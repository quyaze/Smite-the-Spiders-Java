/*
//      Main.java
*/


package latech.stsj;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.screens.GameplayScreen;
import latech.stsj.screens.MainMenuScreen;


/**
 * Entry way into the game.
 */
public class Main extends Game
{
    //  Fields
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private TextureAtlas atlas;
    private Timer timer;
    private Music music;
    private FreeTypeFontGenerator fontGeneric;
    
    private MainMenuScreen mainMenuScreen;
    private GameplayScreen gameplayScreen;
    
    
    //  Create
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        atlas = new TextureAtlas(Gdx.files.internal("packed.atlas"));
        timer = new Timer();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/Lord of the Land.mp3"));
        fontGeneric = new FreeTypeFontGenerator(Gdx.files.internal("fonts/generic.otf"));
        
        mainMenuScreen = new MainMenuScreen(this);
        gameplayScreen = new GameplayScreen(this);
        
        music.setLooping(true);
        setScreen(mainMenuScreen);
    }
    
    
    //  Dispose
    @Override
    public void dispose()
    {
        batch.dispose();
        atlas.dispose();
        mainMenuScreen.dispose();
        gameplayScreen.dispose();
        music.dispose();
        fontGeneric.dispose();
    }
    
    
    /**
     * @return Game's batch for other classes to draw into
     */
    public SpriteBatch getBatch()
    {
        return batch;
    }
    
    
    /**
     * @return Game's viewport
     */
    public ScreenViewport getViewport()
    {
        return viewport;
    }
    
    
    /**
     * @return Game's texture atlas for texture access
     */
    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    
    
    /**
     * Switch to the main menu screen
     */
    public void goToMainMenuScreen()
    {
        setScreen(mainMenuScreen);
    }
    
    
    /**
     * Switch to the gameplay screen
     */
    public void goToGameplayScreen()
    {
        setScreen(gameplayScreen);
    }
    
    
    /**
     * @return Game's timer and task scheduler utility
    */
    public Timer getTimer()
    {
        return timer;
    }
    
    
    /**
     * @return Globally played music
    */
    public Music getMusic()
    {
        return music;
    }
    
    
    /**
     * @return Font generator for generic text (Cinzel)
    */
    public FreeTypeFontGenerator getFontGeneric()
    {
        return fontGeneric;
    }
}
