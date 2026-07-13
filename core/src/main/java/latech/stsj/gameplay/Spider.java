//  TODO: refactor/organize
//  TODO: shoot webs

package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import latech.stsj.Main;

public class Spider
{
    //  Fields
    private Vector2 destination = Vector2.Zero.cpy();
    private Entity entity;
    
    
    //  Constructor
    public Spider(final Main game)
    {
        entity = new Entity(new Sprite(game.getAtlas().findRegion("spider")));
        entity.setScale(4f);
        destination.set(randomDestination());
    }
    
    
    /**
     * @return owned Entity
     */
    public Entity getEntity()
    {
        return entity;
    }
    
    
    /**
     * Spider logic to computer every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {
        float x = entity.getX();
        float y = entity.getY();
        
        //  TODO: tolerance needs to be aware of framerate and speed
        if (MathUtils.isEqual(x, destination.x, 5f) && MathUtils.isEqual(y, destination.y, 5f))
        {
            destination.set(randomDestination());
        }
        entity.translate(entity.getVelocityX() * deltaSeconds, entity.getVelocityY() * deltaSeconds);
    }
    
    
    public void refresh()
    {
        //  TODO: javadoc
        entity.setDirection(destination.x - entity.getX(), destination.y - entity.getY());
    }
    
    
    private Vector2 randomDestination()
    {
        //  TODO: javadoc
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        Vector2 newDestination = new Vector2(MathUtils.random(0, width - entity.getTrueSizeX()), MathUtils.random((int) (height * 0.5f), height - entity.getTrueSizeY()));
        
        entity.setDirection(newDestination.x - entity.getX(), newDestination.y - entity.getY());
        entity.setSpeed(MathUtils.map(0f, 1f, 160f, 200f, MathUtils.random()));
        return newDestination;
    }
}
