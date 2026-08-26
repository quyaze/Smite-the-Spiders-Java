package quyaze.stsj.core;

/** Context for a {@link EWSystem} under a {@link EntityWorld}. */
public abstract class EWSystemContext<T extends EntityWorld<?>> implements PostConstruct
{
    private T world;
    public void setWorld(T world)
    {
        if (this.world != null)
            throw new IllegalStateException("cannot reassign the entity world");
        this.world = world;
        create();
    }
    public T getWorld()
    {
        if (world == null)
            throw new IllegalStateException("no assigned entity world");
        return world;
    }
}
