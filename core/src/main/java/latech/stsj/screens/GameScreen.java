package latech.stsj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.Player;

/**
 * Screen for the gameplay
 */
public class GameScreen implements Screen
{
    //  Fields
    private final Main game;
    private final Player player;
    
    private AtlasRegion background;
    
    
    //  Constructor
    public GameScreen(final Main game)
    {
        this.game = game;
        player = new Player(game);
    }
    
    
    //  Show
    @Override
    public void show()
    {
        TextureAtlas atlas = game.getAtlas();
        
        background = atlas.findRegion("bg");
        player.sprite.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() / 3f);
    }
    
    
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
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        game.getViewport().update(width, height, true);
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds)
    {
        input(deltaSeconds);
        logic(deltaSeconds);
        draw(deltaSeconds);
    }
    
    
    //  Dispose
    @Override
    public void dispose()
    {}
    
    
    /**
     * Gameplay input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void input(float deltaSeconds)
    {
        //  TODO: Pause menu panel.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            game.setScreen(game.getMainMenuScreen());
            return;
        }
        player.input(deltaSeconds);
    }
    
    
    /**
     * Gameplay logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void logic(float deltaSeconds)
    {
        player.logic(deltaSeconds);
    }
    
    
    /**
     * Gameplay asset rendering/drawing for every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void draw(float deltaSeconds)
    {
        ScreenViewport viewport = game.getViewport();
        SpriteBatch batch = game.getBatch();
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(player.sprite, player.sprite.getX(), player.sprite.getY(), player.getScaledX(), player.getScaledY());
        batch.end();
    }
}
