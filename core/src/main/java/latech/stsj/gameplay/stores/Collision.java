/*
//      Collision.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import latech.stsj.templates.Entity;


/**
 * Class providing collision for gameplay objects and characters.
 */
public class Collision
{
    //  Fields
    public Rectangle collision;
    public boolean isDynamicSize;
    public Array<Entity> overlappingEntities;
    public boolean active;
    
    
    /*  Collision box is based on an associated drawable.
    */
   
    
    //  Constructor
    public Collision(TextureDrawable drawable, boolean dynamicSize)
    {
        collision = new Rectangle(
            drawable.position.x,
            drawable.position.y,
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
        isDynamicSize = dynamicSize;
        overlappingEntities = new Array<>(false, 64);
        active = true;
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
