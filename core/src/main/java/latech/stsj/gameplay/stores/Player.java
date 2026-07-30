/*
//      Player.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Vector2;

import latech.stsj.templates.Entity;


/**
 * Class representing the player.
 */
public class Player
{
    //  Fields
    public Vector2 movementInput2D = Vector2.Zero.cpy();
    public float maxSpeed = 600f;
    public Entity hitWeb;
}
