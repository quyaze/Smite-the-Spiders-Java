package quyaze.stsj.core.utility;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Enables signal behavior and bindings/listeners.
 * <p></p>
 * {@code Signal} does not pass arguments unlike {@link Event}.
*/
final public class Signal
{
    /*  Fields  */
    private Array<Bind> bindings;
    private int defers = 0;
    
    
    /*  Constructor  */
    /** Set an initial capacity for {@link Bind}s. */
    public Signal(int capacity)
    {
        bindings = new Array<>(false, capacity);
    }
    
    
    /** Signal and notify all {@link Bind}s. */
    public void fire()
    {
        if (defers > 0)
        {
            Timer.post(
                new Task()
                {
                    @Override public void run()
                    {
                        /*  Mutatable before firing especially in Event
                        */
                        fireByType(BindType.DEFER);
                    }
                }
            );
        }
        fireByType(BindType.IMMEDIATE);
    }
    
    
    /**
     * Bind to the signal.
     * <p></p>
     * The binding will be deferred every {@link Signal#fire()}.
     * @return The created {@link Bind}
     */
    public Bind addBinding(BindDefinition bind)
    {
        return addBinding(bind, BindType.DEFER, false);
    }
    
    
    /**
     * Bind to the signal with the specified {@link BindType}.
     * @return The created {@link Bind}
     */
    public Bind addBinding(BindDefinition bind, BindType bindType)
    {
        return addBinding(bind, bindType, false);
    }
    
    
    /**
     * Bind to the signal.
     * <p></p>
     * Fires only once. It is deferred and then unbinded afterward.
     * @return The created {@link Bind}
     */
    public Bind addBindingOnce(BindDefinition bind)
    {
        return addBinding(bind, BindType.DEFER, true);
    }
    
    
    /**
     * Bind to the signal with the specified {@link BindType}.
     * <p></p>
     * Fires only once and then unbinded afterward.
     * @return The created {@link Bind}
     */
    public Bind addBindingOnce(BindDefinition bind, BindType bindType)
    {
        return addBinding(bind, bindType, true);
    }
    
    
    /**
     * Helper method for adding binds.
     */
    private Bind addBinding(BindDefinition bind, BindType bindType, boolean once)
    {
        Bind binding = new Bind();
        if (bindType == BindType.DEFER) defers++;
        
        binding.signal = this;
        binding.bind = bind;
        binding.index = bindings.size;
        binding.bindType = bindType;
        binding.once = once;
        bindings.add(binding);
        return binding;
    }
    
    
    /**
     * Helper method for firing binds.
     */
    private void fireByType(BindType bindType)
    {
        for (int i = bindings.size - 1; i >= 0; i--)
        {
            Bind binding = bindings.get(i);
            if (binding.bindType == bindType) binding.fire();
        }
    }
    
    
    /**
     * Defines the behavior that executes on {@link Signal#fire()}.
    */
    @FunctionalInterface
    static public interface BindDefinition
    {
        public void fire();
    }
    
    
    /**
     * A representation of the {@link BindDefinition} that was
     * added to the {@link Signal}.
    */
    final static public class Bind
    {
        private Signal signal;
        private BindDefinition bind;
        private int index;
        public BindType bindType;
        public boolean once;
        private Bind() {}
        private void fire()
        {
            if (once) unbind();
            bind.fire();
        }
        
        
        /** Unbind from the {@link Signal}. */
        public void unbind()
        {
            if (bindType == BindType.DEFER) signal.defers--;
            signal.bindings.peek().index = index;
            signal.bindings.removeIndex(index);
        }
    }
}
