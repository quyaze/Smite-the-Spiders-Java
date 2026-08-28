package quyaze.stsj.core;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Enables signal and binding/listener functionality.
 * <p></p>
 * Unlike {@link Event}, {@code Signal} does not have argument
 * definitions. It is basically {@code Event<Void>}.
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
     * Bind to this signal.
     * <p></p>
     * Binding executes immediately on {@link Signal#fire(Object)}.
    */
    public Bind bindImmediate(Bind bind)
    {
        immediate = Utility.append(immediate, bind);
        return bind;
    }
    
    
    /**
     * Bind to this signal.
     * <p></p>
     * Binding executes on the next frame after
     * {@link Signal#fire(Object)}.
    */
    public Bind bindDeferred(Bind bind)
    {
        deferred = Utility.append(deferred, bind);
        return bind;
    }
    
    
    /** Fire the signal. Calls all {@link Bind#onSignal()}s. */
    public void fire()
    {
        if (deferred.length > 0)
        {
            Timer.post(
                new Task()
                {
                    Bind[] target = deferred.clone();
                    @Override public void run()
                    {
                        for (int i = 0; i < target.length; i++) target[i].onSignal();
                    }
                }
            );
        }
        for (int i = 0; i < immediate.length; i++) immediate[i].onSignal();
    }
    
    
    /** A binding/listener for a {@link Signal}. */
    @FunctionalInterface
    public static interface Bind
    {
        public void onSignal();
    }
}
