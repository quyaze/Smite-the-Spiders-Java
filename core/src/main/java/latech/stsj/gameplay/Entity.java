package latech.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Entity class to represent the visual aspect of gameplay objects i.e. characters, beings, projectiles.
 */
public class Entity extends Sprite
{
    //  Fields
    private Vector2 velocity;
    private Vector2 trueSize;
    private float speed = 100f; // pixels per second
    
    
    //  Constructor
    public Entity(TextureRegion tex)
    {
        super(tex);
        velocity = new Vector2(speed, 0f);
        trueSize = new Vector2(tex.getRegionWidth(), tex.getRegionHeight());
    }
    
    
    /**
     * @param speed Entity's linear velocity in pixels per second
     */
    public void setSpeed(float speed)
    {
        //  TODO: math check
        final float ratio = speed / this.speed;
        velocity.set(velocity.x * ratio, velocity.y * ratio);
        this.speed = speed;
    }
    
    
    /**
     * @return Sprite's width multiplied by its x-scale; actual screen length of the entity that the end user sees.
     */
    public float getTrueSizeX()
    {
        return trueSize.x;
    }
    
    
    /**
     * @return Sprite's height multiplied by its y-scale; actual screen height of the entity that the end user sees.
     */
    public float getTrueSizeY()
    {
        return trueSize.y;
    }
    
    
    /**
     * @return Entity speed in the x-direction
     */
    public float getVelocityX()
    {
        return velocity.x;
    }
    
    
    /**
     * @return Entity speed in the y-direction
     */
    public float getVelocityY()
    {
        return velocity.y;
    }
    
    /**
     * Calculates angle and velocity from x- and y-coordinates.
     * @param relativeX x-distance
     * @param relativeY y-distance
     */
    public void setDirection(float relativeX, float relativeY)
    {
        final float angle = MathUtils.atan2(relativeY, relativeX);
        velocity.set(speed * MathUtils.cos(angle), speed * MathUtils.sin(angle));
    }
    
    
    /**
     * Entity rendering/drawing for every frame. Only call inside batch.begin() and batch.end()
     * @param batch SpriteBatch for drawing
     */
    public void draw(SpriteBatch batch)
    {
        batch.draw(this, getX(), getY(), getTrueSizeX(), getTrueSizeY());
    }
    
    
    /**
     * Calculates and saves the visible entity scale having been scaled in the overrides below
    */
    private void calculateTrueScale()
    {
        trueSize.set(getWidth() * getScaleX(), getHeight() * getScaleY());
    }
    
    
    //  Scale (float)
    @Override
    public void scale(float amount)
    {
        super.scale(amount);
        calculateTrueScale();
    }
    
    
    //  Set Scale (float)
    @Override
    public void setScale(float scaleXY)
    {
        //  TODO: javadoc
        super.setScale(scaleXY);
        calculateTrueScale();
    }
    
    
    //  Set Scale (float, float)
    @Override
    public void setScale(float scaleX, float scaleY)
    {
        //  TODO: javadoc
        super.setScale(scaleX, scaleY);
        calculateTrueScale();
    }
    
    
    //  Set Center (float, float)
    @Override
    public void setCenter(float x, float y)
    {
        setPosition(x - getTrueSizeX() * 0.5f, y - getTrueSizeY() * 0.5f);
    }
    
    
    //  Set Center X (float)
    @Override
    public void setCenterX(float x)
    {
        setX(x - getTrueSizeX() * 0.5f);
    }
    
    
    //  Set Center Y (float)
    @Override
    public void setCenterY(float y)
    {
        setY(y - getTrueSizeX() * 0.5f);
    }
}
