/*
//      Player.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Player
{
    public Vector2 movementInput2D;
    public Vector2 screenBounds;
    public float maxSpeed = 600f;
    
    
    public Player()
    {
        movementInput2D = Vector2.Zero.cpy();
        screenBounds = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
