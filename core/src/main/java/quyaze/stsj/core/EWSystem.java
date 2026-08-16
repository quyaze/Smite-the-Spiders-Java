package quyaze.stsj.core;

/**
 * A base class for {@link EntityWorld}. Handles entities from
 * World iteration.
 */
public abstract class EWSystem
{
    /**
     * Handle the entity.
     * @param entity
     */
    public abstract void iterate(int entity);
}
