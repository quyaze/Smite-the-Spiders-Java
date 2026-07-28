/*
//      Stores.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntMap;


/**
 * A class that acts as a container for data associated with
 * <code>Entity</code>s. Designed to be accessed and not
 * iterated.
 * @param <T> Data associated with entities is of type T
 */
final public class Stores<T>
{
    //  Fields
    private IntMap<T> data;
    private int originalCapacity;
    
    
    //  Constructor
    public Stores(int entityCapacity)
    {
        originalCapacity = entityCapacity;
        data = new IntMap<>(entityCapacity);
    }
    
    
    /**
     * Add an entity and its associated data.
     * @param entityId
     * @param data of type <code>T</code>
     */
    public void add(int entityId, T data)
    {
        this.data.put(entityId, data);
    }
    
    
    /**
     * Remove the entity and its associated <code>T</code> data from
     * the store.
     * @param entityId
     * @return Data <code>T</code> from removed entity. Can but
     * should never return <code>null</code> if used correctly
     */
    public T remove(int entityId)
    {
        return data.remove(entityId);
    }
    
    
    /**
     * Move entity's data from its old id to its new id.
     * @param oldId
     * @param newId
     */
    public void move(int oldId, int newId)
    {
        this.data.put(newId, this.data.get(oldId));
        remove(oldId);
    }
    
    
    /**
     * @param entityId
     * @return The <code>T</code> data associated with the entity
     */
    public T get(int entityId)
    {
        return data.get(entityId);
    }
    
    
    /**
     * Clear the store of all entity data.
    */
    public void clear()
    {
        data.clear(originalCapacity);
    }
}
