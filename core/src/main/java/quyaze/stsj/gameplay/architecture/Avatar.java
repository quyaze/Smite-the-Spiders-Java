package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** Represents visible characters, beings, objects, etc. */
public class Avatar
{
    //  Fields
    public TextureRegion texture;
    public Vector2 position;
    public float opacity;
    private Vector2 trueSize;
    
    
    //  Constructors
    
    /** Set avatar texture, position, opacity, and scale. */
    public Avatar(TextureRegion texture, Vector2 position, float opacity, float scale)
    {
        this.texture = texture;
        this.position = position;
        this.opacity = opacity;
        trueSize = new Vector2(
            texture.getRegionWidth(),
            texture.getRegionHeight()
        ).scl(scale);
    }
    
    
    /** Set avatar texture. */
    public Avatar(TextureRegion texture)
    {
        this(
            texture,
            Vector2.Zero.cpy(),
            1f,
            1f
        );
    }
    
    
    /** Set avatar texture and position. */
    public Avatar(TextureRegion texture, Vector2 position)
    {
        this(
            texture,
            position,
            1f,
            1f
        );
    }
    
    
    /** Set avatar texture and scale. */
    public Avatar(TextureRegion texture, float scale)
    {
        this(
            texture,
            Vector2.Zero.cpy(),
            1f,
            scale
        );
    }
    
    
    /** Set avatar texture, position, and scale. */
    public Avatar(TextureRegion texture, Vector2 position, float scale)
    {
        this(
            texture,
            position,
            1f,
            scale
        );
    }
    
    
    /** Set avatar texture, opacity, and position. */
    public Avatar(TextureRegion texture, float opacity, Vector2 position)
    {
        this(
            texture,
            position,
            opacity,
            1f
        );
    }
    
    
    /**
     * @return A copy of the true size
     */
    public Vector2 getTrueSize()
    {
        return trueSize.cpy();
    }
    
    
    /**
     * @return Original texture width with its scale applied; the actual pixel length that the end user sees
     */
    public float getTrueWidth()
    {
        return trueSize.x;
    }
    
    
    /**
     * @return Original texture height with its scale applied; the actual pixel length that the end user sees
     */
    public float getTrueHeight()
    {
        return trueSize.y;
    }
    
    
    /**
     * Set the scale of the texture with a scalar. Uniform and
     * preserves aspect ratio.
     */
    public void setScale(float scale)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(scale);
    }
    
    
    /**
     * Set the scale of the texture with a Vector2.
     * <p></p>
     * width * scale2.x
     * <br>
     * height * scale2.y
     */
    public void setScale(Vector2 scale2)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(scale2);
    }
    
    
    /**
     * Set the scale of the texture with an x- and y-scalar.
     * <p></p>
     * width * scaleX
     * <br>
     * height * scaleY
     */
    public void setScale(float scaleX, float scaleY)
    {
        trueSize.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(scaleX, scaleY);
    }
    
    
    /**
     * Set the x-scale of the texture (width).
     */
    public void setScaleX(float scale)
    {
        trueSize.x = scale * texture.getRegionWidth();
    }
    
    
    /**
     * Set the y-scale of the texture (height).
     */
    public void setScaleY(float scale)
    {
        trueSize.y = scale * texture.getRegionHeight();
    }
}
