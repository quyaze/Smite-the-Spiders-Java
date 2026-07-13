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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.gameplay.Entity;
import latech.stsj.gameplay.Player;
import latech.stsj.gameplay.Projectile;
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
    private final AtlasRegion background;
    private boolean paused = false;
    private boolean spawnFireball;
    
    private Array<Projectile> fireballs;
    private Array<Projectile> webs;
    
    
    //  Constructor
    public GameScreen(final Main game)
    {
        this.game = game;
        background = game.getAtlas().findRegion("bg");
        player = new Player(game);
        spiders =  new Spider[MathUtils.random(1, 3)];
        fireballs = new Array<Projectile>();
        webs = new Array<Projectile>();
        for (int i = 0; i < spiders.length; i++) spiders[i] = new Spider(game);
    }
    
    
    //  Show
    @Override
    public void show()
    {
        paused = false;
        player.character.setCenter(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * ONE_THIRD);
        for (int i = 0; i < spiders.length; i++)
        {
            Spider spider = spiders[i];
            Entity entitySpider = spider.getEntity();
            float spawnHeight = Gdx.graphics.getHeight() * 0.8f;
            float width = Gdx.graphics.getWidth() - entitySpider.getTrueSizeX();
            if (i == 0) entitySpider.setCenter(width * 0.5f, spawnHeight);
            else if (i == 1) entitySpider.setCenter(width * 0.25f, spawnHeight);
            else if (i == 2) entitySpider.setCenter(width * 0.75f, spawnHeight);
            spider.refresh();
        }
    }
    
    
    //  Hide
    @Override
    public void hide()
    {
        fireballs.clear();
        webs.clear();
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
        spawnFireball = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
    
    
    /**
     * Gameplay logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void logic(float deltaSeconds)
    {
        //  Gameplay elements-specific logic. Skipped altogether if paused
        if (paused) return;
        
        
        //  Entities
        player.logic(deltaSeconds);
        for (Spider spider : spiders) spider.logic(deltaSeconds);
        
        
        //  Fireballs
        for (int i = 0; i < fireballs.size; i ++)
        {
            Projectile fireball = fireballs.get(i);
            if (fireball.getIsOutsideScreen())
            {
                fireballs.removeIndex(i);
            }
            else
            {
                Entity fireballEntity = fireball.getEntity();
                fireballEntity.translate(
                    fireballEntity.getVelocityX() * deltaSeconds,
                    fireballEntity.getVelocityY() * deltaSeconds
                );
            }
        }
        
        
        //  Webs
        for (int i = 0; i < webs.size; i ++)
        {
            Projectile web = webs.get(i);
            if (web.getIsOutsideScreen())
            {
                webs.removeIndex(i);
            }
            else
            {
                Entity webEntity = web.getEntity();
                webEntity.translate(
                    webEntity.getVelocityX() * deltaSeconds,
                    webEntity.getVelocityY() * deltaSeconds
                );
            }
        }
        
        
        //  Spawn fireball
        if (spawnFireball)
        {
            Projectile fireball = new Projectile(game.getAtlas().findRegion("spell"));
            Entity fireballEntity = fireball.getEntity();
            
            fireballEntity.setDirection(0f, 1f);
            fireballEntity.setScale(2f);
            fireballEntity.setPosition(
                player.character.getX() + (player.character.getTrueSizeX() - fireballEntity.getTrueSizeX()) * 0.5f,
                player.character.getY() + (player.character.getTrueSizeY() - fireballEntity.getTrueSizeY()) * 0.5f
            );
            fireballEntity.setSpeed(1200f);
            fireballs.add(fireball);
        }
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
        batch.draw(background, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Projectile fireball : fireballs) fireball.getEntity().draw(batch);
        for (Projectile web : webs) web.getEntity().draw(batch);
        player.character.draw(batch);
        for (Spider spider : spiders) spider.getEntity().draw(batch);
        batch.end();
    }
}
