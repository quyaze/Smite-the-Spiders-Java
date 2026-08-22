package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/** Represents the player. */
public abstract class Player
{
    //  Fields
    public Vector2 movementInput;
    public float maxSpeed = 600f;
    public Vector2 upperScreenBounds;
    
    
    //  Constructors
    /**
     * Create a player pending the character.
     * <p></p>
     * Must call {@link #setAvatar(Avatar)}.
    */
    public Player()
    {
        movementInput = Vector2.Zero.cpy();
        upperScreenBounds = new Vector2(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }
    
    /** Create a player with reference to the character. */
    public Player(Avatar avatar)
    {
        this();
        setAvatar(avatar);
    }
    
    
    /** Set player character. */
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
     * Pass the player wizard for positioning.
     */
    public abstract void onCastFireball(Avatar playerCharacter);
}
