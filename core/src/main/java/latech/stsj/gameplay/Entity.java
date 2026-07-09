package latech.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Entity
{
    //  Fields
    protected Sprite sprite;
    protected float speed = 60f; // Speed: pixels per second
    protected Vector2 directionAxis2d = new Vector2();
    
    
    /**
     * @return Entity's sprite
     */
    public Sprite getSprite()
    {
        return sprite;
    }
    
    
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
}
