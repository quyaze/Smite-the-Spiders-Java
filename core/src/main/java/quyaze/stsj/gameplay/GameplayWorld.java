package quyaze.stsj.gameplay;

import quyaze.stsj.core.EntityWorld;
import quyaze.stsj.screens.GameplayScreen;

public class GameplayWorld extends EntityWorld
{
    //  Constructor
    public GameplayWorld(GameplayScreen owner)
    {
        super(owner);
    }
    
    
    //  Render
    @Override
    public void render()
    {
        
    }
    
    
    //  Add Entity
    @Override
    public int addEntity()
    {
        return entities++;
    }
    
    
    //  Remove Entity
    @Override
    public void removeEntity(int entity)
    {
        entities--;
    }
}
