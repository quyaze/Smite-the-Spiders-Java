package quyaze.stsj;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
    private Music music;
    
    private MainMenuScreen mainMenuScreen;
    private GameplayScreen gameplayScreen;
    
    
    //  Create
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        viewport = new ScreenViewport();
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/Lord of the Land.mp3"));
        
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
        music.dispose();
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