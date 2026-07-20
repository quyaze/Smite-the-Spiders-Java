/*
//      AvatarSystem.java
*/


package latech.stsj.gameplay.systems;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;


/**
 * System for moving beings, characters, objects, etc.
 */
public class AvatarSystem extends System
{
    //  Fields
    private GameplayWorld world;
    
    
    //  Constructor
    public AvatarSystem(GameplayWorld world)
    {
        stores = new Stores[] {
            world.textureDrawableStore,
            world.mobilityStore
        };
        this.world = world;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, int entity)
    {
        TextureDrawable drawable = world.textureDrawableStore.get(entity);
        Mobility mobility = world.mobilityStore.get(entity);
        
        drawable.position.add(
            mobility.getVelocityX() * deltaSeconds,
            mobility.getVelocityY() * deltaSeconds
        );
    }
}
