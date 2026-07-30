/*
//      System.java
*/


package latech.stsj.templates;

// import com.badlogic.gdx.utils.Array;

/**
 * Abstract class that allows subclasses to run input, logic, etc.
 * per entity, per render call.
 */
public interface System
{
    /**
     * The primary method for a system to handle an entity,
     * manipulate data, and perform some duty, task, or goal.
     * <p>
     * It should be called from some owning
     * <code>World.render(deltaSeconds)</code> in which it is
     * iterating and passing over its entities.
     * @param deltaSeconds {@code float}
     * @param entity {@link Entity}
     */
    public void render(float deltaSeconds, Entity entity);
}
