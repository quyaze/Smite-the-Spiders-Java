package quyaze.stsj.core;

import com.badlogic.gdx.utils.Array;

public class Event<T>
{
    //  Fields
    final private Array<EventBind<T>> binds;
    
    
    //  Constructor
    public Event(int capacity)
    {
        binds = new Array<>(false, capacity);
    }
    
    
    /** Add an {@link EventBind}. */
    public void addBind(EventBind<T> bind)
    {
        binds.add(bind);
    }
    
    
    /** Clear the event of all binds. */
    public void clearBinds()
    {
        binds.clear();
    }
    
    
    /** Fire the event and execute all binds. */
    public void fire(T eventDefinition)
    {
        for (int i = 0; i < binds.size; i++) binds.get(i).onEvent(eventDefinition);
    }
}
