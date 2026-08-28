package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** Represents visible characters, beings, objects, etc. */
public class Avatar
{
    /*  Fields  */
    public TextureRegion texture;
    public Vector2 position;
    private Vector2 trueSize;
    public float opacity = 1f;
    
    
    /*  Constructors  */
    /** Blank, unsafe avatar. */
    public Avatar()
    {
        position = Vector2.Zero.cpy();
        trueSize = Vector2.Zero.cpy();
    }
    
    /** Set the avatar's texture */
    public Avatar(TextureRegion texture)
    {
        this();
        this.texture = texture;
        setScale(1f);
    }
    
    /** Set the avatar's texture and position. */
    public Avatar(TextureRegion texture, Vector2 position)
    {
        this();
        this.texture = texture;
        position.set(position);
        setScale(1f);
    }
    
    /** Set the avatar's texture and position. */
    public Avatar(TextureRegion texture, float x, float y)
    {
        this();
        this.texture = texture;
        position.set(x, y);
        setScale(1f);
    }
    
    /** Set the avatar's texture and scale. */
    public Avatar(TextureRegion texture, float scale)
    {
        this();
        this.texture = texture;
        setScale(scale);
    }
    
    /** Set the avatar's texture, scale, and position. */
    public Avatar(TextureRegion texture, float scale, Vector2 position)
    {
        this();
        this.texture = texture;
        this.position.set(position);
        setScale(scale);
    }
    
    /** Set the avatar's texture, scale, and position. */
    public Avatar(TextureRegion texture, float scale, float x, float y)
    {
        this();
        this.texture = texture;
        position.set(x, y);
        setScale(scale);
    }
    
    
    /**
     * @return A copy of the true size
     */
    public Vector2 getTrueSize()
    {
        return trueSize.cpy();
    }
    
    
    /**
     * @return Original texture width with its scale applied; the actual world length that the end user sees
     */
    public float getTrueWidth()
    {
        return trueSize.x;
    }
    
    
    /**
     * @return Original texture height with its scale applied; the actual world length that the end user sees
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
    
    
    /** Set the x-scale of the texture (width). */
    public void setScaleX(float scale)
    {
        trueSize.x = scale * texture.getRegionWidth();
    }
    
    
    /** Set the y-scale of the texture (height). */
    public void setScaleY(float scale)
    {
        trueSize.y = scale * texture.getRegionHeight();
    }
    
    
    /** Center coordinate of the avatar. */
    public Vector2 getCenter()
    {
        return position.cpy().add(trueSize.cpy().scl(0.5f));
    }
    
    
    /** Get the top right coordinate; opposite corner from the position. */
    public Vector2 getTopRight()
    {
        return position.cpy().add(trueSize);
    }
}
