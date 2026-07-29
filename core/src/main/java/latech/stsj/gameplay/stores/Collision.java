/*
//      Collision.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Rectangle;

import latech.stsj.enums.CollisionType;


/**
 * Class providing collision for gameplay objects and characters.
 */
public class Collision
{
    //  Fields
    public Rectangle collision;
    public boolean isDynamicSize;
    public CollisionType type;
    
    
    /*  Collision box is based on an associated drawable.
    */
   
    
    //  Constructor
    public Collision(TextureDrawable drawable, boolean dynamicSize, CollisionType type)
    {
        collision = new Rectangle(
            drawable.position.x,
            drawable.position.y,
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
        isDynamicSize = dynamicSize;
        this.type = type;
    }
    
    
    /**
     * Refresh collision parameters from associated drawable.
     * @param drawable
     */
    public void update(TextureDrawable drawable)
    {
        collision.setPosition(drawable.position);
        if (isDynamicSize)
        {
            collision.setSize(
                drawable.getTrueWidth(),
                drawable.getTrueHeight()
            );
        }
    }
}
