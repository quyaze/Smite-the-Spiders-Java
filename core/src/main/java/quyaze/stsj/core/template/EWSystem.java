package quyaze.stsj.core.template;

/**
 * Plug-in for {@link EntityWorld}.
 * <p></p>
 * Allows implementing classes to define its interaction with
 * entities and associated data from the world and
 * {@link EWDatastore}.
 */
public interface EWSystem
{
    /*  Iterate  */
    public void iterate(int entity);
}
