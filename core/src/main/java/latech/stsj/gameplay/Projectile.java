package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Projectile
{
    private Entity entity;
    
    
    //  Constructor
    public Projectile(TextureRegion tex)
    {
        entity = new Entity(tex);
    }
    
    
    public Entity getEntity()
    {
        //  TODO: javadoc
        return entity;
    }
    
    
    public boolean getIsOutsideScreen()
    {
        //  TODO: javadoc
        final float x = entity.getX();
        final float y = entity.getY();
        final float sizeX = entity.getTrueSizeX();
        final float sizeY = entity.getTrueSizeY();
        final float width = Gdx.graphics.getWidth();
        final float height = Gdx.graphics.getHeight();
        return x > width || y > height || x + sizeX < 0 || y + sizeY < 0;
    }
}
