package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** System that enables {@link Player} action. */
public class PlayerSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Create  */
    @Override
    public void create() {}
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Player player = getWorld().getScreen().world.playerDatastore.get(entity);
        Mobility mobility = getWorld().getScreen().world.mobilityDatastore.get(entity);
        
        ScreenViewport viewport = getWorld().getScreen().getGameInstance().getViewport();
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
        
        if (player.respawn)
        {
            player.respawn = false;
            player.spawnPlayer(
                viewport.getWorldWidth(),
                viewport.getWorldHeight()
            );
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
}