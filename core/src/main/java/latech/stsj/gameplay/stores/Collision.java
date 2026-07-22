/*
//      Collision.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Rectangle;

public class Collision
{
    //  Fields
    public Rectangle collision;
    public float[] margin;
    
    
    //  Constructor
    public Collision(TextureDrawable drawable)
    {
        margin = new float[4];
        collision = new Rectangle(
            drawable.position.x,
            drawable.position.y,
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
    }
    
    
    public void update(TextureDrawable drawable)
    {
        collision.set(
            drawable.position.x,
            drawable.position.y,
            drawable.getTrueWidth(),
            drawable.getTrueHeight()
        );
    }
}
