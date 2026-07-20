/*
//      World.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntIntMap;


/**
 * Abstract class representing a basic master over entities and
 * systems.
 */
public abstract class World
{
    //  Fields
    protected IntIntMap entityIds;
    
    
    /**
     * Call in Screen.render(). Subclasses implement their own way
     * of rendering.
     * <p>
     * The implemented method in GameplayWorld, for example,
     * handles input, logic (movement, collision, projectiles), and
     * drawing. Each gameplay aspect has their own render pass in
     * that class.
     * @param deltaSeconds
     */
    public abstract void render(float deltaSeconds);
    
    
    /**
     * Generate an entity.
     * @param system
     * @return Entity no.
     */
    protected int addEntity(int system)
    {
        int size = entityIds.size;
        entityIds.put(size, system);
        return size;
    }
    
    
    /**
     * "Removes" entity. Replaces the entity with the last entity
     * in the list if it is not the last entity.
     * <p>
     * Subclasses use this method but have their own overall way of
     * entity removal. In other words, this method is a helper only.
     * @param entity
     */
    protected void removeEntity(int entity)
    {
        int lastEntity = entityIds.size - 1;
        if (entity != lastEntity) entityIds.put(entity, entityIds.get(lastEntity, -1));
        entityIds.remove(lastEntity, -1);
    }
}
