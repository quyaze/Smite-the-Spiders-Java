package quyaze.stsj.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.World;
import quyaze.stsj.screens.MainMenuScreen;

public class MainMenuWorld extends World
{
    //  Fields
    final private AtlasRegion background;
    final private AtlasRegion title;
    final private GlyphLayout subtitle;
    
    private boolean enableInput;
    final private Vector2 titlePosition, titleSize;
    final private Vector2 subtitlePosition;
    
    
    //  Constructor
    public MainMenuWorld(GameMaster gameMaster, MainMenuScreen owner)
    {
        super(gameMaster, owner);
        background = gameMaster.getAtlas().findRegion("bg");
        title = gameMaster.getAtlas().findRegion("title");
        subtitle = gameMaster.getGameText().generateRegularGlyph("Click anywhere to play.");
        
        titlePosition = Vector2.Zero.cpy();
        subtitlePosition = Vector2.Zero.cpy();
        titleSize = new Vector2(
            title.getRegionWidth(),
            title.getRegionHeight()
        ).scl(2f);
    }
    
    
    //  Render
    @Override
    public void render()
    {
        /*  Input
        */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        if (enableInput)
        {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) gameMaster.goToGameplayScreen();
        }
        
        /*  Logic
        */
        
        
        /*  Draw
        */
        final ScreenViewport viewport = gameMaster.getViewport();
        final SpriteBatch batch = gameMaster.getBatch();
        final BitmapFont regularFont = gameMaster.getGameText().getRegularFont();
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        batch.draw(
            background,
            0f,
            0f,
            width,
            height
        );
        batch.draw(
            title,
            titlePosition.x,
            titlePosition.y,
            titleSize.x,
            titleSize.y
        );
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
        gameMaster.getTimer().scheduleTask(
            new Task()
            {
                @Override public void run()
                {
                    enableInput = true;
                }
            },
            1f
        );
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
    
    
    /** On {@code MainMenuScreen.resize}. */
    public void resize(int width, int height)
    {
        titlePosition.set(
            (width - titleSize.x) * 0.5f,
            (height - titleSize.y) * 0.5f
        );
        subtitlePosition.set(
            (width - subtitle.width) * 0.5f,
            height * 0.35f - subtitle.height * 0.5f
        );
    }
}
