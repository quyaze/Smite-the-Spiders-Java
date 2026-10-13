package quyaze.stsj.core.architecture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * <hr>
 * Provides movement capabilities.
*/
public class Mobility
{
    /*  Fields  */
    private Vector2 velocity;
    private float angle, speed = 100f;
    
    /*  Velocity and speed are in pixels per sceond
        Angle is in radians
        
        Velocity is an xy-coordinate vector that represents speed as the
        magnitude. It is used for character direction.
        
        By default, characters move both x and y pixels by speed * time
        (equals distance and where time is frame time - deltaSeconds).
        This allows characters to move faster diagonally across the
        screen than in a straight x- or y-direction.
        
        Velocity is calculated from angles and scaled by speed. When
        used with Avatar.position, they will move in a linear speed in
        any direction.
        
        Velocity and speed coexist.
    */
    
    
    /*  Constructors  */
    /** <hr> Set a speed of 100, going right. */
    public Mobility()
    {
        velocity = Vector2.Zero.cpy();
        applyDirection();
    }
    
    
    /** Set the speed, going right. */
    public Mobility(float speed)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        applyDirection();
    }
    
    
    /** Set the speed and direction with a relative Vector2. */
    public Mobility(float speed, Vector2 relative)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        setDirection(relative);
    }
    
    
    /** Set speed and direction with relative xy-coordinates. */
    public Mobility(float speed, float relativeX, float relativeY)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        setDirection(relativeX, relativeY);
    }
    
    
    /** Set the speed and an exact angle in degrees. */
    public Mobility(float speed, float angleDeg)
    {
        this();
        this.speed = speed;
        angle = angleDeg * MathUtils.degreesToRadians;
        applyDirection();
    }
    
    
    /**
     * @return A copy of the velocity
    */
    public Vector2 getVelocity()
    {
        return velocity.cpy();
    }
    
    
    /**
     * @return x-velocity
    */
    public float getVelocityX()
    {
        return velocity.x;
    }
    
    
    /**
     * @return y-velocity
    */
    public float getVelocityY()
    {
        return velocity.y;
    }
    
    
    /**
     * @return Actual linear speed
    */
    public float getSpeed()
    {
        return speed;
    }
    
    
    /** Set speed. */
    public void setSpeed(float speed)
    {
        if (!Float.isFinite(speed)) return;
        this.speed = Math.abs(speed);
        if (speed < 0f) angle -= MathUtils.PI * Math.signum(angle);
        applyDirection();
    }
    
    /** Set angular direction with the given Vector2 coordinates. */
    public void setDirection(Vector2 vecDir)
    {
        angle = MathUtils.atan2(vecDir.y, vecDir.x);
        applyDirection();
    }
    
    
    /** Set angular direction in the x- and y-coordinates. */
    public void setDirection(float dirX, float dirY)
    {
        angle = MathUtils.atan2(dirY, dirX);
        applyDirection();
    }
    
    
    /*  Apply Direction  */
    private void applyDirection()
    {
        velocity.set(MathUtils.cos(angle), MathUtils.sin(angle)).scl(speed);
    }
}
