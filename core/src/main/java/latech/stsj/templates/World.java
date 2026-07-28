/*
//      World.java
*/


package latech.stsj.templates;

import com.badlogic.gdx.utils.Array;


/**
 * Abstract class for a World that can belong to a Screen.
 * <p>
 * Default constructors:<br>
 * {@code World(boolean, int)}.<br>
 * Default fields:<br>
 * {@link Array}<{@code Entity}>
 */
public abstract class World
{
    //  Fields
    protected Array<Entity> entities;
    
    
    //  Constructor
    public World(boolean ordered, int entityCapacity)
    {
        entities = new Array<>(ordered, entityCapacity);
    }
    
    
    /**
     * Function to be called every frame for entity processing.
     * @param deltaSeconds
     */
    public abstract void render(float deltaSeconds);
}
