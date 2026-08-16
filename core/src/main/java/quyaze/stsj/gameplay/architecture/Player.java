package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.Vector2;

/** Represents the player. */
public class Player
{
    final public Vector2 movementInput;
    public float maxSpeed = 600f;
    
    
    //  Constructor
    public Player()
    {
        movementInput = Vector2.Zero.cpy();
    }
}
