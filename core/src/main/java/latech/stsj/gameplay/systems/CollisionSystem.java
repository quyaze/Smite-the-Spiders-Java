/*
//      CollisionSystem.java
*/

package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;

import latech.stsj.enums.CollisionType;
import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * TODO: javadoc
 * TODO: when player and spider are hit
 */
public class CollisionSystem implements System
{
    //  Fields
    private GameplayWorld world;
    private Entity player;
    private Array<Entity> spiders;
    private Array<Entity> spells;
    private Array<Entity> webs;
    private IntSet ids;
    
    private Rectangle screen;
    
    private Entity iteratingSpider, iteratingSpell, iteratingWeb;
    private Rectangle rectA, rectB;
    
    
    //  Constructor
    public CollisionSystem(GameplayWorld world)
    {
        this.world = world;
        spiders = new Array<>(false, 3);
        spells = new Array<>(false, 48);
        webs = new Array<>(false, 4);
        ids = new IntSet(3 + 48 + 4);
        screen = new Rectangle(
            0f,
            0f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        int id = entity.getId();
        
        TextureDrawable drawable = world.textureDrawableStore.get(id);
        Collision collision = world.collisionStore.get(id);
        CollisionType type = collision.type;
        
        if (
            (type == CollisionType.SPELL || type == CollisionType.WEB)
            && shouldCull(collision.collision)
        )
        {
            world.removeEntities(entity);
            return;
        }
        
        collision.update(drawable);
        
        if (ids.contains(id)) return;
        ids.add(id);
        
        switch (type)
        {
            case PLAYER: player = entity; break;
                
            case SPIDER: spiders.add(entity); break;
                
            case SPELL: spells.add(entity); break;
                
            case WEB: webs.add(entity); break;
            
            //  Illegal
            default: world.removeEntities(entity); return;
        }
    }
    
    
    /**
     * Calculate collisions.
    */
    public void solve()
    {
        /*  This code could probably be cleaner
        */
        if (player == null) return;
        
        for (int i = 0; i < spells.size; i++)
        {
            iteratingSpell = spells.get(i);
            if (iteratingSpell.debris)
            {
                ids.remove(iteratingSpell.getId());
                continue;
            }
            rectA = world.collisionStore.get(iteratingSpell.getId()).collision;
            for (int j = 0; j < spiders.size; j++)
            {
                iteratingSpider = spiders.get(j);
                if (iteratingSpider.debris)
                {
                    ids.remove(iteratingSpider.getId());
                    continue;
                }
                rectB = world.collisionStore.get(iteratingSpider.getId()).collision;
                if (rectB.overlaps(rectA))
                {
                    /*  Hit spider
                    */
                    world.removeEntities(iteratingSpider, iteratingSpell);
                    break;
                }
            }
        }
        rectB = world.collisionStore.get(player.getId()).collision;
        
        for (int i = 0; i < webs.size; i++)
        {
            iteratingWeb = webs.get(i);
            if (iteratingWeb.debris)
            {
                ids.remove(iteratingWeb.getId());
            }
            rectA = world.collisionStore.get(iteratingWeb.getId()).collision;
            if (rectA.overlaps(rectB))
            {
                /*  Hit player
                */
                break;
            }
        }
        remover();
    }
    
    
    public void remover()
    {
        iteratingSpell = null;
        iteratingSpider = null;
        iteratingWeb = null;
        rectA = null;
        rectB = null;
        player = null;
        ids.clear();
        spiders.clear();
        spells.clear();
        webs.clear();
    }
    
    
    /**
     * @param projectile
     * @return Is projectile completely off the screen
     */
    private boolean shouldCull(Rectangle projectile)
    {
        return !projectile.overlaps(screen);
    }
}
