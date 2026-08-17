package quyaze.stsj.core;

/**
 * An interface designed for {@link EntityWorld}. Handles
 * entities from World iteration.
 */
public interface SystemForEW
{
    /**
     * Handle the entity.
     * @param entity
     */
    public void iterate(int entity);
}
