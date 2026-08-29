package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.WorldContext;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** System that simulates {@link Avatar} movement. */
public class AvatarSystem extends WorldContext<GameplayWorld> implements EWSystem
{
    /*  Fields  */
    private GameplayWorld world;
    
    
    /*  Create  */
    @Override public void create()
    {
        world = getWorld();
    }
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        Player player = world.playerDatastore.get(entity);
        
        final float dS = Gdx.graphics.getDeltaTime();
        
        avatar.position.add(mobility.getVelocity().scl(dS));
        
        if (player == null) return;
        
        /*  Keep player from going off-screen
        */
        avatar.position.set(
            MathUtils.clamp(
                avatar.position.x,
                0,
                player.upperScreenBounds.x
            ),
            MathUtils.clamp(
                avatar.position.y,
                0,
                player.upperScreenBounds.y
            )
        );
    }
}
