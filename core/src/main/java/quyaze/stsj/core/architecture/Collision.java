package quyaze.stsj.core.architecture;

import com.badlogic.gdx.math.Rectangle;

/**
 * <hr>
 * Represents basic collision.
*/
public class Collision
{
    /*  Fields  */
    
    /** <hr> The collision box. */
    public Rectangle cBox;
    
    /** <hr> Describes the collision to be moveable or static. */
    public boolean dynamic;
    
    /** <hr> Describes the collision to change in size. */
    public boolean resizable;
    
    /** <hr> A flag for the collision to be skipped in a solver. */
    public boolean skipSolving;
    
    
    /*  Constructor  */
    
    /** <hr> New collision. */
    public Collision()
    {
        cBox = new Rectangle();
    }
}
