package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;

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
        Avatar avatar = world.avatarDatastore.get(entity);
        Mobility mobility = world.mobilityDatastore.get(entity);
        
        float dS = Gdx.graphics.getDeltaTime();
        
        avatar.position.add(
            mobility.getVelocityX() * dS,
            mobility.getVelocityY() * dS
        );
    }
}
