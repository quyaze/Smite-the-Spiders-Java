package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import quyaze.stsj.core.SystemForEW;
import quyaze.stsj.gameplay.GameplayEntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;

/** Handles the drawing and screen rendering. */
public class DrawSystem implements SystemForEW
{
    //  Field
    final private GameplayEntityWorld world;
    final private SpriteBatch batch;
    
    
    //  Constructor
    public DrawSystem(GameplayEntityWorld world, SpriteBatch batch)
    {
        this.world = world;
        this.batch = batch;
    }
    
    
    //  Iterate
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = world.avatarDatastore.get(entity);
        
        batch.setColor(1f, 1f, 1f, avatar.opacity);
        batch.draw(
            avatar.texture,
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
    }
}
