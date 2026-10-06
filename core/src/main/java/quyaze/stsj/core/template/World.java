package quyaze.stsj.core.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

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
    private WorldTimer wt;
    
    protected float unitsPerPixel = 1f;
    
    
    /* Constructor */
    /** Create a new {@code World}. */
    public World()
    {
        wt = new WorldTimer();
    }
    
    
    /** The {@link WorldTimer} of the world. */
    public WorldTimer getTimer()
    {
        return wt;
    }
    
    
    /** @return World units per pixel */
    public float getUnitsPerPixel()
    {
        return unitsPerPixel;
    }
    
    
    /** Scale an avatar to stretch to the screen. */
    public float getAvatarScaleToView(TextureRegion texture)
    {
        return Math.max(
            getWorldViewWidth() / (float) texture.getRegionWidth(),
            getWorldViewHeight() / (float) texture.getRegionHeight()
        );
    }
    
    
    /** Worldview horizontal distance. */
    public float getWorldViewWidth()
    {
        return Gdx.graphics.getWidth() * unitsPerPixel;
    }
    
    
    /** Worldview vertical distance. */
    public float getWorldViewHeight()
    {
        return Gdx.graphics.getHeight() * unitsPerPixel;
    }
    
    
    /** Worldview screen distance. */
    public Vector2 getWorldViewSize()
    {
        return new Vector2(
            getWorldViewWidth(),
            getWorldViewHeight()
        );
    }
    
    
    /** World's render pass. */
    public abstract void render(float delta);
}
