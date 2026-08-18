package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** System in charge of avatar movement. */
public class AvatarSystem implements SystemForEW
{
    //  Fields
    final private GameplayEntityWorld world;
    
    
    //  Constructor
    public AvatarSystem(GameplayEntityWorld world)
    {
        this.world = world;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Mobility mobility = world.mobilityDatastore.get(entity);
        
        if (mobility == null) return; // Entity is just a static avatar
        
        Avatar avatar = world.avatarDatastore.get(entity);
        Player player = world.playerDatastore.get(entity);
        
        avatar.position.add(mobility.getVelocity().scl(Gdx.graphics.getDeltaTime()));
        
        if (player == null) return;
        
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
