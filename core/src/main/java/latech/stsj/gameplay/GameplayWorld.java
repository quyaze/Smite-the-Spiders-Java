/*
//      GameplayWorld.java
*/


package latech.stsj.gameplay;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import latech.stsj.Main;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.Player;
import latech.stsj.gameplay.stores.Spider;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.gameplay.systems.AvatarSystem;
import latech.stsj.gameplay.systems.PlayerSystem;
import latech.stsj.gameplay.systems.ProjectileSystem;
import latech.stsj.gameplay.systems.SpiderSystem;
import latech.stsj.gameplay.systems.DrawSystem;
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;
import latech.stsj.templates.World;

public class GameplayWorld extends World
{
    //  Fields
    public Stores<TextureDrawable> textureDrawableStore;
    public Stores<Mobility> mobilityStore;
    public Stores<Player> playerStore;
    public Stores<Collision> collisionStore;
    public Stores<Spider> spiderStore;
    
    private PlayerSystem playerSystem;
    private DrawSystem drawSystem;
    private AvatarSystem avatarSystem;
    private SpiderSystem spiderSystem;
    private ProjectileSystem projectileSystem;
    
    final private System[] systems;
    private IntSet entityDebris;
    
    private SpriteBatch batch;
    private ScreenViewport viewport;
    final AtlasRegion texSpell;
    
    final static private char flagPlayer = 1;       //     1 = 1 << 0
    final static private char flagCollision = 2;    //    10 = 1 << 1
    final static private char flagAvatar = 4;       //   100 = 1 << 2
    final static private char flagSpider = 8;       //  1000 = 1 << 3
    final static private char flagTexture = 16;     // 10000 = 1 << 4
    //  << is binary bitshift
    
    
    //  Constructor
    public GameplayWorld(Main main)
    {
        // entityIds = new IntIntMap(32);
        entities = new OrderedSet<>(48);
        
        textureDrawableStore = new Stores<TextureDrawable>();
        mobilityStore = new Stores<Mobility>();
        playerStore = new Stores<Player>();
        collisionStore = new Stores<Collision>();
        spiderStore = new Stores<Spider>();
        
        drawSystem = new DrawSystem(this, main.getBatch());
        avatarSystem = new AvatarSystem(this);
        playerSystem = new PlayerSystem(this);
        spiderSystem = new SpiderSystem(this);
        projectileSystem = new ProjectileSystem(this);
        
        systems = new System[] {
            playerSystem,
            projectileSystem,
            avatarSystem,
            spiderSystem,
            drawSystem
        };
        entityDebris = new IntSet(4);
        
        batch = main.getBatch();
        viewport = main.getViewport();
        
        texSpell = main.getAtlas().findRegion("spell");
        spawnBackground(main);
        spawnPlayer(main);
        spawnSpiders(main);
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds)
    {
        /*  Loop through all systems with the corresponding
            bitmask. Each system has their own render pass and
            perform logic upon all applicable entities.
        */
        System system;
        for (char flag = 1; flag <= flagTexture; flag <<= 1)
        {
            system = systems[Integer.numberOfTrailingZeros(flag)];
            
            /*  If statement below is for the draw pass. A few utilities
                are needed before rendering visible objects on screen.
            */
            if (flag == flagTexture)
            {
                ScreenUtils.clear(Color.BLACK);
                viewport.apply();
                batch.setProjectionMatrix(viewport.getCamera().combined);
                batch.begin();
            }
            
            /*  Pass entities to system
            */
            for (int entity = 0; entity < entityIds.size; entity++)
            {
                if (!isRegistered(entityIds.get(entity, 0), flag))
                {
                    continue;
                }
                system.render(deltaSeconds, entity);
            }
        }
        batch.end();
        
        /*  Deferred entity removal
        */
        for (IntSet.IntSetIterator entities = entityDebris.iterator(); entities.hasNext;)
        {
            
            removeEntity(entities.next());
        }
        entityDebris.clear();
    }
    
    
    //  Remove Entity
    @Override
    protected void removeEntity(int entity)
    {
        super.removeEntity(entity);
    }
    
    
    //  Spawn Background
    private void spawnBackground(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("bg"));
        
        Entity entity = new Entity(
            new System[] {drawSystem},
            new Stores[] {textureDrawableStore}
        );
        tex.setScale(Gdx.graphics.getWidth() / (float) tex.tex.getRegionWidth());
        
        textureDrawableStore.add(entity, tex);
    }
    
    
    //  Spawn Player
    private void spawnPlayer(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("wizard"));
        Mobility mobility = new Mobility();
        Player player = new Player();
        
        int entity = addEntity(flagTexture | flagAvatar | flagPlayer);
        tex.setScale(4f);
        tex.position.set(
            (Gdx.graphics.getWidth() - tex.getTrueWidth()) * 0.5f,
            Gdx.graphics.getHeight() * 0.25f - tex.getTrueHeight() * 0.5f
        );
        
        textureDrawableStore.add(entity, tex);
        mobilityStore.add(entity, mobility);
        playerStore.add(entity, player);
    }
    
    
    //  Spawn Spiders
    private void spawnSpiders(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("spider"));
        Mobility mobility = new Mobility();
        Spider spider = new Spider();
        
        int entity = addEntity(flagTexture | flagAvatar | flagSpider);
        tex.setScale(4f);
        tex.position.set(
            (Gdx.graphics.getWidth() - tex.getTrueWidth()) * 0.5f,
            Gdx.graphics.getHeight() * 0.75f - tex.getTrueHeight() * 0.5f
        );
        mobility.setSpeed(spider.randomSpeed());
        spider.destination.set(tex.position);
        
        textureDrawableStore.add(entity, tex);
        mobilityStore.add(entity, mobility);
        spiderStore.add(entity, spider);
    }
    
    
    /**
     * Called from PlayerSystem.
     * <p>
     * Wizard cast spell.
     * @param playerDrawable Acquire wizard's position so that the
     * spell is casted from the center.
     */
    public void spawnFireball(TextureDrawable playerDrawable)
    {
        TextureDrawable drawable = new TextureDrawable(texSpell);
        Mobility mobility = new Mobility();
        
        int entity = addEntity(flagTexture | flagAvatar | flagCollision);
        drawable.setScale(2f);
        drawable.position.set(
            playerDrawable.position.x + (playerDrawable.getTrueHeight() - drawable.getTrueHeight()) * 0.5f,
            playerDrawable.position.y + (playerDrawable.getTrueWidth() - drawable.getTrueWidth()) * 0.5f
        );
        Collision collision = new Collision(drawable);
        mobility.setSpeed(1000f);
        mobility.setDirection(Vector2.Y.cpy());
        
        textureDrawableStore.add(entity, drawable);
        mobilityStore.add(entity, mobility);
        collisionStore.add(entity, collision);
    }
    
    
    //  Bitmask flags
    private boolean isRegistered(int system, char flag)
    {
        return (system & flag) != 0b0;
    }
    
    
    /**
     * For GameplayWorld.
     * <p>
     * Allows systems to mark entities for removal. Entity removal
     * is deferred each render.
     * @param entity
     */
    public void deferEntityRemoval(int entity)
    {
        entityDebris.add(entity);
    }
    
    
    /**
     * @param width
     * @param height
     */
    public void resize(int width, int height)
    {
        projectileSystem.resize(width, height);
    }
}
