/*
//      Collision.java
*/


package latech.stsj.gameplay.structure;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import latech.stsj.templates.Entity;


/**
 * Class providing collision for gameplay objects and characters.
 * <p>
 * Collision is based on an associated TextureDrawable.
 */
public class Collision
{
    //  Fields
    public Rectangle rectangle;
    public boolean isConstantSize;
    public Array<Entity> collidingEntities;
    public boolean active;
    public boolean screenCullable;
    
    
    /*  Collision box is based on an associated drawable.
    */
   
    
    //  Constructor
    public Collision(TextureDrawable drawable, boolean isConstantSize)
    {
        rectangle = new Rectangle(
            drawable.position.x,
            drawable.position.y,
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
        this.isConstantSize = isConstantSize;
        collidingEntities = new Array<>(false, 64);
        active = true;
    }
    
    
    /**
     * Refresh collision parameters from associated drawable.
     * @param drawable
     */
    public void update(TextureDrawable drawable)
    {
        rectangle.setPosition(drawable.position);
        if (isConstantSize) return;
        rectangle.setSize(
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
    }
}
