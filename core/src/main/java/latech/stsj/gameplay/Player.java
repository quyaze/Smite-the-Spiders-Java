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
public class Player
{
    //  Fields
    final Main game;
    
    private Vector2 movementAxis2d = Vector2.Zero.cpy();
    public Entity character;
    final private boolean[] keyboardMap = {
        false,  //  Move up
        false,  //  Move right
        false,  //  Move down
        false,  //  Move left
    };
    
    
    //  Constructor
    public Player(final Main game)
    {
        this.game = game;
        
        character = new Entity(new Sprite(game.getAtlas().findRegion("wizard")));
        character.setScale(4f);
        character.setSpeed(600f);
    }
    
    
    /**
     * Player input to process every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void input(float deltaSeconds)
    {
        //  TODO: game controller
        //  Evaluate input to movement
        keyboardMap[0] = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8);
        keyboardMap[1] = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6);
        keyboardMap[2] = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2);
        keyboardMap[3] = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4);
    }
    
    
    /**
     * Player logic to compute every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {
        //  Movement logic if there is movement input
        if (!(keyboardMap[0] || keyboardMap[1] || keyboardMap[2] || keyboardMap[3])) return;
        
        //  Map input to movement
        if (keyboardMap[0]) movementAxis2d.add(0f, 1f);
        if (keyboardMap[1]) movementAxis2d.add(1f, 0f);
        if (keyboardMap[2]) movementAxis2d.sub(0f, 1f);
        if (keyboardMap[3]) movementAxis2d.sub(1f, 0f);
        
        //  Calculate movement then move
        character.setDirection(movementAxis2d.x, movementAxis2d.y);
        character.translate(character.getVelocityX() * deltaSeconds, character.getVelocityY() * deltaSeconds);
        
        //  Prevent wizard from going off screen
        final float x = character.getX();
        final float y = character.getY();
        final float xMax = Gdx.graphics.getWidth() - MathUtils.ceil(character.getTrueSizeX());
        final float yMax = Gdx.graphics.getHeight() - MathUtils.ceil(character.getTrueSizeY());
        
        if (x < 0 || x > xMax) character.setX(MathUtils.clamp(x, 0, xMax));
        if (y < 0 || y > yMax) character.setY(MathUtils.clamp(y, 0, yMax));
        
        //  Reset mapping
        movementAxis2d.setZero();
    }
}
