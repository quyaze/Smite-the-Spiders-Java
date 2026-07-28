/*
//      Spider.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Spider
{
    //  Fields
    public Vector2 destination;
    
    
    //  Constructor
    public Spider()
    {
        destination = Vector2.Zero.cpy();
    }
    
    
    /**
     * 
     */
    public static float randomSpeed()
    {
        return MathUtils.random(180, 220); // 200 ± 20
    }
}
