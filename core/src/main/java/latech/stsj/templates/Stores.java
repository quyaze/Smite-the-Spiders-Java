/*
//      Stores.java
*/


package latech.stsj.templates;

import java.util.HashMap;


/**
 * A class that acts as a container for data associated with
 * <code>Entity</code>s. Designed to be accessed and not
 * iterated.
 * @param <T> Data associated with entities is of type T
 */
final public class Stores<T>
{
    //  Fields
    private HashMap<Entity, T> data;
    
    
    //  Constructor
    public Stores(int entityCapacity)
    {
        data = new HashMap<>(entityCapacity);
    }
    
    
    /**
     * Add an entity and its associated data.
     * @param entity
     * @param data
     */
    public void add(Entity entity, T data)
    {
        this.data.put(entity, data);
    }
    
    
    /**
     * Remove the entity and its associated <code>T</code> data from
     * the store.
     * @param entity
     * @return Data <code>T</code> from removed entity. Can but
     * should never return <code>null</code> if used correctly
     */
    public T remove(Entity entity)
    {
        return data.remove(entity);
    }
    
    
    /**
     * @param entity
     * @return The <code>T</code> data associated with the entity
     */
    public T get(Entity entity)
    {
        return data.get(entity);
    }
}
