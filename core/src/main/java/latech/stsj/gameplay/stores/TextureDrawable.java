/*
//      TextureDrawable.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


/**
 * Class providing gameplay characters with a sprite.
 */
public class TextureDrawable
{
    //  Fields
    public TextureRegion tex;
    public Vector2 position;
    public Vector2 anchor;
    private Vector2 trueSize;
    
    
    //  Constructor
    public TextureDrawable(TextureRegion tex)
    {
        this.tex = tex;
        position = Vector2.Zero.cpy();
        anchor = Vector2.One.cpy().scl(-1f);
        trueSize = new Vector2(tex.getRegionWidth(), tex.getRegionHeight());
    }
    
    
    /**
     * @return Original texture width with its scale applied; the actual pixel length that the end user sees.
     */
    public float getTrueWidth()
    {
        return trueSize.x;
    }
    
    
    /**
     * @return Original texture height with its scale applied; the actual pixel length that the end user sees.
     */
    public float getTrueHeight()
    {
        return trueSize.y;
    }
    
    
    /**
     * Set the scale of the texture with a scalar. Uniform and preserves aspect ratio.
     * @param scale
     */
    public void setScale(float scale)
    {
        trueSize.set(tex.getRegionWidth(), tex.getRegionHeight()).scl(scale);
    }
    /**
     * Set the scale of the texture with a Vector2 ( vec1.x * vec2.x, vec1.y * vec2.y ).
     * @param scale2
     */
    public void setScale(Vector2 scale2)
    {
        trueSize.set(tex.getRegionWidth(), tex.getRegionHeight()).scl(scale2);
    }
    /**
     * Set the scale of the texture with an x- and y-scalar ( x1 * x2, y1 * y2 ).
     * @param scaleX
     * @param scaleY
     */
    public void setScale(float scaleX, float scaleY)
    {
        trueSize.set(tex.getRegionWidth(), tex.getRegionHeight()).scl(scaleX, scaleY);
    }
    
    
    /**
     * Set the x-scale of the texture (width).
     * @param scale
     */
    public void setScaleX(float scale)
    {
        trueSize.x = scale * tex.getRegionWidth();
    }
    
    
    /**
     * Set the y-scale of the texture (height).
     * @param scale
     */
    public void setScaleY(float scale)
    {
        trueSize.y = scale * tex.getRegionHeight();
    }
}
