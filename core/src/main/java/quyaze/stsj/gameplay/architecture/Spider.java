package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** Represents a spider. */
public class Spider
{
    /*  Fields  */
    public Vector2 destination;
    
    
    /*  Constructor  */
    /** Create a spider and immediately move to its destination. */
    public Spider(Avatar avatar, Mobility mobility)
    {
        destination = Vector2.Zero.cpy();
        newPath(avatar, mobility);
    }
    
    
    /**
     * Set the spider's new path to follow.
     * <p></p>
     * References its {@link Avatar} and {@link Mobility}.
     */
    public void newPath(Avatar avatar, Mobility mobility)
    {
        destination.set(
            MathUtils.random(
                0,
                Gdx.graphics.getWidth() - avatar.getTrueWidth()
            ),
            MathUtils.random(
                Gdx.graphics.getHeight() * 0.5f,
                Gdx.graphics.getHeight() - avatar.getTrueHeight()
            )
        );
        mobility.setDirection(destination.cpy().sub(avatar.position));
        mobility.setSpeed(MathUtils.random(180, 220));
        avatar.texture.flip((mobility.getVelocityX() < 0) == avatar.texture.isFlipX(), false);
    }
}
