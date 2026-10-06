package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.architecture.Mobility;
import quyaze.stsj.core.architecture.Player;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.gameplay.GameplayWorld;

/** System that enables {@link Player} action. */
public class PlayerSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Player player = world.playerDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        
        Vector2 movementInput = player.movementInput;
        
        /*  Input  */
        movementInput.setZero();
        player.updateKeymap();
        
        /*  Logic  */
        if (player.keymap[0]) movementInput.y += 1f;
        if (player.keymap[1]) movementInput.x += 1f;
        if (player.keymap[2]) movementInput.y -= 1f;
        if (player.keymap[3]) movementInput.x -= 1f;
        if (player.keymap[4]) player.onCastFireball.fire();
        
        if (player.flagShouldRespawn)
        {
            player.flagShouldRespawn = false;
            player.spawnPlayer(world);
        }
        
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
    
    
    /*  Render  */
    @Override
    public void render(final float dS) {}
    
    
    /*  Post Entity Batch  */
    @Override public void postEntityBatch() {}
}