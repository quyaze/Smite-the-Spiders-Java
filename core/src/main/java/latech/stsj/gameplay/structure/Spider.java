/*
//      Spider.java
*/


package latech.stsj.gameplay.structure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Class representing a Spider.
 */
public class Spider
{
    //  Fields
    public Vector2 destination = Vector2.Zero.cpy();
    
    
    /**
     * 
     */
    public static float randomSpeed()
    {
        return MathUtils.random(180, 220); // 200 ± 20
    }
}
