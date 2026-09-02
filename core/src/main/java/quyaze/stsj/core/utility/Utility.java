package quyaze.stsj.core.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.template.World;

/** General utilities. */
final public class Utility
{
    static public float getAvatarScreenScaled(World<?> world, TextureRegion texture)
    {
        return Math.max(
            getScreenWorldWidth(world) / (float) texture.getRegionWidth(),
            getScreenWorldHeight(world) / (float) texture.getRegionHeight()
        );
    }
    
    
    static public float getScreenWorldWidth(World<?> world)
    {
        return Gdx.graphics.getWidth() * world.getUnitsPerPixel();
    }
    
    
    static public float getScreenWorldHeight(World<?> world)
    {
        return Gdx.graphics.getHeight() * world.getUnitsPerPixel();
    }
    
    
    static public Vector2 getScreenWorldSize(World<?> world)
    {
        return new Vector2(
            getScreenWorldWidth(world),
            getScreenWorldHeight(world)
        );
    }
    
    
    private Utility() {}
}
