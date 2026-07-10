//  TODO: cast fireball

package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import latech.stsj.Main;

/**
 * Player class for the game
 */
public class Player extends Entity
{
    //  Fields
    public boolean enableInput = false;
    private Vector2 movementAxis2d = Vector2.Zero;
    private float movementSensitivity = 0.2f;
    
    
    //  Constructor
    public Player(final Main game)
    {
        sprite = new Sprite(game.getAtlas().findRegion("wizard"));
        sprite.setScale(4f);
        speed *= 6f;
    }
    
    
    /**
     * Player input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void input(float deltaSeconds)
    {
        //  1.  Is input enabled
        if (!enableInput) return;
        
        //  2.  Move
        final int xMax = Gdx.graphics.getWidth() - (int) (getTrueScaleX());
        final int yMax = Gdx.graphics.getHeight() - (int) (getTrueScaleY());
        
        movementAxis2d.setZero();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            movementAxis2d.add(0f, 1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            movementAxis2d.add(1f, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            movementAxis2d.sub(0f, 1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            movementAxis2d.sub(1f, 0f);
        }
        
        //  Stop here if frozen or there is no input from the user
        if (movementAxis2d.len2() < movementSensitivity || frozen) return;
        direction = MathUtils.atan2(movementAxis2d.y, movementAxis2d.x);
        sprite.translate(getSpeedX() * deltaSeconds, getSpeedY() * deltaSeconds);
        
        //  3.  Prevent sprite from going off screen
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
}
