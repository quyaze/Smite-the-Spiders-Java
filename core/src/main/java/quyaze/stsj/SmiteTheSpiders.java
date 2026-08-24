package quyaze.stsj;

import java.io.File;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.GameText;
import quyaze.stsj.screens.GameplayScreen;
import quyaze.stsj.screens.MainMenuScreen;

/**
 * The game entry point and definition.
 * <p></p>
 * Has static utilities and instance-owned screens.
*/
final public class SmiteTheSpiders extends Game
{
    /*  Fields  */
    static private SpriteBatch batch;
    static private ScreenViewport viewport;
    static private TextureAtlas atlas;
    static private Music music;
    static private GameText gameText;
    static private Timer timer;
    
    private MainMenuScreen mainMenuScreen;
    private GameplayScreen gameplayScreen;
    
    
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
        music = Gdx.audio.newMusic(Gdx.files.internal("audio" + File.separator + "Lord of the Land.mp3"));
        gameText = new GameText();
        timer = new Timer();
        
        mainMenuScreen = new MainMenuScreen(this);
        gameplayScreen = new GameplayScreen();
        
        goToMainMenuScreen();
        music.setLooping(true);
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
    static public SpriteBatch getBatch()
    {
        return batch;
    }
    
    
    /**
     * @return Global viewport ({@link ScreenViewport})
     */
    static public ScreenViewport getViewport()
    {
        return viewport;
    }
    
    
    /**
     * @return Global {@link TextureAtlas} for texture lookup
     */
    static public TextureAtlas getAtlas()
    {
        return atlas;
    }
    
    
    /**
     * @return Globally played {@link Music}
     */
    static public Music getMusic()
    {
        return music;
    }
    
    
    /**
     * @return Global {@link GameText} utility
     */
    static public GameText getGameText()
    {
        return gameText;
    }
    
    
    /**
     * @return Global {@link Timer} utility
     */
    static public Timer getTimer()
    {
        return timer;
    }
    
    
    /**
     * Switch to the {@link MainMenuScreen}.
    */
    public void goToMainMenuScreen()
    {
        setScreen(mainMenuScreen);
    }
    
    
    /**
     * Switch to the {@link GameplayScreen}.
    */
    public void goToGameplayScreen()
    {
        setScreen(gameplayScreen);
    }
}