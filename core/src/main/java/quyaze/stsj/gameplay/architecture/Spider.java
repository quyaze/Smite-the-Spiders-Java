package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** Represents the spider. */
public class Spider
{
    //  Fields
    final public Vector2 destination;
    
    
    //  Constructor
    public Spider()
    {
        destination = Vector2.Zero.cpy();
    }
    
    
    /** Generate a random speed for a spider. */
    public static float randomSpeed()
    {
        return MathUtils.random(180, 220); // 200 ± 20
    }
}
