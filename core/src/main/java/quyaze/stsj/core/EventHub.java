package quyaze.stsj.core;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.SmiteTheSpiders;

/** A representation of a central hub containing events. */
final public class EventHub
{
    /*  Fields  */
    private Event<?>[] events;
    private Class<?>[] definitions;
    private Bind<?>[][] immediate;
    private Bind<?>[][] deferred;
    private int lastIndex = 0;
    private HashMap<CharSequence, Integer> nameToIndex;
    
    
    /*  Constructor  */
    public EventHub(int capacity)
    {
        events = new Event[capacity];
        definitions = new Class[capacity];
        immediate = new Bind[capacity][0];
        deferred = new Bind[capacity][0];
        nameToIndex = new HashMap<>(capacity);
    }
    
    
    /** Add an event with a given {@code name}. */
    public <T> void addEvent(CharSequence name, Class<T> eventDefinition)
    {
        if (lastIndex++ == events.length) throw new IllegalStateException("too big");
        nameToIndex.put(name, lastIndex);
        events[lastIndex] = new Event<T>(eventDefinition);
    }
    
    
    /**
     * Bind/listen to an event being fired.
     * @return Successful bind
    */
    public <T> boolean bindToEventDeferred(CharSequence name, Bind<T> bind)
    {
        final int index = nameToIndex.get(name);
        Event<T> event = getEvent(name, definitions[nameToIndex.get(name)]);
        if (event == null) throw new NullPointerException("no event of name %s to bind".formatted(name));
        
        return true;
    }
    
    
    /** Fire the event by {@code name}. */
    public <T> void fireEvent(String name, T eventArgs)
    {
        if (!nameToIndex.containsKey(name)) return;
        final int index = nameToIndex.get(name);
        
        if (definitions[index] != eventArgs.getClass())
            throw new IllegalArgumentException("incorrect event argument type");
        
        if (deferred.length > 0)
            {
                SmiteTheSpiders.getTimer().postTask(
                    new Task()
                    {
                        @Override public void run()
                        {
                            Bind<?>[] target = deferred[index];
                            for (int i = 0; i < deferred.length; i++)
                            {
                                // ((Bind<T>) deferred[i]).fire(eventArgs);
                                target[index].fire(eventArgs);
                            }
                        }
                    }
                );
            }
        // if (immediate.length > 0)
        for (int i = 0; i < immediate.length; i++) immediate[i].fire(eventArgs);
    }
    
    
    /**
     * @return Event of the type if it exists and is the correct definition
    */
    @SuppressWarnings("unchecked")
    private <T> Event<T> getEvent(CharSequence name, Class<?> unknownType)
    {
        final int index = nameToIndex.get(name);
        Event<?> unknownEvent = events[index];
        if (unknownEvent == null) return null; // Not found
        if (unknownType != definitions[index]) return null;
        return (Event<T>) unknownEvent;
    }
    
    
    /** Expand the fixed array for the item to fit in. */
    private <T> void expandAndAdd(T[] array, T item)
    {
        final int last = array.length;
        array = Arrays.copyOf(array, last + 1);
        array[last] = item;
    }
    
    
    /* Event */
    final private static class Event<T>
    {
        public Bind<T>[] immediate;
        public Bind<T>[] deferred;
        
        @SuppressWarnings("unchecked")
        public Event(Class<T> eventDefinition)
        {
            immediate = new Bind[0];
            deferred = new Bind[0];
        }
        
        public void bindImmediate(Bind<T> bind)
        {
            final int last = immediate.length;
            immediate = Arrays.copyOf(immediate, immediate.length + 1);
            immediate[last] = bind;
        }
        
        public void bindDeferred(Bind<T> bind)
        {
            final int last = deferred.length;
            deferred = Arrays.copyOf(deferred, deferred.length + 1);
            deferred[last] = bind;
        }
        
        public void fire(T eventArgs)
        {
            if (deferred.length > 0)
            {
                SmiteTheSpiders.getTimer().postTask(
                    new Task()
                    {
                        @Override public void run()
                        {
                            for (int i = 0; i < deferred.length; i++) deferred[i].fire(eventArgs);
                        }
                    }
                );
            }
            // if (immediate.length > 0)
            for (int i = 0; i < immediate.length; i++) immediate[i].fire(eventArgs);
        }
    }
    
    
    /** Binding or listener for an {@link Event}. */
    @FunctionalInterface
    private static interface Bind<T>
    {
        /** Bind function to owning {@link Event#fire(Object)}. */
        public void fire(T eventDef);
    }
}
