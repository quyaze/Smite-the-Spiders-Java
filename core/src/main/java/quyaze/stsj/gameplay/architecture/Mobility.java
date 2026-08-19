package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** Provide movement to game {@link Avatar}s. */
public class Mobility
{
    //  Fields
    private Vector2 velocity;
    private float angle;
    private float speed;
    
    /*  Velocity and speed are in pixels per sceond
        Angle is in radians
        Position is in pixels
        
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
    
    
    //  Constructors
    
    /** Set a default speed of 100 and go right. */
    public Mobility()
    {
        velocity = Vector2.Zero.cpy();
        speed = 100f;
        setDirection(Vector2.X.cpy());
    }
    
    
    /** Set speed and go right. */
    public Mobility(float speed)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        setDirection(Vector2.X.cpy());
    }
    
    
    /** Set speed and direction with a Vector2. */
    public Mobility(float speed, Vector2 relative)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        setDirection(relative);
    }
    
    
    /** Set speed and direction with xy-coordinates. */
    public Mobility(float speed, float relativeX, float relativeY)
    {
        velocity = Vector2.Zero.cpy();
        this.speed = speed;
        setDirection(relativeX, relativeY);
    }
    
    
    /** Set speed and an exact angle in degrees. */
    public Mobility(float speed, float angleDeg)
    {
        this();
        this.speed = speed;
        angle = angleDeg * MathUtils.degreesToRadians;
        applyDirection2D();
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
     * @return Actual speed
     */
    public float getSpeed()
    {
        return speed;
    }
    
    
    /**
     * Set speed.
     */
    public void setSpeed(float speed)
    {
        if (this.speed <= 0f) this.speed = 1f;
        velocity.scl(speed / this.speed);
        this.speed = speed;
    }
    
    
    /**
     * Set angular direction with the given Vector2 coordinates.
     */
    public void setDirection(Vector2 relative)
    {
        angle = MathUtils.atan2(relative.y, relative.x);
        applyDirection2D();
    }
    
    
    /**
     * Set angular direction with the given x- and y-coordinates.
     */
    public void setDirection(float relativeX, float relativeY)
    {
        angle = MathUtils.atan2(relativeY, relativeX);
        applyDirection2D();
    }
    
    
    /**  Apply direction from setDirection(...) */
    private void applyDirection2D()
    {
        velocity.set(MathUtils.cos(angle), MathUtils.sin(angle)).scl(speed);
    }
}
