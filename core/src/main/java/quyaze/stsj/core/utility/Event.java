package quyaze.stsj.core.utility;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Enables event behavior and bindings/listeners.
 * <p></p>
 * Events establish an argument definition, which is basically
 * a class representing a pack of arguments.
 * {@link Event#fire(Object)} passes down these arguments into all
 * binds.
*/
final public class Event<T>
{
    /*  Fields  */
    private Array<Bind<T>> bindings;
    private int defers = 0;
    final private Class<T> definition;
    
    
    /*  Constructor  */
    /** Set an initial capacity for {@link Bind}s. */
    public Event(int capacity, Class<T> definition)
    {
        bindings = new Array<>(false, capacity);
        this.definition = definition;
    }
    
    
    /** Fire the event, passing arguments down to {@link Bind}s. */
    public void fire(T args)
    {
        if (args == null)
            throw new NullPointerException("null argument");
        if (!definition.isInstance(args))
            throw new IllegalArgumentException("passed incorrect argument definition");
        
        if (defers > 0)
        {
            Timer.post(
                new Task()
                {
                    @Override public void run()
                    {
                        fireByType(BindType.DEFER, args);
                    }
                }
            );
        }
        fireByType(BindType.IMMEDIATE, args);
    }
    
    
    /**
     * Bind to the event.
     * <p></p>
     * The binding will be deferred every {@link Event#fire(Object)}.
     * @return The created {@link Bind}
     */
    public Bind<T> addBinding(BindDefinition<T> bind)
    {
        return addBinding(bind, BindType.DEFER, false);
    }
    
    
    /**
     * Bind to the event with the specified {@link BindType}.
     * @return The created {@link Bind}
     */
    public Bind<T> addBinding(BindDefinition<T> bind, BindType bindType)
    {
        return addBinding(bind, bindType, false);
    }
    
    
    /**
     * Bind to the event.
     * <p></p>
     * Fires only once. It is deferred and then unbinded afterward.
     * @return The created {@link Bind}
     */
    public Bind<T> addBindingOnce(BindDefinition<T> bind)
    {
        return addBinding(bind, BindType.DEFER, true);
    }
    
    
    /**
     * Bind to the event with the specified {@link BindType}.
     * <p></p>
     * Fires only once and then unbinded afterward.
     * @return The created {@link Bind}
     */
    public Bind<T> addBindingOnce(BindDefinition<T> bind, BindType bindType)
    {
        return addBinding(bind, bindType, true);
    }
    
    
    /**
     * Helper method for adding binds.
     */
    private Bind<T> addBinding(BindDefinition<T> bind, BindType bindType, boolean once)
    {
        Bind<T> binding = new Bind<>();
        if (bindType == BindType.DEFER) defers++;
        
        binding.event = this;
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
    private void fireByType(BindType bindType, T args)
    {
        for (int i = bindings.size - 1; i >= 0; i--)
        {
            Bind<T> binding = bindings.get(i);
            if (binding.bindType == bindType) binding.fire(args);
        }
    }
    
    
    /**
     * Defines the behavior that executes on
     * {@link Event#fire(Object)}.
    */
    @FunctionalInterface
    static public interface BindDefinition<T>
    {
        public void fire(T args);
    }
    
    
    /**
     * A representation of the {@link BindDefinition} that was
     * added to the {@link Event}.
    */
    final static public class Bind<T>
    {
        private Event<T> event;
        private BindDefinition<T> bind;
        private int index;
        public BindType bindType;
        public boolean once;
        private Bind() {}
        private void fire(T args)
        {
            if (once) unbind();
            bind.fire(args);
        }
        
        
        /** Unbind from the {@link Event}. */
        public void unbind()
        {
            if (bindType == BindType.DEFER) event.defers--;
            event.bindings.peek().index = index;
            event.bindings.removeIndex(index);
        }
    }
}
