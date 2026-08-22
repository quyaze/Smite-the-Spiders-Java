package quyaze.stsj.core;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/** A representation of a central hub containing events. */
public class EventHub
{
    //  Fields
    private Timer timer;
    private Event<?>[] events;
    private HashMap<String, Integer> nameToIndex;
    
    
    //  Constructor
    /** Construct, pending a timer. */
    public EventHub(int capacity)
    {
        events = new Event[capacity];
        nameToIndex = new HashMap<>(capacity * 3);
    }
    
    /** Construct with a reference to a timer. */
    public EventHub(int capacity, Timer timer)
    {
        this(capacity);
        setTimer(timer);
    }
    
    
    /** Set the timer for deferred binds/listeners. */
    public void setTimer(Timer timer)
    {
        this.timer = timer;
    }
    
    
    /** Add an event with a {@code name}. */
    public <T> void addEvent(String name, Class<T> eventDefinition)
    {
        nameToIndex.put(name, events.length);
        events[events.length] = new Event<T>(eventDefinition);
    }
    
    
    /** Fire the event by {@code name}. */
    @SuppressWarnings("unchecked")
    public <T> void fireEvent(String name, T eventDef)
    {
        Event<T> event;
        {
            Event<?> eventUnknown = events[nameToIndex.get(eventDef)];
            if (eventUnknown == null) return; // Not found
            if (eventDef.getClass() != eventUnknown.eventDefinition) throw new IllegalArgumentException("incorrect event definition"); 
            event = (Event<T>) eventUnknown;  
        }
        event.fire(timer, eventDef);
    }
    
    
    /** Event. */
    final private static class Event<T>
    {
        public Array<Bind<T>> immediate;
        public Array<Bind<T>> deferred;
        // public Array<Bind<T>> userDeferred;
        final public Class<T> eventDefinition;
        public Event(Class<T> eventDefinition)
        {
            immediate = new Array<>(false, 0);
            deferred = new Array<>(false, 0);
            // userDeferred = new Array<>(false, 0);
            this.eventDefinition = eventDefinition;
        }
        public void fire(Timer timer, T eventArgs)
        {
            if (deferred.notEmpty())
            {
                if (timer == null) throw new IllegalStateException("timer is not assigned");
                timer.postTask(
                    new Task()
                    {
                        @Override public void run()
                        {
                            for (int i = 0; i < deferred.size; i++) deferred.get(i).fire(eventArgs);
                        }
                    }
                );
            }
            if (immediate.notEmpty())
            {
                for (int i = 0; i < immediate.size; i++) immediate.get(i).fire(eventArgs);
            }
        }
    }
    
    
    /** Bind. */
    @FunctionalInterface
    public static interface Bind<T>
    {
        /** Bind to owning {@link Event#fire(Timer, Object)}. */
        public void fire(T eventDef);
    }
}
