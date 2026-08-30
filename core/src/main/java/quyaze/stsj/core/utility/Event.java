package quyaze.stsj.core.utility;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

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
    public Bind<T> bindImmediate(Bind<T> bind)
    {
        immediate = Utility.append(immediate, bind);
        return bind;
    }
    
    
    /**
     * Bind to this event.
     * <p></p>
     * Binding executes on the next frame after
     * {@link Event#fire(Object)}.
    */
    public Bind<T> bindDeferred(Bind<T> bind)
    {
        deferred = Utility.append(deferred, bind);
        return bind;
    }
    
    
    /** Fire the event. Calls all {@link Bind#fire(Object)}s. */
    public void fire(T argDefinition)
    {
        if (deferred.length > 0)
        {
            Timer.post(
                new Task()
                {
                    Bind<T>[] target = deferred.clone();
                    @Override public void run()
                    {
                        for (int i = 0; i < target.length; i++) target[i].fire(argDefinition);
                    }
                }
            );
        }
        // if (immediate.length > 0)
        for (int i = 0; i < immediate.length; i++) immediate[i].fire(argDefinition);
    }
    
    
    /** A binding/listener for an {@link Event}. */
    @FunctionalInterface
    public static interface Bind<T>
    {
        public void fire(T argDefinition);
    }
}
