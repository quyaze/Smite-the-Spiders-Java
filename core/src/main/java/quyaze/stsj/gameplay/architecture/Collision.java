package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.Rectangle;

/**
 * Provides basic collision capabilities to {@link Avatar}s
 * <p></p>
 * The collision box is the Avatar's texture.
 */
public class Collision
{
    //  Fields
    final public Rectangle collisionBox;
    public Avatar avatar;
    
    
    //  Constructor
    public Collision(Avatar avatar)
    {
        collisionBox = new Rectangle(
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
    }
    
    
    /** Update the collision box. */
    public void update()
    {
        collisionBox.setPosition(avatar.position);
    }
}
