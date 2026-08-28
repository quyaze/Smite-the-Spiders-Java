package quyaze.stsj.core;

/**
 * If a class constructor has unsafe initialization or behavior,
 * but requires some initializing behavior, {@link #create()} can
 * be implemented and called. To be specific, first allow the class
 * to instantiate. Then, call {@code create()}.
*/
public interface PostConstruct
{
    /** Call soon after constructor. */
    public void create();
}
