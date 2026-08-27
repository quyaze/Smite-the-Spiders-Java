package quyaze.stsj.core;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** Some utilities for the game. */
final public class Utility
{
    static public <T> T[] append(T[] array, T item)
    {
        final int last = array.length;
        T[] copy = Arrays.copyOf(array, last + 1);
        copy[last] = item;
        return copy;
    }
    
    
    static public float getAvatarScreenScaled(ScreenViewport viewport, TextureRegion texture)
    {
        return Math.max(
            viewport.getWorldWidth() / (float) texture.getRegionWidth(),
            viewport.getWorldHeight() / (float) texture.getRegionHeight()
        );
    }
    
    
    private Utility() {}
}
