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
    public float randomSpeed()
    {
        return MathUtils.random(160, 200); // 180 ± 20
    }
}
