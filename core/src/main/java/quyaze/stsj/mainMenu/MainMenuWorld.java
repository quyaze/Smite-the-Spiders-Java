package quyaze.stsj.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.World;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.screens.MainMenuScreen;

/** World for the main menu. */
public class MainMenuWorld extends World
{
    //  Fields
    final private AtlasRegion background;
    final private AtlasRegion title;
    final private GlyphLayout subtitle;
    
    private boolean enableInput;
    final private Avatar backgroundAvatar;
    final private Avatar titleAvatar;
    final private Vector2 subtitlePosition;
    
    
    //  Constructor
    public MainMenuWorld(SmiteTheSpiders game, MainMenuScreen owner)
    {
        super(game, owner);
        background = game.getAtlas().findRegion("bg");
        title = game.getAtlas().findRegion("title");
        subtitle = game.getGameText().generateGlyphRegular("Click anywhere to play.");
        
        backgroundAvatar = new Avatar(background);
        titleAvatar = new Avatar(title, 2f);
        subtitlePosition = Vector2.Zero.cpy();
    }
    
    
    //  Render
    @Override
    public void render(float delta)
    {
        /*  Input
        */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
            return;
        }
        if (enableInput && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            game.goToGameplayScreen();
            return;
        }
        
        /*  Logic
        */
        if (titleAvatar.opacity < 1f)
        {
            titleAvatar.opacity = MathUtils.clamp(
                titleAvatar.opacity + delta * 1.25f, 0f, 1f
            );
        }
        
        /*  Draw
        */
        ScreenViewport viewport = game.getViewport();
        SpriteBatch batch = game.getBatch();
        BitmapFont regularFont = game.getGameText().regular;
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        batch.draw(
            background,
            0f,
            0f,
            backgroundAvatar.getTrueWidth(),
            backgroundAvatar.getTrueHeight()
        );
        batch.setColor(1f, 1f, 1f, titleAvatar.opacity);
        batch.draw(
            title,
            titleAvatar.position.x,
            titleAvatar.position.y,
            titleAvatar.getTrueWidth(),
            titleAvatar.getTrueHeight()
        );
        batch.setColor(Color.WHITE);
        if (enableInput)
        {
            regularFont.draw(
                batch,
                subtitle,
                subtitlePosition.x,
                subtitlePosition.y
            );
        }
        
        batch.end();
    }
    
    
    /**
     * On {@code MainMenuScreen.show()}.
     * <p></p>
     * Starts the main menu.
    */
    public void show()
    {
        game.getTimer().scheduleTask(
            new Task()
            {
                @Override public void run()
                {
                    enableInput = true;
                }
            },
            1f
        );
        titleAvatar.opacity = 0f;
    }
    
    
    /**
     * On {@code MainMenuScreen.hide()}.
     * <p></p>
     * Exits the main menu.
    */
    public void hide()
    {
        enableInput = false;
    }
    
    
    /** On {@code MainMenuScreen.resize()}. */
    public void resize(int width, int height)
    {
        backgroundAvatar.setScale(Math.max(
            width / (float) backgroundAvatar.texture.getRegionWidth(),
            height / (float) backgroundAvatar.texture.getRegionHeight()
        ));
        titleAvatar.position.set(
            (width - titleAvatar.getTrueWidth()) * 0.5f,
            (height - titleAvatar.getTrueHeight()) * 0.5f
        );
        subtitlePosition.set(
            (width - subtitle.width) * 0.5f,
            height * 0.35f - subtitle.height * 0.5f
        );
    }
}
