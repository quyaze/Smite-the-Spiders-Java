package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.GameplayWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;

/** System in charge of avatar movement. */
public class AvatarSystem implements EWSystem
{
    //  Fields
    final private GameplayWorld gameplayWorld;
    
    
    //  Constructor
    public AvatarSystem(GameplayWorld gameplayWorld)
    {
        this.gameplayWorld = gameplayWorld;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = gameplayWorld.avatarDatastore.get(entity);
        Mobility mobility = gameplayWorld.mobilityDatastore.get(entity);
        
        avatar.position.add(
            new Vector2(
                mobility.getVelocityX(),
                mobility.getVelocityY()
            ).scl(Gdx.graphics.getDeltaTime())
        );
    }
}
