/*
//      Stores.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntMap;


/**
 * Lays out the stores that hold data for associated entities.
 * This data is accessed for any entity within systems.
 * @param <T> the type of data stored for each entity
 */
public class Stores<T>
{
    //  Fields
    protected IntMap<T> entityData;
    
    
    //  Constructor
    public Stores()
    {
        entityData = new IntMap<T>(64);
    }
    
    
    /**
     * Associate an entity with the corresponding {@code T} data.
     * @param entity the entity identifier
     * @param data the data of type {@code T} to associate with the entity
     */
    public void add(int entity, T data)
    {
        entityData.put(entity, data);
    }
    
    
    /**
     * Remove an entity and its associated {@code T} data.
     * @param entity the entity identifier to remove
     */
    public void remove(int entity)
    {
        int lastEntity = entityData.size - 1;
        if (entity != lastEntity) entityData.put(entity, entityData.get(lastEntity));
        entityData.remove(lastEntity);
    }
    
    
    /**
     * Retrieve the entity's data of type {@code T}.
     * @param entity
     * @return the data associated with the entity or {@code null}
     */
    public T get(int entity)
    {
        return entityData.get(entity);
    }
}
