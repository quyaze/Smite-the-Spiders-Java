package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.Rectangle;

/**
 * Provides basic collision capabilities to {@link Avatar}s
 * <p></p>
 * The collision box is the Avatar's texture.
 */
public abstract class Collision
{
    //  Fields
    final public Rectangle collisionBox;
    public Avatar avatar;
    public boolean skipSolving;
    
    
    //  Constructor
    
    /** Set the Collision's reference avatar. */
    public Collision(Avatar avatar)
    {
        collisionBox = new Rectangle(
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
        this.avatar = avatar;
    }
    
    
    /** Update the collision box. */
    public void update()
    {
        collisionBox.setPosition(avatar.position);
    }
    
    
    /** Collision with another Collision object is detected. */
    public abstract void onCollided(int entity, Collision collider);
}
