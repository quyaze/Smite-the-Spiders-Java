package quyaze.stsj.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

/** Utility class for game text. */
public class GameText implements Disposable
{
    //  Fields
    private FreeTypeFontGenerator regularGenerator;
    private FreeTypeFontParameter regularParams;
    private BitmapFont regular;
    
    
    //  Constructor
    public GameText()
    {
        regularGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/regular.otf"));
        regularParams = new FreeTypeFontParameter();
        
        regularParams.size = 36;
        
        regular = regularGenerator.generateFont(regularParams);
    }
    
    
    //  Dispose
    @Override
    public void dispose()
    {
        regularGenerator.dispose();
        regular.dispose();
    }
    
    
    /**
     * @return Regular (Cinzel) font
     */
    public BitmapFont getRegularFont()
    {
        return regular;
    }
    
    
    /**
     * @param textContent
     * @return Generated glyph layout with the {@code textContent}
     */
    public GlyphLayout generateRegularGlyph(String textContent)
    {
        return new GlyphLayout(regular, textContent);
    }
}
