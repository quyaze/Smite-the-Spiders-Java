package quyaze.stsj.core;

import quyaze.stsj.SmiteTheSpiders;

/** Context for a {@link EWSystem} under a {@link EntityWorld}. */
public abstract class WorldContext<T extends ScreenContext<?>> implements PostConstruct
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
    public SmiteTheSpiders getGameInstance()
    {
        return world.getGameInstance();
    }
}
