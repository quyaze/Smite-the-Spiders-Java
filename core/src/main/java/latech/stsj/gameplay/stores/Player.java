/*
//      Player.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Vector2;

public class Player
{
    public Vector2 movementInput2D;
    public float maxSpeed = 600f;
    
    
    public Player(TextureDrawable playerCharacter)
    {
        movementInput2D = Vector2.Zero.cpy();
    }
}
//  TODO: javadocs