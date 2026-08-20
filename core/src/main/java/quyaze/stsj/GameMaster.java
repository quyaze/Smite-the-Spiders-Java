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
 * Define the game entry point.
*/
public class GameMaster extends Game
{
    //  Fields
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private TextureAtlas atlas;
    private Music music;
    private GameText gameText;
    private Timer timer;
    
    private MainMenuScreen mainMenuScreen;
    private GameplayScreen gameplayScreen;
    
    
    //  Create
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
        gameplayScreen = new GameplayScreen(this);
        
        goToMainMenuScreen();
        music.setLooping(true);
        music.play();
    }
    
    
    //  Dispose
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
     * @return Global batch for drawing
     */
    public SpriteBatch getBatch()
    {
        return batch;
    }
    
    
    /**
     * @return Global viewport
     */
    public ScreenViewport getViewport()
    {
        return viewport;
    }
    
    
    /**
     * @return Global texture atlas
     */
    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    
    
    /**
     * @return Globally played music
     */
    public Music getMusic()
    {
        return music;
    }
    
    
    /**
     * @return Global GameText
     */
    public GameText getGameText()
    {
        return gameText;
    }
    
    
    /**
     * @return Global Timer utility
     */
    public Timer getTimer()
    {
        return timer;
    }
    
    
    /**
     * Switch to the main menu screen.
    */
    public void goToMainMenuScreen()
    {
        setScreen(mainMenuScreen);
    }
    
    
    /**
     * Switch to the gameplay screen.
    */
    public void goToGameplayScreen()
    {
        setScreen(gameplayScreen);
    }
}