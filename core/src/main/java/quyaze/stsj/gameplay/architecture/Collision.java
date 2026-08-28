package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.Rectangle;

/**
 * Represents basic collision. The collision box is determined by
 * the assigned avatar's position and size.
 */
public class Collision
{
    /*  Fields  */
    public Rectangle collisionBox;
    public Avatar avatar;
    public boolean skipSolving;
    
    
    /*  Constructors  */
    /** New {@code Collision}, referencing the avatar. */
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
    
    
    /** Update the collision box to its avatar's position. */
    public void updatePosition()
    {
        collisionBox.setPosition(avatar.position);
    }
    
    
    /** Update the collision box to be its avatar's size. */
    public void updateSize()
    {
        collisionBox.setSize(avatar.getTrueWidth(), avatar.getTrueHeight());
    }
}
