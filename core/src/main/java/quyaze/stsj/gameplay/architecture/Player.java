package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/** Represents the player. */
public abstract class Player
{
    final public Vector2 movementInput;
    public float maxSpeed = 600f;
    public Vector2 upperScreenBounds;
    
    
    //  Constructors
    
    public Player()
    {
        movementInput = Vector2.Zero.cpy();
        upperScreenBounds = new Vector2(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }
    
    /** Screen bounds is set based on {@code avatar}. */
    public Player(Avatar avatar)
    {
        this();
        setAvatar(avatar);
    }
    
    
    /** Set screen bounds with the player character. */
    public void setAvatar(Avatar avatar)
    {
        upperScreenBounds.set(
            Gdx.graphics.getWidth() - avatar.getTrueWidth(),
            Gdx.graphics.getHeight() - avatar.getTrueHeight()
        );
    }
    
    
    /**
     * Event for when the player is casting a fireball.
     * <p></p>
     * Passes the player wizard for positioning.
     */
    public abstract void onCastFireball(Avatar playerCharacter);
}
