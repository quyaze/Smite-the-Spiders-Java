//  TODO: refactor/organize
//  TODO: shoot webs

package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import latech.stsj.Main;

public class Spider extends Entity
{
    //  Fields
    private Vector2 destination = new Vector2();
    
    
    //  Constructor
    public Spider(final Main game)
    {
        sprite = new Sprite(game.getAtlas().findRegion("spider"));
        sprite.setScale(4f);
        destination.set(randomDestination());
    }
    
    
    /**
     * Spider logic to computer every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {
        float x = sprite.getX();
        float y = sprite.getY();
        
        //  TODO: tolerance needs to be aware of framerate and speed
        if (MathUtils.isEqual(x, destination.x, 5f) && MathUtils.isEqual(y, destination.y, 5f))
        {
            destination.set(randomDestination());
        }
        sprite.translate(getSpeedX() * deltaSeconds, getSpeedY() * deltaSeconds);
    }
    
    
    public void refresh()
    {
        //  TODO: javadoc
        direction = MathUtils.atan2(destination.y - sprite.getY(), destination.x - sprite.getX());
    }
    
    
    private Vector2 randomDestination()
    {
        //  TODO: javadoc
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        Vector2 newDestination = new Vector2(MathUtils.random(0, width - getTrueScaleX()), MathUtils.random((int) (height * 0.5f), height - getTrueScaleY()));
        
        direction = MathUtils.atan2(newDestination.y - sprite.getY(), newDestination.x - sprite.getX());
        speed = MathUtils.random(160, 200); // 60 * random
        return newDestination;
    }
}
