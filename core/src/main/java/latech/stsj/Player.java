package latech.stsj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

/**
 * Player class for the game
 */
public class Player
{
    //  Fields
    public Sprite sprite;
    private float speed = 3 * 60f;
    
    
    //  Constructor
    public Player(final Main game)
    {
        sprite = new Sprite(game.getAtlas().findRegion("wizard"));
        sprite.setScale(4f);
    }
    
    
    /**
     * Player input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void input(float deltaSeconds)
    {
        //  1.  Move
        final int xMax = Gdx.graphics.getWidth() - (int) (getScaledX());
        final int yMax = Gdx.graphics.getHeight() - (int) (getScaledY());
        
        //  TODO: Advanced movement. Player is currently faster going diagnoal than straight.
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            sprite.translateY(speed * deltaSeconds);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            sprite.translateX(-speed * deltaSeconds);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            sprite.translateY(-speed * deltaSeconds);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            sprite.translateX(speed * deltaSeconds);
        }
        
        //  2.  Prevent sprite from going off screen
        //  Position is acquired here because sprite may have moved
        final float x = sprite.getX();
        final float y = sprite.getY();
        
        if (x < 0 || x > xMax)
        {
            sprite.setX(MathUtils.clamp(x, 0, xMax));
        }
        if (y < 0 || y > yMax)
        {
            sprite.setY(MathUtils.clamp(y, 0, yMax));
        }
    }
    
    
    /**
     * Player logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {}
    
    
    /**
     * @return Player sprite's width multiplied by its x-scale. This is the screen length of the wizard that the player sees.
     */
    public float getScaledX()
    {
        return sprite.getWidth() * sprite.getScaleX();
    }
    
    
    /**
     * @return Player sprite's height multiplied by its y-scale. This is the screen height of the wizard that the player sees.
     */
    public float getScaledY()
    {
        return sprite.getHeight() * sprite.getScaleY();
    }
}
