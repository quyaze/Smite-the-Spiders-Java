package quyaze.stsj.gameplay.eventDefs;

/** Event argument definition for when an entity is changed. */
final public class OnEntityReassigned
{
    final public int oldEntity, newEntity;
    public OnEntityReassigned(int oldEntity, int newEntity)
    {
        this.oldEntity = oldEntity;
        this.newEntity = newEntity;
    }
}
