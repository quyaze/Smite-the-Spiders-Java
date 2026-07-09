package latech.stsj;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.screens.GameScreen;
import latech.stsj.screens.MainMenuScreen;

/**
 * Main - entry point into the game
 */
public class Main extends Game
{
    //  Fields
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private TextureAtlas atlas;
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    
    private Timer timer;
    private Music music;
    
    
    //  Create
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        atlas = new TextureAtlas(Gdx.files.internal("packed.atlas"));
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        timer = new Timer();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/Lord of the Land.mp3"));
        
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
        gameScreen.dispose();
        music.dispose();
    }
    
    
    //  Render
    @Override
    public void render()
    {
        super.render();
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
     * @return Screen representing the main menu
     */
    public MainMenuScreen getMainMenuScreen()
    {
        return mainMenuScreen;
    }
    
    
    /**
     * @return Screen representing gameplay
     */
    public GameScreen getGameScreen()
    {
        return gameScreen;
    }
    
    
    /**
     * @return Timer and task scheduler utility
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
}
