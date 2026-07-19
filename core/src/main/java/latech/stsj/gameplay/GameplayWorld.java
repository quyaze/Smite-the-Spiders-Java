/*
//      GameplayWorld.java
*/


package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
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
import latech.stsj.templates.Stores;
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
    
    private SpriteBatch batch;
    private ScreenViewport viewport;
    
    final static private char flagTexture = 1;      //     1
    final static private char flagAvatar = 2;       //    10
    final static private char flagPlayer = 4;       //   100
    final static private char flagCollision = 8;    //  1000
    final static private char flagSpider = 16;      // 10000
    
    IntArray entityRenderQueue;
    
    
    //  Constructor
    public GameplayWorld(Main main)
    {
        entityIds = new IntIntMap(32);
        
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
        
        batch = main.getBatch();
        viewport = main.getViewport();
        
        entityRenderQueue = new IntArray(false, 32);
        
        /*  The order of these functions determines texture draw
            order/zindex
        */
        spawnSpiders(main);
        spawnPlayer(main);
        spawnBackground(main);
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds)
    {
        for (IntIntMap.Entry entry : entityIds)
        {
            int system = entry.value;
            int entity = entry.key;
            
            if (system == 0)
            {
                removeEntity(entity);
                continue;
            }
            
            if (isRegistered(system, flagTexture)) entityRenderQueue.add(entity);
            if (isRegistered(system, flagPlayer)) playerSystem.render(deltaSeconds, entity);
            if (isRegistered(system, flagAvatar)) avatarSystem.render(deltaSeconds, entity);
            if (isRegistered(system, flagSpider)) spiderSystem.render(deltaSeconds, entity);
            if (isRegistered(system, flagCollision)) projectileSystem.render(deltaSeconds, entity);
        }
        
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        for (int entity : entityRenderQueue.items) drawSystem.render(deltaSeconds, entity);
        batch.end();
        entityRenderQueue.clear();
    }
    
    
    //  Spawn Background
    private void spawnBackground(Main main)
    {
        TextureDrawable tex = new TextureDrawable(main.getAtlas().findRegion("bg"));
        
        int entity = addEntity(flagTexture);
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
    
    
    //  Bitmask flags
    private boolean isRegistered(int system, char flag)
    {
        return (system & flag) != 0b0;
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
