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
    private Vector2 movementAxis2d = Vector2.Zero;
    
    
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
        //  TODO: game controller
        //  1.  Evaluate move direction from player input
        final boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        final boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        final boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        final boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        
        //  2.  Map input to movement
        movementAxis2d.setZero();
        if (upPressed) movementAxis2d.add(0f, 1f);      //  Input up
        if (rightPressed) movementAxis2d.add(1f, 0f);   //  Input right
        if (downPressed) movementAxis2d.sub(0f, 1f);    //  Input down
        if (leftPressed) movementAxis2d.sub(1f, 0f);    //  Input left
        
        //  2.  No input from the user
        if (!(upPressed || rightPressed || downPressed || leftPressed)) return;
        
        //  3.  Calculate movement then move
        direction = MathUtils.atan2(movementAxis2d.y, movementAxis2d.x);
        sprite.translate(getSpeedX() * deltaSeconds, getSpeedY() * deltaSeconds);
        
        //  4.  Prevent wizard from going off screen
        final float x = sprite.getX();
        final float y = sprite.getY();
        final float xMax = Gdx.graphics.getWidth() - MathUtils.ceil(getTrueScaleX());
        final float yMax = Gdx.graphics.getHeight() - MathUtils.ceil(getTrueScaleY());
        
        if (x < 0 || x > xMax) sprite.setX(MathUtils.clamp(x, 0, xMax));
        if (y < 0 || y > yMax) sprite.setY(MathUtils.clamp(y, 0, yMax));
    }
    
    
    /**
     * Player logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {}
}
