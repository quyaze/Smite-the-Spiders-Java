package quyaze.stsj.core.template;

/**
 * Plug-in for {@link EntityWorld}.
 * <p></p>
 * Allows implementing classes to define its interaction with
 * entities and associated data from the world and
 * {@link EWDatastore} within {@link #iterate(int)}.
*/
public interface EWSystem
{
    /*  Iterate  */
    public void iterate(int entity);
    
    
    /*  Render  */
    public void render(float dS);
    
    
    /*  Render  */
    public void postEntityBatch();
}
