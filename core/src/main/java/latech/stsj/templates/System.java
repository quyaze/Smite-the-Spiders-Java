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
    /**
     * The primary method that performs a specific task or job per
     * entity by acquiring its associated data. To be called each
     * frame.
     * @param deltaSeconds
     * @param entity
     */
    public abstract void render(float deltaSeconds, int entity);
}
