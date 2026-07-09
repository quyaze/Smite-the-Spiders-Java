package latech.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;

import latech.stsj.Main;

public class Spider extends Entity
{
    //  Constructor
    public Spider(final Main game)
    {
        sprite = new Sprite(game.getAtlas().findRegion("spider"));
        sprite.setScale(4f);
    }
    
    
    /**
     * Spider logic to computer every frame
     * @param deltaSeconds Time (sec) since last frame
     */
    public void logic(float deltaSeconds)
    {}
}
