package latech.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Entity class to represent characters and beings
 */
public class Entity
{
    //  Fields
    public Sprite sprite;
    public float direction = 0;
    public float speed = 60f; // pixels per second
    
    
    /**
     * @return Sprite's width multiplied by its x-scale; actual screen length of the entity that the end user sees.
     */
    public float getTrueScaleX()
    {
        return sprite.getWidth() * sprite.getScaleX();
    }
    
    
    /**
     * @return Sprite's height multiplied by its y-scale; actual screen height of the entity that the end user sees.
     */
    public float getTrueScaleY()
    {
        return sprite.getHeight() * sprite.getScaleY();
    }
    
    
    /**
     * @return Entity speed in the x-direction
     */
    public float getSpeedX()
    {
        return speed * MathUtils.cos(direction);
    }
    
    
    /**
     * @return Entity speed in the y-direction
     */
    public float getSpeedY()
    {
        return speed * MathUtils.sin(direction);
    }
    
    
    /**
     * Entity rendering/drawing for every frame. Only call inside batch.begin() and batch.end()
     * @param batch SpriteBatch for drawing
     */
    public void draw(SpriteBatch batch)
    {
        batch.draw(sprite, sprite.getX(), sprite.getY(), getTrueScaleX(), getTrueScaleY());
    }
}
