package quyaze.stsj.core.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.template.World;

/** General utilities. */
final public class Utility
{
    private Utility()
    {}
    
    
    /** Scale an avatar to stretch to the screen. */
    static public float getAvatarScaleToView(World<?> world, TextureRegion texture)
    {
        return Math.max(
            getWorldViewWidth(world) / (float) texture.getRegionWidth(),
            getWorldViewHeight(world) / (float) texture.getRegionHeight()
        );
    }
    
    
    /** Worldview horizontal distance. */
    static public float getWorldViewWidth(World<?> world)
    {
        return Gdx.graphics.getWidth() * world.getUnitsPerPixel();
    }
    
    
    /** Worldview vertical distance. */
    static public float getWorldViewHeight(World<?> world)
    {
        return Gdx.graphics.getHeight() * world.getUnitsPerPixel();
    }
    
    
    /** Worldview screen distance. */
    static public Vector2 getWorldViewSize(World<?> world)
    {
        return new Vector2(
            getWorldViewWidth(world),
            getWorldViewHeight(world)
        );
    }
}
