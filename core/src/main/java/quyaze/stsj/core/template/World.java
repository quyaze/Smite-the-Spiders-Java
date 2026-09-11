package quyaze.stsj.core.template;

import com.badlogic.gdx.Screen;

/**
 * A kind of base plug-in for {@link Screen}s, which can be
 * considered the {@code owner} of the {@code World}. Should
 * separate andhold all primary source from the {@code owner}.
 * <p></p>
 * The {@code owner} should call {@link #render(float)}.
*/
public abstract class World<T extends GameContext> extends ScreenContext<T>
{
    /*  Fields  */
    protected float unitsPerPixel = 1f;
    
    
    /**
     * @return World units per pixel
    */
    public float getUnitsPerPixel()
    {
        return unitsPerPixel;
    }
    
    
    /** World's render pass. */
    public abstract void render(float delta);
}
