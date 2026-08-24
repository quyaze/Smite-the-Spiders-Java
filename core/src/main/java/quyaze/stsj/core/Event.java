package quyaze.stsj.core;

import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.SmiteTheSpiders;

/**
 * Enables event and binding/listener functionality.
 * <p></p>
 * Events allow arguments to pass on {@link #fire(Object)} with the
 * argument definition.
*/
public class Event<T>
{
    /*  Fields  */
    private Bind<T>[] immediate;
    private Bind<T>[] deferred;
    
    
    /*  Constructor  */
    @SuppressWarnings("unchecked")
    public Event()
    {
        immediate = new Bind[0];
        deferred = new Bind[0];
    }
    
    
    /**
     * Bind to this event.
     * <p></p>
     * Binding executes immediately on {@link Event#fire(Object)}.
    */
    public void bindImmediate(Bind<T> bind)
    {
        immediate = SmiteTheSpiders.getUtility().append(immediate, bind);
    }
    
    
    /**
     * Bind to this event.
     * <p></p>
     * Binding executes on the next frame after
     * {@link Event#fire(Object)}.
    */
    public void bindDeferred(Bind<T> bind)
    {
        deferred = SmiteTheSpiders.getUtility().append(deferred, bind);
    }
    
    
    /** Fire the event. Calls all {@link Bind#onEvent(Object)}s. */
    public void fire(T argDefinition)
    {
        if (deferred.length > 0)
        {
            SmiteTheSpiders.getTimer().postTask(
                new Task()
                {
                    Bind<T>[] target = deferred.clone();
                    @Override public void run()
                    {
                        for (int i = 0; i < target.length; i++) target[i].onEvent(argDefinition);
                    }
                }
            );
        }
        // if (immediate.length > 0)
        for (int i = 0; i < immediate.length; i++) immediate[i].onEvent(argDefinition);
    }
    
    
    /** A binding/listener for an {@link Event}. */
    @FunctionalInterface
    public static interface Bind<T>
    {
        public void onEvent(T argDefinition);
    }
}
