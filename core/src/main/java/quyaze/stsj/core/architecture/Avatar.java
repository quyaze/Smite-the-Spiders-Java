package quyaze.stsj.core.architecture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * <hr>
 * Represents visible characters, beings, objects, etc.
*/
public class Avatar
{
    /*  Fields  */
    private TextureRegion texture;
    private Vector2 position, trueSize;
    private float opacity = 1f;
    
    
    /*  Constructors  */
    /** <hr> Blank avatar. */
    public Avatar()
    {
        position = Vector2.Zero.cpy();
        trueSize = Vector2.Zero.cpy();
    }
    
    /** <hr> Set the avatar's texture. */
    public Avatar(TextureRegion texture)
    {
        this();
        this.texture = texture;
        setScale(1f);
    }
    
    /** <hr> Set the avatar's texture and position. */
    public Avatar(TextureRegion texture, Vector2 position)
    {
        this();
        this.texture = texture;
        position.set(position);
        setScale(1f);
    }
    
    /** <hr> Set the avatar's texture and position. */
    public Avatar(TextureRegion texture, float x, float y)
    {
        this();
        this.texture = texture;
        position.set(x, y);
        setScale(1f);
    }
    
    /** <hr> Set the avatar's texture and scale. */
    public Avatar(TextureRegion texture, float scale)
    {
        this();
        this.texture = texture;
        setScale(scale);
    }
    
    /** <hr> Set the avatar's texture, scale, and position. */
    public Avatar(TextureRegion texture, float scale, Vector2 position)
    {
        this();
        this.texture = texture;
        this.position.set(position);
        setScale(scale);
    }
    
    /** <hr> Set the avatar's texture, scale, and position. */
    public Avatar(TextureRegion texture, float scale, float x, float y)
    {
        this();
        this.texture = texture;
        position.set(x, y);
        setScale(scale);
    }
    
    
    /**
     * <hr>
     * Returns the position by reference, allowing it to be manipulated and
     * passed by reference.
     * <p></p>
     * Use {@link #readPosition()} for a copy of the position that is
     * "read-only."
     * @return Direct reference to the avatar's position
    */
    public Vector2 getPosition()
    {
        return position;
    }
    
    
    /**
     * <hr>
     * @return A copy of the avatar's position
    */
    public Vector2 readPosition()
    {
        return position;
    }
    
    
    /**
     * <hr>
     * @return The avatar's x-position
    */
    public float getX()
    {
        return position.x;
    }
    
    
    /**
     * <hr>
     * @return The avatar's y-position
    */
    public float getY()
    {
        return position.y;
    }
    
    
    /**
     * <hr>
     * @return A copy of the true size
    */
    public Vector2 getTrueSize()
    {
        return trueSize.cpy();
    }
    
    
    /**
     * <hr>
     * @return Original texture width with its scale applied; the actual world length that the end user sees
    */
    public float getTrueWidth()
    {
        return trueSize.x;
    }
    
    
    /**
     * <hr>
     * @return Original texture height with its scale applied; the actual world length that the end user sees
    */
    public float getTrueHeight()
    {
        return trueSize.y;
    }
    
    
    /**
     * <hr>
     * @return The avatar's render opacity
    */
    public float getOpacity()
    {
        return opacity;
    }
    
    
    /**
     * <hr>
     * Set the avatar's render opacity.
     * @param opacity
    */
    public void setOpacity(float opacity)
    {
        this.opacity = opacity;
    }
    
    
    /**
     * <hr>
     * Set the scale of the texture with a {@code scalar}. Uniform and
     * preserves aspect ratio.
     * @param scale - scalar
    */
    public void setScale(float scale)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(scale);
    }
    
    
    /**
     * <hr>
     * Set the scale of the texture with a Vector2.
     * <br>
     * {@code width * vecScl.x}
     * <br>
     * {@code height * vecScl.y}
     * @param vecScl - scalar by components
    */
    public void setScale(Vector2 vecScl)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(vecScl);
    }
    
    
    /**
     * <hr>
     * Set the scale of the texture with an x- and y-scalar.
     * <br>
     * {@code sX * width}
     * <br>
     * {@code sY * height}
     * @param sX - scalar for width
     * @param sY - scalar for height
    */
    public void setScale(float sclX, float sclY)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(sclX, sclY);
    }
    
    
    /**
     * <hr>
     * Set the texture's horizontal scale.
     * <br>
     * {@code scale * width}
     * @param scale - scalar
    */
    public void setScaleX(float scale)
    {
        trueSize.x = scale * texture.getRegionWidth();
    }
    
    
    /**
     * <hr>
     * Set the texture's vertical scale.
     * <br>
     * {@code scale * height}
     * @param scale - scalar
    */
    public void setScaleY(float scale)
    {
        trueSize.y = scale * texture.getRegionHeight();
    }
    
    
    /**
     * <hr>
     * @return The avatar's center coordinate
    */
    public Vector2 getCenter()
    {
        return position.cpy().add(trueSize.cpy().scl(0.5f));
    }
    
    
    /**
     * <hr>
     * @return The avatar's top right coordinate; opposite corner from its position
    */
    public Vector2 getTopRight()
    {
        return position.cpy().add(trueSize);
    }
}
