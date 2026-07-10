//  TODO: possible state system. Gameplay loop for round based gameplay to generate new spiders

package latech.stsj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.gameplay.Player;
import latech.stsj.gameplay.Spider;

/**
 * Screen for the gameplay
 */
public class GameScreen implements Screen
{
    //  Fields
    private final float ONE_THIRD = 1/3f;
    
    private final Main game;
    private final Player player;
    private final Array<Spider> spiders;
    
    private AtlasRegion background;
    
    
    //  Constructor
    public GameScreen(final Main game)
    {
        this.game = game;
        player = new Player(game);
        spiders = new Array<Spider>();
        
        for (int i = 0; i < MathUtils.random(1, 3); i++) spiders.add(new Spider(game));
    }
    
    
    //  Show
    @Override
    public void show()
    {
        TextureAtlas atlas = game.getAtlas();
        
        game.getTimer().scheduleTask(
            new Task()
            {
                public void run()
                {
                    player.enableInput = true;
                    player.frozen = false;
                    for (Spider spider : spiders) spider.frozen = false;
                }
            },
            1f
        );
        background = atlas.findRegion("bg");
        player.getSprite().setPosition((Gdx.graphics.getWidth() - player.getTrueScaleX()) * 0.5f, Gdx.graphics.getHeight() * ONE_THIRD);
        
        for (int i = 0; i < spiders.size; i++)
        {
            Spider spider = spiders.get(i);
            float spawnHeight = (Gdx.graphics.getHeight() - spider.getTrueScaleY()) * 0.8f;
            float width = Gdx.graphics.getWidth() - spider.getTrueScaleX();
            float spiderCenter = spider.getTrueScaleX() * 0.5f;
            if (i == 0) spider.getSprite().setCenter(width * 0.5f - spiderCenter, spawnHeight);
            else if (i == 1) spider.getSprite().setCenter(width * 0.25f - spiderCenter, spawnHeight);
            else if (i == 2) spider.getSprite().setCenter(width * 0.75f - spiderCenter, spawnHeight);
            spider.refresh();
        }
    }
    
    
    //  Hide
    @Override
    public void hide()
    {
        player.enableInput = false;
        player.frozen = true;
        for (Spider spider : spiders) spider.frozen = true;
    }
    
    
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
