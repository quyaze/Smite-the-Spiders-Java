package quyaze.stsj.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

/** Utility class for game text. */
final public class GameText implements Disposable
{
    /*  Fields  */
    private FreeTypeFontGenerator regularGenerator;
    private FreeTypeFontParameter regularParams;
    public BitmapFont regular;
    
    
    /*  Constructor  */
    /** Reference the viewport for font scale initialization. */
    public GameText()
    {
        regularGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PirataOne-Regular.ttf"));
        regularParams = new FreeTypeFontParameter();
        
        regularParams.size = 36;
        regularParams.spaceX = 4;
        regular = regularGenerator.generateFont(regularParams);
        regular.setUseIntegerPositions(false);
    }
    
    
    /*  Dispose  */
    @Override
    public void dispose()
    {
        regularGenerator.dispose();
        regular.dispose();
    }
    
    
    /**
     * @return Generated glyph layout with the {@code textContent}
     */
    public GlyphLayout generateGlyphRegular(String textContent)
    {
        return new GlyphLayout(regular, textContent);
    }
}
