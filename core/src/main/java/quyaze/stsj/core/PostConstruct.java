package quyaze.stsj.core;

/**
 * General purpose interface. Use cases are mainly coupled classes
 * with initialization issues, allowing code to run after
 * constructing and before implemented create(), etc. Overall
 * allows initialization control.
*/
public interface PostConstruct
{
    /** Call soon after constructor. */
    public void create();
}
