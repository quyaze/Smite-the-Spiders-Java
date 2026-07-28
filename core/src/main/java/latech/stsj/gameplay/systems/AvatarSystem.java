/*
//      AvatarSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.math.MathUtils;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for moving beings, characters, objects, etc.
 */
public class AvatarSystem implements System
{
    //  Fields
    private Stores<TextureDrawable> textureDrawableStore;
    private Stores<Mobility> mobilityStore;
    
    
    //  Constructor
    public AvatarSystem(GameplayWorld world)
    {
        textureDrawableStore = world.textureDrawableStore;
        mobilityStore = world.mobilityStore;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = textureDrawableStore.get(entity.getId());
        Mobility mobility = mobilityStore.get(entity.getId());
        
        drawable.position.add(
            mobility.getVelocityX() * deltaSeconds,
            mobility.getVelocityY() * deltaSeconds
        );
        
        if (!mobility.screenContained) return;
        drawable.position.set(
            MathUtils.clamp(drawable.position.x, mobility.screenBounds[0], mobility.screenBounds[2]),
                MathUtils.clamp(drawable.position.y, mobility.screenBounds[1], mobility.screenBounds[3])
        );
    }
}
