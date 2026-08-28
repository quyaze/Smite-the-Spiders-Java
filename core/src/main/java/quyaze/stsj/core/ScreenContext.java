package quyaze.stsj.core;

import com.badlogic.gdx.Screen;

import quyaze.stsj.SmiteTheSpiders;

/** Context for a {@link Screen}-'owned' object. */
public abstract class ScreenContext<T extends GameContext> implements PostConstruct
{
    private T inst;
    public void setScreen(T inst)
    {
        if (this.inst != null)
            throw new IllegalStateException("cannot reassign the screen owner");
        this.inst = inst;
        create();
    }
    public T getScreen()
    {
        if (inst == null)
            throw new IllegalStateException("no assigned owner");
        return inst;
    }
    public SmiteTheSpiders getGameInstance()
    {
        return inst.getGameInstance();
    }
}
