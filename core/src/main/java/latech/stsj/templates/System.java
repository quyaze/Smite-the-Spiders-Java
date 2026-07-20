/*
//      System.java
*/


package latech.stsj.templates;


/**
 * Abstract class that allows subclasses to run input, logic, etc.
 * per entity, per render call.
 */
public abstract class System
{
    //  Fields
    public Stores<? extends Object>[] stores;
    
    
    /**
     * The primary method that performs a specific task or job per
     * entity and may acquire associated data.
     * <p>
     * Is called from owning <code>World.render(float)</code>.
     * @param deltaSeconds
     * @param entity
     */
    public abstract void render(float deltaSeconds, int entity);
}
