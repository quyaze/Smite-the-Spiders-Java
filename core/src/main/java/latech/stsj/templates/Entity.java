package latech.stsj.templates;


/**
 * Represents entities. Contained by worlds, holds data and
 * managed by systems.
 */
public class Entity
{
    //  Fields
    final private System[] systems;
    final private Stores<? extends Object>[] stores;
    
    
    //  Constructor
    public Entity(System[] systems, Stores<? extends Object>[] stores)
    {
        this.systems = systems;
        this.stores = stores;
    }
    
    
    /**
     * TODO: javadoc
     * @param bitmask
     * @return
     */
    public System bitmaskToSystem(char bitmask)
    {
        return systems[Integer.numberOfLeadingZeros(bitmask)];
    }
}
