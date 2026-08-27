package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** Represents a spider. */
public class Spider
{
    /*  Fields  */
    public Vector2 destination;
    
    
    /*  Constructor  */
    /** Create a spider and immediately move to its destination. */
    public Spider(ScreenViewport viewport, Avatar avatar, Mobility mobility)
    {
        destination = Vector2.Zero.cpy();
        newPath(viewport, avatar, mobility);
    }
    
    
    /**
     * Set the spider's new path to follow.
     * <p></p>
     * References its {@link Avatar} and {@link Mobility}.
     */
    public void newPath(ScreenViewport viewport, Avatar avatar, Mobility mobility)
    {
        final float worldWidth = viewport.getWorldWidth();
        final float worldHeight = viewport.getWorldHeight();
        destination.set(
            MathUtils.random(
                0,
                worldWidth - avatar.getTrueWidth()
            ),
            MathUtils.random(
                worldHeight * 0.5f,
                worldHeight - avatar.getTrueHeight()
            )
        );
        mobility.setDirection(destination.cpy().sub(avatar.position));
        mobility.setSpeed(MathUtils.random(180, 220));
        avatar.texture.flip((mobility.getVelocityX() < 0) == avatar.texture.isFlipX(), false);
    }
}
