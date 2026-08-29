package quyaze.stsj.mainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.GameText;
import quyaze.stsj.core.Utility;
import quyaze.stsj.core.World;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.screens.MainMenuScreen;

/** World for the main menu. */
public class MainMenuWorld extends World<MainMenuScreen>
{
    /*  Fields  */
    private SmiteTheSpiders game;
    
    private TextureRegion background;
    private TextureRegion title;
    private GlyphLayout subtitle;
    
    private boolean enableInput;
    private Avatar backgroundAvatar;
    private Avatar titleAvatar;
    private Vector2 subtitlePosition;
    
    final static public float UNITS_PER_PIXEL = 1f;
    
    
    /*  Constructor  */
    public MainMenuWorld()
    {
        subtitlePosition = Vector2.Zero.cpy();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        game = getGameInstance();
        TextureAtlas atlas = game.getAtlas();
        GameText gameText = game.getGameText();
        
        background = atlas.findRegion("bg");
        title = atlas.findRegion("title");
        subtitle = gameText.generateGlyphRegular("Click anywhere to play.");
        
        backgroundAvatar = new Avatar(background);
        titleAvatar = new Avatar(title, 2f);
    }
    
    
    /*  Render  */
    @Override
    public void render(final float dS)
    {
        if (!input()) return;
        logic(dS);
        draw();
    }
    
    
    /**
     * On {@code MainMenuScreen.show()}.
     * <p></p>
     * Starts the main menu.
    */
    public void show()
    {
        Timer.schedule(
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
        width *= UNITS_PER_PIXEL;
        height *= UNITS_PER_PIXEL;
        
        backgroundAvatar.setScale(
            Utility.getAvatarScreenScaled(background, UNITS_PER_PIXEL)
        );
        titleAvatar.position.set(
            (width - titleAvatar.getTrueWidth()) * 0.5f,
            (height - titleAvatar.getTrueHeight()) * 0.5f
        );
        titleAvatar.setScale(2f);
        subtitlePosition.set(
            (width - subtitle.width) * 0.5f,
            height * 0.35f - subtitle.height * 0.5f
        );
    }
    
    
    /*  Input  */
    private boolean input()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
            return false;
        }
        if (enableInput && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            game.toGameplayScreen();
            return false;
        }
        return true;
    }
    
    
    /*  Logic  */
    private void logic(final float dS)
    {
        /*  "Smite the Spiders" fade-in
        */
        if (titleAvatar.opacity < 1f)
        {
            titleAvatar.opacity = Math.min(
                titleAvatar.opacity + dS * 1.25f, 1f
            );
        }   
    }
    
    
    /*  Draw  */
    private void draw()
    {
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
}
