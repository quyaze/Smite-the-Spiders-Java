/*
//      Collision.java
*/


package latech.stsj.gameplay.stores;

import com.badlogic.gdx.math.Rectangle;

public class Collision
{
    //  Fields
    public Rectangle collision;
    
    
    //  Constructor
    public Collision(TextureDrawable playerTex)
    {
        collision = new Rectangle(
            playerTex.tex.getRegionX(),
            playerTex.tex.getRegionY(),
            playerTex.getTrueWidth(),
            playerTex.getTrueHeight()
        );
    }
}
