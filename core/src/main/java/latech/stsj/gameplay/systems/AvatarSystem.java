/*
//      AvatarSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.math.MathUtils;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * System supplementing movement for beings, characters, objects,
 * etc.
 */
public class AvatarSystem implements System
{
    //  Fields
    final private GameplayWorld world;


    //  Constructor
    public AvatarSystem(GameplayWorld world)
    {
        this.world = world;
    }


    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity.getId());
        Mobility mobility = world.mobilityStore.get(entity.getId());

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
