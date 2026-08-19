package quyaze.stsj.core;

/**
 * A binding for an event definition. The function executes on
 * the owning {@link Event}.fire().
 */
@FunctionalInterface
public interface EventBind<T>
{
    public void onEvent(T event);
}
