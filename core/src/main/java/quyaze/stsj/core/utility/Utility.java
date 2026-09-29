package quyaze.stsj.core.utility;

/** General utilities. */
final public class Utility
{
    private Utility()
    {}
    
    
    /**
     * Cast an object to a class.
     * @return Casted class or {@code null} if could not cast
    */
    @SuppressWarnings("unchecked")
    static public <T> T castTo(Object obj, Class<T> target)
    {
        return obj != null && target.isInstance(obj) ? (T) obj : null;
    }
}
