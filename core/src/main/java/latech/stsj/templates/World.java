/*
//      World.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntIntMap;

public abstract class World
{
    protected IntIntMap entityIds;
    public abstract void render(float deltaSeconds);
    
    
    protected int addEntity(int systemCode)
    {
        int size = entityIds.size;
        entityIds.put(size, systemCode);
        return size;
    }
    
    
    protected void removeEntity(int entity)
    {
        int lastEntity = entityIds.size - 1;
        if (entity != lastEntity) entityIds.put(entity, entityIds.get(lastEntity, -1));
        entityIds.remove(lastEntity, -1);
    }
}
