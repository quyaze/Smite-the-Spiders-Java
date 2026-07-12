//  TODO: gameplay loop (round based) to generate new spiders
//  TODO: pause menu

package latech.stsj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.gameplay.Player;
import latech.stsj.gameplay.Spider;

/**
 * Screen for the gameplay.
 */
public class GameScreen implements Screen
{
    //  Fields
    private final float ONE_THIRD = 1/3f;
    
    private final Main game;
    private final Player player;
    private final Spider[] spiders;
    
    private AtlasRegion background;
    private boolean paused = false;
    
    
    //  Constructor
    public GameScreen(final Main game)
    {
        this.game = game;
        background = game.getAtlas().findRegion("bg");
        player = new Player(game);
        spiders =  new Spider[MathUtils.random(1, 3)];
        for (int i = 0; i < spiders.length; i++) spiders[i] = new Spider(game);
    }
    
    
    //  Show
    @Override
    public void show()
    {
        paused = false;
        player.sprite.setCenter(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * ONE_THIRD);
        for (int i = 0; i < spiders.length; i++)
        {
            Spider spider = spiders[i];
            float spawnHeight = Gdx.graphics.getHeight() * 0.8f - spider.getTrueScaleY() * 0.5f;
            float width = Gdx.graphics.getWidth() - spider.getTrueScaleX();
            float spiderCenter = spider.getTrueScaleX() * 0.5f;
            if (i == 0) spider.sprite.setCenter(width * 0.5f - spiderCenter, spawnHeight);
            else if (i == 1) spider.sprite.setCenter(width * 0.25f - spiderCenter, spawnHeight);
            else if (i == 2) spider.sprite.setCenter(width * 0.75f - spiderCenter, spawnHeight);
            spider.refresh();
        }
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
     * Gameplay input to process every frame.
     * @param deltaSeconds Time (sec) since last frame
     */
    private void input(float deltaSeconds)
    {
        //  GameScreen-specific input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) paused = !paused;
        if (paused && Gdx.input.isKeyJustPressed(Input.Keys.Q)) game.setScreen(game.getMainMenuScreen());
        
        //  Gameplay elements-specific input. Skipped altogether if paused
        if (paused) return;
        player.input(deltaSeconds);
    }
    
    
    /**
     * Gameplay logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void logic(float deltaSeconds)
    {
        //  Gameplay elements-specific logic. Skipped altogether if paused
        if (paused) return;
        player.logic(deltaSeconds);
        for (Spider spider : spiders) spider.logic(deltaSeconds);
    }
    
    
    /**
     * Gameplay rendering/drawing for every frame
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
        player.draw(batch);
        for (Spider spider : spiders) spider.draw(batch);
        batch.end();
    }
}
