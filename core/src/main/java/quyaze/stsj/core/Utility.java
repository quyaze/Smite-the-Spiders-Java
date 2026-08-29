package quyaze.stsj.core;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** General utilities. */
final public class Utility
{
    static public <T> T[] append(T[] array, T item)
    {
        final int last = array.length;
        T[] copy = Arrays.copyOf(array, last + 1);
        copy[last] = item;
        return copy;
    }
    
    
    static public float getAvatarScreenScaled(TextureRegion texture, float unitsPerPixels)
    {
        return Math.max(
            getScreenWorldWidth(unitsPerPixels) / (float) texture.getRegionWidth(),
            getScreenWorldHeight(unitsPerPixels) / (float) texture.getRegionHeight()
        );
    }
    
    
    static public float getScreenWorldWidth(float unitsPerPixel)
    {
        return Gdx.graphics.getWidth() * unitsPerPixel;
    }
    
    
    static public float getScreenWorldHeight(float unitsPerPixel)
    {
        return Gdx.graphics.getHeight() * unitsPerPixel;
    }
    
    
    static public Vector2 getScreenWorldSize(float unitsPerPixel)
    {
        return new Vector2(
            getScreenWorldWidth(unitsPerPixel),
            getScreenWorldHeight(unitsPerPixel)
        );
    }
    
    
    private Utility() {}
}
