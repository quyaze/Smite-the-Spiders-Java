package quyaze.stsj.core;

import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.SmiteTheSpiders;

/**
 * Enables signal and binding/listener functionality.
 * <p></p>
 * Unlike {@link Event}, {@code Signal} does not pass argument
 * definitions; it is similar to {@code Event<Void>}.
*/
public class Signal
{
    /*  Fields  */
    private Bind[] immediate;
    private Bind[] deferred;
    
    
    /*  Constructor  */
    public Signal()
    {
        immediate = new Bind[0];
        deferred = new Bind[0];
    }
    
    
    /**
     * Bind to this event.
     * <p></p>
     * Binding executes immediately on {@link Event#fire(Object)}.
    */
    public void bindImmediate(Bind bind)
    {
        immediate = SmiteTheSpiders.getUtility().append(immediate, bind);
    }
    
    
    /**
     * Bind to this event.
     * <p></p>
     * Binding executes on the next frame after
     * {@link Event#fire(Object)}.
    */
    public void bindDeferred(Bind bind)
    {
        deferred = SmiteTheSpiders.getUtility().append(deferred, bind);
    }
    
    
    /** Fire the event. Calls all {@link Bind#onEvent(Object)}s. */
    public void fire()
    {
        if (deferred.length > 0)
        {
            SmiteTheSpiders.getTimer().postTask(
                new Task()
                {
                    Bind[] target = deferred.clone();
                    @Override public void run()
                    {
                        for (int i = 0; i < target.length; i++) target[i].onEvent();
                    }
                }
            );
        }
        // if (immediate.length > 0)
        for (int i = 0; i < immediate.length; i++) immediate[i].onEvent();
    }
    
    
    /** A binding/listener for a {@link Signal}. */
    @FunctionalInterface
    public static interface Bind
    {
        public void onEvent();
    }
}
