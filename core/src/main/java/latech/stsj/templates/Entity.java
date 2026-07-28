/*
//      Entity.java
*/


package latech.stsj.templates;


/**
 * TODO: javadoc
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
    
    
    public void setId(int id)
    {
        for (int i = 0; i < stores.length; i++) stores[i].move(this.id, id);
        this.id = id;
    }
    
    
    public int getId()
    {
        return id;
    }
    
    
    public int getSystem()
    {
        return systemFlag;
    }
    
    
    public void removeFromStores()
    {
        for (int i = 0; i < stores.length; i++) stores[i].remove(id);
    }
}
//  TODO: javadocs