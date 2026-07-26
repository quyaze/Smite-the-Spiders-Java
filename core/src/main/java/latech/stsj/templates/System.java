/*
//      System.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.Array;

/**
 * Abstract class that allows subclasses to run input, logic, etc.
 * per entity, per render call.
 */
public abstract class System
{
    //  Fields
    public Array<Entity> entities;
    
    
    //  Constructor
    public System(boolean ordered, int entityCapacity)
    {
        entities = new Array<>(ordered, entityCapacity);
    }
    
    
    /**
     * TODO: javadoc
     * @param entity
    */
    public void registerEntity(Entity entity)
    {
        entities.add(entity);
    }
    
    
    public void retireEntity()
    {
        
    }
    
    
    /**
     * The primary method for a system to handle an entity,
     * manipulate data, and perform some duty, task, or goal.
     * <p>
     * It should be called from some owning
     * <code>World.render(deltaSeconds)</code> in which it is
     * iterating and passing over its entities.
     * @param deltaSeconds
     * @param entity
     */
    public abstract void render(float deltaSeconds, Entity entity);
}
