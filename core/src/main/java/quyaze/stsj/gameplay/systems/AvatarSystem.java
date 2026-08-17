package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;

/** System in charge of avatar movement. */
public class AvatarSystem implements SystemForEW
{
    //  Fields
    final private GameplayEntityWorld gameplay;
    
    
    //  Constructor
    public AvatarSystem(GameplayEntityWorld gameplay)
    {
        this.gameplay = gameplay;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = gameplay.avatarDatastore.get(entity);
        Mobility mobility = gameplay.mobilityDatastore.get(entity);
        
        avatar.position.add(
            new Vector2(
                mobility.getVelocityX(),
                mobility.getVelocityY()
            ).scl(Gdx.graphics.getDeltaTime())
        );
    }
}
