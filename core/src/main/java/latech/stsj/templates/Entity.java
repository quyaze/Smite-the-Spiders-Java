/*
//      Entity.java
*/


package latech.stsj.templates;

import java.util.Objects;

/**
 * Represents entities. Contained by worlds, holds data, and
 * managed by systems.
 */
final public class Entity
{
    //  Fields
    private World owner;
    
    
    //  Constructor
    public Entity(World owner)
    {
        this.owner = owner;
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this);
    }
}
