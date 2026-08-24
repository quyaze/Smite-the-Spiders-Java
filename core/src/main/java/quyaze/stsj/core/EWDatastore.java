package quyaze.stsj.core;

import com.badlogic.gdx.utils.IntMap;

/**
 * Base class for {@link EntityWorld}. Contains data for associated
 * entities.
 */
public class EWDatastore<T>
{
    /*  Fields  */
    private IntMap<T> data;
    
    
    /*  Constructor  */
    public EWDatastore(int initialCapacity)
    {
        data = new IntMap<T>(initialCapacity);
    }
    
    
    /**
     * Associate {@code data} with the {@code entity}.
     */
    public void put(int entity, T data)
    {
        this.data.put(entity, data);
    }
    
    
    /**
     * Remove associated {@code entity}'s data.
     * @return Data or {@code null}
     */
    public T remove(int entity)
    {
        return this.data.remove(entity);
    }
    
    
    /**
     * Get {@code entity}'s associated data.
     * @return Data or {@code null}
     */
    public T get(int entity)
    {
        return this.data.get(entity);
    }
    
    
    /**
     * Is there data associated with the {@code entity}.
     */
    public boolean contains(int entity)
    {
        return this.data.containsKey(entity);
    }
    
    
    /**
     * Reallocate data from the {@code oldEntity} to the
     * {@code newEntity}. This is needed in entity
     * swap-removal.
     * @return The data that was transferred
     */
    public T transfer(int oldEntity, int newEntity)
    {
        return data.put(newEntity, data.remove(oldEntity));
    }
    
    
    /**
     * Clear all data.
     */
    public void clear()
    {
        data.clear();
    }
}
