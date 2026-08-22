package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** Handles the player. */
public class PlayerSystem implements SystemForEW
{
    //  Fields
    final private GameplayWorld world;
    
    
    //  Constructor
    public PlayerSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Player player = world.playerDatastore.get(entity);
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        
        Vector2 movementInput = player.movementInput;
        
        /*  Input
        */
        movementInput.setZero();
        boolean[] keyMap = new boolean[] {
            Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8),
            Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6),
            Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2),
            Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4),
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            
            /*  0 Go up
                1 Go right
                2 Go down
                3 Go left
                4 Cast spell
            */
        };
        
        /*  Logic
        */
        if (keyMap[0]) movementInput.y += 1f;
        if (keyMap[1]) movementInput.x += 1f;
        if (keyMap[2]) movementInput.y -= 1f;
        if (keyMap[3]) movementInput.x -= 1f;
        if (keyMap[4]) player.onCastFireball(avatar);
        
        //  Gamepad controllers coming soon
        final float inputStrength = movementInput.len2();
        float speed = 0f;
        if (inputStrength > 0.04f) // Deadzone 0.2^2
        {
            float magnitude = (float) Math.sqrt(inputStrength);
            movementInput.scl(1f / magnitude);
            speed = player.maxSpeed * Math.min(magnitude, 1f);
        }
        mobility.setSpeed(speed);
        mobility.setDirection(movementInput);
    }
}