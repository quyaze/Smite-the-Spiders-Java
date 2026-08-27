package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EWSystemContext;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** System that simulates {@link Avatar} movement. */
public class AvatarSystem extends EWSystemContext<GameplayWorld> implements EWSystem
{
    /*  Create  */
    @Override public void create() {}
    
    
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = getWorld().avatarDatastore.get(entity);
        Mobility mobility = getWorld().mobilityDatastore.get(entity);
        Player player = getWorld().playerDatastore.get(entity);
        
        final float delta = Gdx.graphics.getDeltaTime();
        
        avatar.position.add(mobility.getVelocity().scl(delta));
        
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
