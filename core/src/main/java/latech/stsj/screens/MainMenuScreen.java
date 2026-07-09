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

/**
 * Screen for the main menu
 */
public class MainMenuScreen implements Screen
{
    //  Fields
    private final Main game;
    private static final int LOGO_SCALE = 3;
    
    private AtlasRegion background;
    private AtlasRegion logo;
    
    
    //  Constructor
    public MainMenuScreen(final Main game)
    {
        this.game = game;
    }
    
    
    //  Show
    @Override
    public void show()
    {
        TextureAtlas atlas = game.getAtlas();
        
        background = atlas.findRegion("bg");
        logo = atlas.findRegion("logo");
        game.getMusic().play();
    }
    
    
    //  Hide
    @Override
    public void hide()
    {}
    
    
    //  Pause
    @Override
    public void pause() {}
    
    
    //  Resume
    @Override
    public void resume() {}
    
    
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
     * Main menu input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void input(float deltaSeconds)
    {
        if (Gdx.input.isButtonJustPressed(0))
        {
            game.setScreen(game.getGameScreen());
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
    }
    
    
    /**
     * Main menu logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void logic(float deltaSeconds) {}
    
    
    /**
     * Main menu asset rendering/drawing for every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void draw(float deltaSeconds)
    {
        SpriteBatch batch = game.getBatch();
        ScreenViewport viewport = game.getViewport();
        float width = Gdx.graphics.getWidth();
        float height =  Gdx.graphics.getHeight();
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, width, height);
        batch.draw(logo, (width - logo.getRegionWidth() * LOGO_SCALE) * 0.5f, (height - logo.getRegionHeight() * LOGO_SCALE) * 0.5f, logo.getRegionWidth() * LOGO_SCALE, logo.getRegionHeight() * LOGO_SCALE);
        batch.end();
    }
}
