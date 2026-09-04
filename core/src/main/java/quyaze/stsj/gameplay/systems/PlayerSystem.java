package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.core.architecture.Avatar;
import quyaze.stsj.core.architecture.Mobility;
import quyaze.stsj.core.architecture.Player;
import quyaze.stsj.core.template.EWSystem;
import quyaze.stsj.core.template.WorldContext;
import quyaze.stsj.gameplay.GameplayWorld;

import static quyaze.stsj.gameplay.GameplayCore.*;

/** System that enables {@link Player} action. */
public class PlayerSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    private boolean enablePlayerHitEffect;
    private float opacityOverride = 1f;
    private float playerHitEffectDirection = -1f;
    
    
    /*  Create  */
    @Override
    public void create()
    {
        world = getWorld();
        world.getScreen().core.onPlayerHit.addBinding(
            () -> {
                enablePlayerHitEffect = true;
                Timer.schedule(
                    new Task()
                    {
                        @Override public void run()
                        {
                            opacityOverride = 1f;
                            enablePlayerHitEffect = false;
                        }
                    },
                    PLAYER_HIT_FX_PHASE
                );
            }
        );
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Player player = world.playerDatastore.get(entity);
        Avatar avatar = world.avatarDatastore.get(entity);
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
        
        if (player.respawn)
        {
            player.respawn = false;
            player.spawnPlayer(world);
        }
        
        if (enablePlayerHitEffect) avatar.opacity = opacityOverride;
        
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
    
    
    /** On {@link GameplayWorld#render(float)}. */
    public void render(final float dS)
    {
        if (!enablePlayerHitEffect) return;
        
        if (opacityOverride <= PLAYER_HIT_FX_FADE) playerHitEffectDirection = 1f;
        else if (opacityOverride >= 1f) playerHitEffectDirection = -1f;
        
        /*  I definitely did NOT ask AI for the opacity math
        */
        opacityOverride = MathUtils.clamp(
            opacityOverride + dS * PLAYER_HIT_FX_STEP * playerHitEffectDirection,
            PLAYER_HIT_FX_FADE,
            1f
        );
    }
}