/*
//      Spider.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import latech.stsj.templates.Entity;


/**
 * Class representing a Spider.
 */
public class Spider
{
    //  Fields
    public Vector2 destination = Vector2.Zero.cpy();
    public Entity hitSpell;
    
    
    /**
     * 
     */
    public static float randomSpeed()
    {
        return MathUtils.random(180, 220); // 200 ± 20
    }
}
