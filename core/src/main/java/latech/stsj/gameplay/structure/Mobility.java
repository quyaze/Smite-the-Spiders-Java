/*
//      Mobility.java
*/


package latech.stsj.gameplay.structure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Class providing movement to characters.
 */
public class Mobility
{
    //  Fields
    private Vector2 velocity;
    private float angle;
    private float speed;
    public boolean screenContained;
    public float[] screenBounds;
    
    /*  Velocity and speed are in pixels per sceond
        Angle is in radians
        Position is in pixels
        
        Velocity is an xy-coordinate vector that represents speed as the
        magnitude. It is used for character direction.
        
        By default, characters move both x and y pixels by speed * time
        (equals distance and where time is frame time - deltaSeconds).
        This allows characters to move faster diagonally across the
        screen than in a straight x- or y-direction.
        
        Velocity is calculated from angles and scaled by speed. For the
        most part, gameplay characters now move in a linear speed in any
        direction.
        
        Field screenContained is accessed in AvatarSystem. Keeps
        characters, specifically the player, from going off screen.
    */
    
    
    //  Constructor
    public Mobility()
    {
        velocity = Vector2.Zero.cpy();
        angle = 0f;
        speed = 100f;
    }
    
    
    /**
     * @return A copy of the velocity
     */
    public Vector2 getVelocity()
    {
        return velocity.cpy();
    }
    
    
    /**
     * @return X-velocity
     */
    public float getVelocityX()
    {
        return velocity.x;
    }
    
    
    /**
     * @return Y-velocity
     */
    public float getVelocityY()
    {
        return velocity.y;
    }
    
    
    /**
     * @return Actual speed
     */
    public float getSpeed()
    {
        return speed;
    }
    
    
    /**
     * Set speed.
     * @param speed
     */
    public void setSpeed(float speed)
    {
        if (this.speed <= 0) this.speed = 1;
        velocity.scl(speed / this.speed);
        this.speed = speed;
    }
    
    
    /**
     * Set angular direction with the given Vector2 (x and y).
     * @param relative
     */
    public void setDirection(Vector2 relative)
    {
        angle = MathUtils.atan2(relative.y, relative.x);
        applyDirection2D();
    }
    
    
    /**
     * Set angular direction with the given x- and y-coordinates.
     * @param relativeX
     * @param relativeY
     */
    public void setDirection2D(float relativeX, float relativeY)
    {
        angle = MathUtils.atan2(relativeY, relativeX);
        applyDirection2D();
    }
    
    
    //  Apply direction
    private void applyDirection2D()
    {
        velocity.set(MathUtils.cos(angle), MathUtils.sin(angle)).scl(speed);
    }
}
