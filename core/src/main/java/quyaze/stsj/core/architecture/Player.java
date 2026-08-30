package quyaze.stsj.core.architecture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.utility.Signal;
import quyaze.stsj.core.utility.Utility;

/** Represents the player. */
public class Player
{
    /*  Fields  */
    public Vector2 movementInput;
    public float maxSpeed = 600f;
    public Vector2 upperScreenBounds;
    public boolean[] keymap = new boolean[5];
    public boolean respawn = true;
    private Avatar wizard;
    
    public Signal onCastFireball; // Remove
    
    
    /*  Constructors  */
    /**
     * Create a player pending the character.
     * <p></p>
     * Unsafe; must call {@link #setAvatar(Avatar)}.
    */
    public Player()
    {
        movementInput = Vector2.Zero.cpy();
        upperScreenBounds = Vector2.Zero.cpy();
        onCastFireball = new Signal();
    }
    
    /** Create a player with reference to the character. */
    public Player(Avatar avatar, float unitsPerPixel)
    {
        this();
        setAvatar(avatar, unitsPerPixel);
    }
    
    
    /** Set player character. */
    public void setAvatar(Avatar avatar, float unitsPerPixel)
    {
        Vector2 world = Utility.getScreenWorldSize(unitsPerPixel);
        
        upperScreenBounds.set(world.sub(avatar.getTrueSize()));
        wizard = avatar;
    }
    
    
    /** Spawn the player, referencing its {@link Avatar}. */
    public void spawnPlayer(float unitsPerPixel)
    {
        if (wizard == null)
            throw new IllegalStateException("player has no set avatar");
        
        Vector2 location = new Vector2(
            Utility.getScreenWorldWidth(unitsPerPixel) * 0.5f,
            Utility.getScreenWorldHeight(unitsPerPixel) / 3f
        );
        
        location.sub(wizard.getTrueSize().scl(0.5f));
        
        wizard.position.set(location);
    }
    
    
    /** Updates keymap. */
    public void updateKeymap()
    {
        /*  Keymap:
            
            [0] Move up
            [1] Move right
            [2] Move down
            [3] Move left
            [4] Cast spell
        */
       
        keymap[0] = (
            Gdx.input.isKeyPressed(Input.Keys.W) ||
            Gdx.input.isKeyPressed(Input.Keys.UP) ||
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)
        );
        
        keymap[1] = (
            Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)
        );
        
        keymap[2] = (
            Gdx.input.isKeyPressed(Input.Keys.S) ||
            Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)
        );
        
        keymap[3] = (
            Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)
        );
        
        keymap[4] = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
}
