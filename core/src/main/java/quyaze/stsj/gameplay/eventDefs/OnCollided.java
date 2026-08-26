package quyaze.stsj.gameplay.eventDefs;

import quyaze.stsj.gameplay.architecture.Collision;

/** Event definition for the colliding of two Collision objects. */
final public class OnCollided
{
    public int thisEntity, otherEntity;
    public Collision thisCollision, otherCollision;
    public OnCollided(int thisEntity, int otherEntity, Collision thisCollision, Collision otherCollision)
    {
        this.thisEntity = thisEntity;
        this.otherEntity = otherEntity;
        this.thisCollision = thisCollision;
        this.otherCollision = otherCollision;
    }
}