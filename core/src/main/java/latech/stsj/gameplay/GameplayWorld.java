/*
//      GameplayWorld.java
*/


package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    private SpriteBatch batch;
    private ScreenViewport viewport;
    
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
    private Array<Entity> entityDebris;
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
        super(false, 64);
        
        batch = main.getBatch();
        viewport = main.getViewport();
        
        textureDrawableStore = new Stores<>(64);
        mobilityStore = new Stores<>(64);
        playerStore = new Stores<>(64);
        collisionStore = new Stores<>(64);
        spiderStore = new Stores<>(64);
        
        drawSystem = new DrawSystem(this, batch);
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
        entityDebris = new Array<>(false, 4);
        
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
            for (int i = 0; i < entities.size; i++)
            {
                Entity entity = entities.get(i);
                if (entity.debris) continue;
                if (isRegistered(entity.getSystem(), flag)) system.render(deltaSeconds, entity);
            }
        }
        batch.end();
        
        /*  Entity removal
        */
        for (int i = 0; i < entityDebris.size; i++)
        {
            Entity debris = entityDebris.get(i);
            Entity lastEntity = entities.get(entities.size - 1);
            int debrisId = debris.getId();
            debris.removeFromStores();
            if (debris != lastEntity)
            {
                entities.set(debrisId, lastEntity);
                lastEntity.setId(debrisId);
            }
            entities.removeIndex(entities.size - 1);
        }
        entityDebris.clear();
    }
    
    
    //  Spawn Background
    private void spawnBackground(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("bg"));
        
        Entity entity = new Entity(
            entities.size,
            flagTexture,
            new Stores[] {
                textureDrawableStore
            }
        );
        tex.setScale(Gdx.graphics.getWidth() / (float) tex.tex.getRegionWidth());
        
        entities.add(entity);
        textureDrawableStore.add(entity.getId(), tex);
    }
    
    
    //  Spawn Player
    private void spawnPlayer(Main main)
    {
        //  TODO: order of initialization
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("wizard"));
        tex.setScale(4f);
        tex.position.set(
            (Gdx.graphics.getWidth() - tex.getTrueWidth()) * 0.5f,
            Gdx.graphics.getHeight() * 0.25f - tex.getTrueHeight() * 0.5f
        );
        
        //  TODO: update screen bounds on screen resized
        Mobility mobility = new Mobility();
        mobility.screenContained = true;
        mobility.screenBounds = new float[] {
            0f,
            0f,
            Gdx.graphics.getWidth() - tex.getTrueWidth(),
            Gdx.graphics.getHeight() - tex.getTrueHeight()
        };
        
        Player player = new Player(tex);
        
        Entity entity = new Entity(
            entities.size,
            flagTexture | flagAvatar | flagPlayer,
            new Stores[] {
                textureDrawableStore,
                mobilityStore,
                playerStore
            }
        );
        
        entities.add(entity);
        textureDrawableStore.add(entity.getId(), tex);
        mobilityStore.add(entity.getId(), mobility);
        playerStore.add(entity.getId(), player);
    }
    
    
    //  Spawn Spiders
    private void spawnSpiders(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("spider"));
        Mobility mobility = new Mobility();
        Spider spider = new Spider();
        
        Entity entity = new Entity(
            entities.size,
            flagTexture | flagAvatar | flagSpider,
            new Stores[] {
                textureDrawableStore,
                mobilityStore,
                spiderStore
            }
        );
        tex.setScale(4f);
        tex.position.set(
            (Gdx.graphics.getWidth() - tex.getTrueWidth()) * 0.5f,
            Gdx.graphics.getHeight() * 0.75f - tex.getTrueHeight() * 0.5f
        );
        mobility.setSpeed(spider.randomSpeed());
        spider.destination.set(tex.position);
        
        entities.add(entity);
        textureDrawableStore.add(entity.getId(), tex);
        mobilityStore.add(entity.getId(), mobility);
        spiderStore.add(entity.getId(), spider);
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
        
        Entity entity = new Entity(
            entities.size,
            flagTexture | flagAvatar | flagCollision,
            new Stores[] {
                textureDrawableStore,
                mobilityStore,
                collisionStore
            }
        );
        drawable.setScale(2f);
        drawable.position.set(
            playerDrawable.position.x + (playerDrawable.getTrueHeight() - drawable.getTrueHeight()) * 0.5f,
            playerDrawable.position.y + (playerDrawable.getTrueWidth() - drawable.getTrueWidth()) * 0.5f
        );
        Collision collision = new Collision(drawable);
        mobility.setSpeed(1000f);
        mobility.setDirection(Vector2.Y.cpy());
        
        entities.add(entity);
        textureDrawableStore.add(entity.getId(), drawable);
        mobilityStore.add(entity.getId(), mobility);
        collisionStore.add(entity.getId(), collision);
    }
    
    
    //  Bitmask flags
    private boolean isRegistered(int system, char flag)
    {
        return (system & flag) != 0b0;
    }
    
    
    /**
     * A system requests that an entity should be removed.
     * <p>
     * Entity removal is deferred.
     * @param entity
     */
    public void removeEntity(Entity entity)
    {
        entity.debris = true;
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
