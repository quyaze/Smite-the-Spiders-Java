/*
//      Stores.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.IntMap;

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
     * Associate entity with the corresponding <T> data.
     * @param entity
     * @param data <T>
     */
    public void add(int entity, T data)
    {
        entityData.put(entity, data);
    }
    
    
    /**
     * Omit entity and its <T> data.
     * @param entity
     */
    public void remove(int entity)
    {
        int lastEntity = entityData.size - 1;
        if (entity != lastEntity) entityData.put(entity, entityData.get(lastEntity));
        entityData.remove(lastEntity);
    }
    
    
    /**
     * Retrieve the entity's <T> data
     * @param entity
     * @return <T>
     */
    public T get(int entity)
    {
        return entityData.get(entity);
    }
}
