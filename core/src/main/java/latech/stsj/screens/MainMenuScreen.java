/*
//      MainMenuScreen.java
*/


package latech.stsj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.templates.StarterScreen;


/**
 * Screen for the main menu.
 */
public class MainMenuScreen extends StarterScreen
{
    //  Fields
    private static final int LOGO_SCALE = 3;
    
    private AtlasRegion background;
    private AtlasRegion logo;
    
    private float logoPhase = 1f;
    private boolean logoPhaseInverse = false;
    private boolean enableInput = false;
    
    private BitmapFont subtitleText;
    private GlyphLayout subtitleGlyph;
    
    
    //  Constructor
    public MainMenuScreen(final Main main)
    {
        this.main = main;
        
        FreeTypeFontParameter params = new FreeTypeFontParameter();
        params.size = 36;
        
        subtitleText = main.getFontGeneric().generateFont(params);
        subtitleGlyph = new GlyphLayout(subtitleText, "Click anywhere to play");
    }
    
    
    //  Show
    @Override
    public void show()
    {
        TextureAtlas atlas = main.getAtlas();
        
        main.getTimer().scheduleTask(
            new Task()
            {
                public void run()
                {
                    enableInput = true;
                }
            },
            1f
        );
        background = atlas.findRegion("bg");
        logo = atlas.findRegion("title");
        logoPhase = 0f;
        main.getMusic().play();
    }
    
    
    //  Hide
    @Override
    public void hide()
    {
        enableInput = false;
    }
    
    
    //  Pause
    @Override
    public void pause()
    {}
    
    
    //  Resume
    @Override
    public void resume() {}
    
    
    //  Resize
    @Override
    public void resize(int width, int height)
    {
        main.getViewport().update(width, height, true);
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
    {
        subtitleText.dispose();
    }
    
    
    /**
     * Main menu input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void input(float deltaSeconds)
    {
        //  1.  Is player exiting
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
        
        //  2.  Is input enabled
        if (!enableInput) return;
        
        //  3.  Proceed to main menu input processing
        if (Gdx.input.isButtonJustPressed(0))
        {
            main.goToGameplayScreen();
        }
    }
    
    
    /**
     * Main menu logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void logic(float deltaSeconds) {}
    
    
    /**
     * Main menu rendering/drawing for every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    private void draw(float deltaSeconds)
    {
        SpriteBatch batch = main.getBatch();
        ScreenViewport viewport = main.getViewport();
        float width = Gdx.graphics.getWidth();
        float height =  Gdx.graphics.getHeight();
        
        if (logoPhase < 1)
        {
            logoPhase = MathUtils.clamp(logoPhase + (logoPhaseInverse ? -deltaSeconds : deltaSeconds) * 0.8f, 0f, 1f);
        }
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, width, width * 0.75f);
        batch.setColor(1f, 1f, 1f, MathUtils.lerp(0f, 1f, logoPhase));
        batch.draw(logo, (width - logo.getRegionWidth() * LOGO_SCALE) * 0.5f, (height - logo.getRegionHeight() * LOGO_SCALE) * 0.5f, logo.getRegionWidth() * LOGO_SCALE, logo.getRegionHeight() * LOGO_SCALE);
        batch.setColor(1f, 1f, 1f, 1f);
        if (enableInput) subtitleText.draw(batch, subtitleGlyph, (Gdx.graphics.getWidth() - subtitleGlyph.width) * 0.5f, (Gdx.graphics.getHeight() - subtitleGlyph.height) * 0.25f);
        batch.end();
    }
}
