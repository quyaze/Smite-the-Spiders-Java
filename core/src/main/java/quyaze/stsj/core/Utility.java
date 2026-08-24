package quyaze.stsj.core;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Some utilities for the game. */
public class Utility
{
    public <T> T[] append(T[] array, T item)
    {
        final int last = array.length;
        T[] copy = Arrays.copyOf(array, last + 1);
        copy[last] = item;
        return copy;
    }
    
    
    public float getAvatarScreenScaled(TextureRegion texture)
    {
        return Math.max(
            Gdx.graphics.getWidth() / (float) texture.getRegionWidth(),
            Gdx.graphics.getHeight() / (float) texture.getRegionHeight()
        );
    }
}
