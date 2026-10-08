package quyaze.stsj.core.architecture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <hr>
 * Represents basic collision.
*/
public class Collision
{
    /*  Fields  */
    private Rectangle collisionBox;
    private Vector2 position, size;
    
    /** <hr>  */
    public boolean skipSolving;
    
    /** <hr> The collision can change in position. */
    public boolean dynamic;
    
    /** <hr> Describes if the collision can change in size. */
    public boolean resizable;
    
    
    /*  Constructors  */
    /** <hr> Blank collision. */
    public Collision()
    {
        position = Vector2.Zero.cpy();
        size = Vector2.Zero.cpy();
        collisionBox = new Rectangle();
    }
    
    /** <hr> Reference an {@link Avatar} for the collision. */
    public Collision(Avatar avatar)
    {
        this();
        updateCollision(avatar.getPosition(), avatar.getTrueSize());
    }
    
    
    public Collision(float x, float y, float width, float height)
    {
        this();
        updateCollision(x, y, width, height);
    }
    
    
    /** Update the collision box to its avatar's position. */
    public void updatePosition(float x, float y)
    {
        collisionBox.setPosition(position.set(x, y));
    }
    
    
    public void updatePosition(Vector2 vec2)
    {
        collisionBox.setPosition(position.set(vec2));
    }
    
    
    /** Update the collision box to be its avatar's size. */
    public void updateSize(float width, float height)
    {
        size.set(width, height);
        collisionBox.setSize(width, height);
    }
    
    
    public void updateSize(Vector2 vec2)
    {
        size.set(vec2);
        collisionBox.setSize(vec2.x, vec2.y);
    }
    
    
    public void updateCollision(float x, float y, float width, float height)
    {
        updatePosition(x, y);
        updateSize(width, height);
    }
    
    
    public void updateCollision(Vector2 vecPos, float width, float height)
    {
        updatePosition(vecPos);
        updateSize(width, height);
    }
    
    
    public void updateCollision(float x, float y, Vector2 vecSize)
    {
        updatePosition(x, y);
        updateSize(vecSize);
    }
    
    
    public void updateCollision(Vector2 vecPos, Vector2 vecSize)
    {
        updatePosition(vecPos);
        updateSize(vecSize);
    }
    
    
    public void setCollision(Collision collision)
    {
        collisionBox.set(collision.collisionBox);
        position.set(collision.position);
        size.set(collision.size);
        skipSolving = collision.skipSolving;
        dynamic = collision.dynamic;
    }
}
