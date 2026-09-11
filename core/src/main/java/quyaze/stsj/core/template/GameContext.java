package quyaze.stsj.core.template;

import quyaze.stsj.SmiteTheSpiders;

/** Context for a general {@link SmiteTheSpiders}-'owned' object. */
public abstract class GameContext implements PostConstruct
{
    private SmiteTheSpiders inst;
    public void setGameInstance(SmiteTheSpiders inst)
    {
        if (this.inst != null)
            throw new IllegalStateException("cannot reassign the game instance");
        this.inst = inst;
        create();
    }
    public SmiteTheSpiders getGameInstance()
    {
        if (inst == null)
            throw new IllegalStateException("no assigned game instance");
        return inst;
    }
}
