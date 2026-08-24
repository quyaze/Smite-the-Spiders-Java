package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;

/** System in charge of avatar movement. */
public class AvatarSystem implements EWSystem
{
    //  Fields
    final private GameplayWorld world;
    
    
    //  Constructor
    public AvatarSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Mobility mobility = world.mobilityDatastore.get(entity);
        
        Avatar avatar = world.avatarDatastore.get(entity);
        Player player = world.playerDatastore.get(entity);
        
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
