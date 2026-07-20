/*
//      World.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntIntMap;


/**
 * Abstract class representing the master of entities and systems.
 */
public abstract class World
{
    //  Fields
    protected IntIntMap entityIds;
    
    
    /**
     * Call in Screen.render()
     * @param deltaSeconds
     */
    public abstract void render(float deltaSeconds);
    
    
    /**
     * Generate an entity
     * @param systemCode
     * @return Entity no.
     */
    protected int addEntity(int systemCode)
    {
        int size = entityIds.size;
        entityIds.put(size, systemCode);
        return size;
    }
    
    
    /**
     * "Removes" entity. Replaces the entity with the last entity
     * in the list if it is not the last entity.
     * @param entity
     */
    protected void removeEntity(int entity)
    {
        int lastEntity = entityIds.size - 1;
        if (entity != lastEntity) entityIds.put(entity, entityIds.get(lastEntity, -1));
        entityIds.remove(lastEntity, -1);
    }
}
