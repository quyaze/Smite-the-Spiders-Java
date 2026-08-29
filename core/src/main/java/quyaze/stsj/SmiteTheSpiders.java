package quyaze.stsj;

import java.io.File;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.GameText;
import quyaze.stsj.screens.GameplayScreen;
import quyaze.stsj.screens.MainMenuScreen;

/** The game entry point and definition. */
final public class SmiteTheSpiders extends Game
{
    /*  Fields  */
    static private boolean lock;
    
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private TextureAtlas atlas;
    private Music music;
    private GameText gameText;
    
    private MainMenuScreen mainMenuScreen;
    private GameplayScreen gameplayScreen;
    
    
    /*  Constructor  */
    /** New game instance. */
    public SmiteTheSpiders()
    {
        if (lock)
            throw new IllegalStateException("attempt to create another game instance");
        lock = true;
        
        mainMenuScreen = new MainMenuScreen();
        gameplayScreen = new GameplayScreen();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        atlas = new TextureAtlas(
            Gdx.files.internal("packed" + File.separator + "packed.atlas"),
            Gdx.files.internal("packed")
        );
        gameText = new GameText();
        music = Gdx.audio.newMusic(Gdx.files.internal("audio" + File.separator + "Lord of the Land.mp3"));
        
        mainMenuScreen.setGameInstance(this);
        gameplayScreen.setGameInstance(this);
        
        music.setLooping(true);
        toMainMenuScreen();
        music.play();
    }
    
    
    /*  Dispose  */
    @Override
    public void dispose()
    {
        super.dispose();
        batch.dispose();
        atlas.dispose();
        music.dispose();
        gameText.dispose();
        mainMenuScreen.dispose();
        gameplayScreen.dispose();
    }
    
    
    /**
     * @return Global {@link SpriteBatch} for drawing
     */
    public SpriteBatch getBatch()
    {
        return batch;
    }
    
    
    /**
     * @return Global viewport ({@link ScreenViewport})
     */
    public ScreenViewport getViewport()
    {
        return viewport;
    }
    
    
    /**
     * @return Global {@link TextureAtlas} for texture lookup
     */
    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    
    
    /**
     * @return Globally played {@link Music}
     */
    public Music getMusic()
    {
        return music;
    }
    
    
    /**
     * @return Global {@link GameText} utility
     */
    public GameText getGameText()
    {
        return gameText;
    }
    
    
    /** Switch to the {@link MainMenuScreen.} */
    public void toMainMenuScreen()
    {
        if (screen == mainMenuScreen) return;
        setScreen(mainMenuScreen);
    }
    
    
    /** Switch to the {@link GameplayScreen}. */
    public void toGameplayScreen()
    {
        if (screen == gameplayScreen) return;
        setScreen(gameplayScreen);
    }
}