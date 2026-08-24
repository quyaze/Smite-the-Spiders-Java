package quyaze.stsj.core;

/**
 * Allows implementing classes to define its interaction with
 * entities and associated data from {@link EntityWorld} and
 * {@link EWDatastore}.
 */
public interface EWSystem
{
    /*  Iterate  */
    public void iterate(int entity);
}
