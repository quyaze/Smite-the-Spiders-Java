package quyaze.stsj.gameplay.architecture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.core.Signal;
import quyaze.stsj.core.Utility;

/** Represents a spider. */
public class Spider
{
    /*  Fields  */
    public Vector2 destination;
    
    public Signal onThrowWeb;
    
    
    /*  Constructor  */
    /** Create a spider and immediately move to its destination. */
    public Spider(Avatar avatar, Mobility mobility, float unitsPerPixel)
    {
        destination = Vector2.Zero.cpy();
        onThrowWeb = new Signal();
        newPath(avatar, mobility, unitsPerPixel);
    }
    
    
    /**
     * Set the spider's new path to follow.
     * <p></p>
     * References its {@link Avatar} and {@link Mobility}.
     */
    public void newPath(Avatar avatar, Mobility mobility, float unitsPerPixel)
    {
        final float width = Utility.getScreenWorldWidth(unitsPerPixel);
        final float height = Utility.getScreenWorldHeight(unitsPerPixel);
        
        destination.set(
            MathUtils.random(
                0,
                width - avatar.getTrueWidth()
            ),
            MathUtils.random(
                height * 0.5f,
                height - avatar.getTrueHeight()
            )
        );
        mobility.setDirection(destination.cpy().sub(avatar.position));
        mobility.setSpeed(MathUtils.random(180, 220));
        avatar.texture.flip(
            (mobility.getVelocityX() < 0) == avatar.texture.isFlipX(),
            false
        );
        
        /*  Chance for spiders to throw a web at the player when its set on
            a new path
        */
        if (MathUtils.randomBoolean()) onThrowWeb.fire();
    }
}
