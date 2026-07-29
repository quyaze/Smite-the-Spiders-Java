/*
//      Entity.java
*/


package latech.stsj.templates;


/**
 * Class representing entities in a World.
 */
final public class Entity
{
    //  Fields
    private int id;
    final private int systemFlag;
    final private Stores<?>[] stores;
    public boolean debris;
    
    
    //  Constructor
    public Entity(int id, int systemFlag, Stores<?>[] stores)
    {
        this.id = id;
        this.systemFlag = systemFlag;
        this.stores = stores;
    }
    
    
    /**
     * @param id Entity's new Id to set
     */
    public void setId(int id)
    {
        for (int i = 0; i < stores.length; i++) stores[i].move(this.id, id);
        this.id = id;
    }
    
    
    /**
     * @return Entity's Id
     */
    public int getId()
    {
        return id;
    }
    
    
    /**
     * @return Entity's designated systems (bitmask flag)
     */
    public int getSystem()
    {
        return systemFlag;
    }
    
    
    /**
     * Entity removes data from all stores associated with itself.
    */
    public void removeFromStores()
    {
        for (int i = 0; i < stores.length; i++) stores[i].remove(id);
    }
}
