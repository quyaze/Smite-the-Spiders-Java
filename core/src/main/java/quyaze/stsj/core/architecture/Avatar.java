package quyaze.stsj.core.architecture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * <hr>
 * Represents visible characters, beings, objects, etc.
*/
public class Avatar
{
    /*  Fields  */
    private Vector2 position, size;
    private float opacity = 1f;
    
    /** <hr> Avatar's {@link TextureRegion texture} */
    public TextureRegion texture;
    
    
    /*  Constructors  */
    
    /** <hr> Blank avatar. */
    public Avatar()
    {
        position = Vector2.Zero.cpy();
        size = Vector2.Zero.cpy();
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
        return position.cpy();
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
     * @return A copy of avatar's world size
    */
    public Vector2 getSize()
    {
        return size.cpy();
    }
    
    
    /**
     * <hr>
     * @return World length of the avatar: <br> {@code texture width * x-scale}
    */
    public float getWidth()
    {
        return size.x;
    }
    
    
    /**
     * <hr>
     * @return World height of the avatar: <br> {@code texture height * y-scale}
    */
    public float getHeight()
    {
        return size.y;
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
        size.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(scale);
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
        size.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(vecScl);
    }
    
    
    /**
     * <hr>
     * Set the scale of the texture with an x- and y-scalar.
     * <br>
     * {@code sclX * width}
     * <br>
     * {@code sclY * height}
     * @param sclX - scalar for width
     * @param sclY - scalar for height
    */
    public void setScale(float sclX, float sclY)
    {
        size.set(texture.getRegionWidth(), texture.getRegionHeight()).scl(sclX, sclY);
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
        size.x = scale * texture.getRegionWidth();
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
        size.y = scale * texture.getRegionHeight();
    }
    
    
    /**
     * <hr>
     * @return The avatar's center world-coordinate
    */
    public Vector2 getCenter()
    {
        return readPosition().add(getSize().scl(0.5f));
    }
    
    
    /**
     * <hr>
     * @return The avatar's top right world-coordinate; opposite corner from its position
    */
    public Vector2 getTopRight()
    {
        return position.cpy().add(size);
    }
    
    
    /**
     * <hr>
     * Draw the avatar.
     * @param batch - use {@link quyaze.stsj.SmiteTheSpiders#getBatch() game's batch}
     */
    public void draw(SpriteBatch batch)
    {
        batch.setColor(1f, 1f, 1f, opacity);
        batch.draw(texture, getX(), getY(), size.x, size.y);
        batch.setColor(Color.WHITE);
    }
    
    
    /**
     * <hr>
     * Set the avatar to be horizontally flipped.
     * @param flipped
    */
    public void setFlippedX(boolean flipped)
    {
        texture.flip(flipped != texture.isFlipX(), false);
    }
    
    
    /**
     * <hr>
     * Set the avatar to be horizontally flipped.
     * @param flipped
    */
    public void setFlippedY(boolean flipped)
    {
        texture.flip(false, flipped != texture.isFlipY());
    }
}
