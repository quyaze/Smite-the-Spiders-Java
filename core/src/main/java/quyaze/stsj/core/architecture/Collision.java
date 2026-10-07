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
    private boolean skipSolving;
    
    
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
        size.set(avatar.getTrueSize());
        collisionBox.setPosition(position.set(avatar.getPosition()));
        collisionBox.setSize(size.x, size.y);
    }
    
    
    public Collision(float x, float y, float width, float height)
    {
        this();
        size.set(width, height);
        collisionBox.setPosition(position.set(x, y));
        collisionBox.setSize(size.x, size.y);
    }
    
    
    /** Update the collision box to its avatar's position. */
    public void updatePosition()
    {
        collisionBox.setPosition(avatar.getPosition());
    }
    
    
    /** Update the collision box to be its avatar's size. */
    public void updateSize()
    {
        collisionBox.setSize(avatar.getTrueWidth(), avatar.getTrueHeight());
    }
    
    
    public boolean getSkipSolving()
    {
        return skipSolving;
    }
    
    
    public void setSkipSolving(boolean skipSolving)
    {
        this.skipSolving = skipSolving;
    }
}
