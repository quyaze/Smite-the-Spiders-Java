/*
//      GameplayWorld.java
*/


package latech.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import latech.stsj.Main;
import latech.stsj.enums.GameplayPhase;
import latech.stsj.gameplay.stores.Collision;
import latech.stsj.gameplay.stores.Mobility;
import latech.stsj.gameplay.stores.Player;
import latech.stsj.gameplay.stores.Spider;
import latech.stsj.gameplay.stores.TextureDrawable;
import latech.stsj.gameplay.systems.AvatarSystem;
import latech.stsj.gameplay.systems.CollisionSystem;
import latech.stsj.gameplay.systems.PlayerSystem;
import latech.stsj.gameplay.systems.SpiderSystem;
import latech.stsj.gameplay.systems.DrawSystem;
import latech.stsj.templates.Entity;
import latech.stsj.templates.Stores;
import latech.stsj.templates.System;
import latech.stsj.templates.World;


/**
 * World for the gameplay.
*/
final public class GameplayWorld extends World
{
    //  Fields
    private Main main;
    public GameplayState state;
    public GameplayPhase phase;
    
    public Stores<TextureDrawable> textureDrawableStore;
    public Stores<Mobility> mobilityStore;
    public Stores<Player> playerStore;
    public Stores<Collision> collisionStore;
    public Stores<Spider> spiderStore;
    
    private PlayerSystem playerSystem;
    private CollisionSystem collisionSystem;
    private AvatarSystem avatarSystem;
    private SpiderSystem spiderSystem;
    private DrawSystem drawSystem;
    
    private Array<Entity> entityDebris;
    private System iteratingSystem;
    public boolean gamePaused;
    
    private Task collisionSolveTask;
    final static private float collisionSolveInterval = 1/30f;
    
    final static private char flagPlayer = 1;       //      1 = 1 << 0
    final static private char flagCollision = 2;    //     10 = 1 << 1
    final static private char flagAvatar = 4;       //    100 = 1 << 2
    final static private char flagSpider = 8;       //   1000 = 1 << 3
    final static private char flagTexture = 16;     //  10000 = 1 << 4
    /*  << is binary bitshift
    */
    
    
    //  Constructor
    public GameplayWorld(Main main)
    {
        super(false, 64);
        
        this.main = main;
        state = new GameplayState(3);
        phase = GameplayPhase.NORMAL;
        
        textureDrawableStore = new Stores<>(64);
        mobilityStore = new Stores<>(64);
        playerStore = new Stores<>(64);
        collisionStore = new Stores<>(64);
        spiderStore = new Stores<>(64);
        
        playerSystem = new PlayerSystem(this);
        collisionSystem = new CollisionSystem(this);
        avatarSystem = new AvatarSystem(this);
        spiderSystem = new SpiderSystem(this);
        drawSystem = new DrawSystem(this, main.getBatch());
        
        entityDebris = new Array<>(false, 4);
        
        collisionSolveTask = new Timer.Task()
        {
            public void run()
            {
                if (!gamePaused) collisionSystem.solve();
            }
        };
    }
    
    
    /**
     * Create gameplay world. For {@code Screen.show()}.
    */
    public void creator()
    {
        spawnBackground();
        spawnPlayer();
        spawnSpiders();
        main.getTimer().scheduleTask(collisionSolveTask, 0f, collisionSolveInterval, -1);
    }
    
    
    /**
     * Unload gameplay world. For {@code Screen.hide()}.
    */
    public void remover()
    {
        collisionSystem.remover();
        collisionSolveTask.cancel();
        entities.clear();
        entityDebris.clear();
        textureDrawableStore.clear();
        mobilityStore.clear();
        playerStore.clear();
        collisionStore.clear();
        spiderStore.clear();
        gamePaused = false;
        phase = GameplayPhase.NORMAL;
        state.points = 0;
        state.lives = 3;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds)
    {
        /*  Begin by seeing if we are pausing or exiting to the main menu.
        
            Every frame, all systems perform a pass in which
            system.render(float, Entity) is called. These systems access
            data and manipulate entities for gameplay.
            
            An entity applies to a system if its signature is recognizable
            by the system's bitmask flag. If it applies, the entity is
            passed to the system. Entities marked for removal are skipped.
            
            Entities can be marked for removal. Actual removal is deferred.
            The last entity and entity to remove are swapped. The entity to
            remove can then be 'popped' for the array to remain without
            holes.
        */
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) gamePaused = !gamePaused;
        if (gamePaused && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            main.goToMainMenuScreen();
            return;
        }
        
        //  TODO: game state/round based. omit in the future
        if (!gamePaused && Gdx.input.isKeyJustPressed(Input.Keys.Q)) spawnSpiders();
        
        for (char flag = 1; flag <= flagTexture; flag <<= 1)
        {
            if (gamePaused && flag != flagTexture) continue;
            
            switch (flag)
            {
                case flagPlayer: iteratingSystem = playerSystem; break;
                
                case flagCollision: iteratingSystem = collisionSystem; break;
                
                case flagAvatar: iteratingSystem = avatarSystem; break;
                
                case flagSpider: iteratingSystem = spiderSystem; break;
                
                case flagTexture:
                    iteratingSystem = drawSystem;
                    
                    ScreenUtils.clear(Color.BLACK);
                    main.getViewport().apply();
                    main.getBatch().setProjectionMatrix(main.getViewport().getCamera().combined);
                    main.getBatch().begin();
                    break;
                
                default: return;
            }
            
            for (int i = 0; i < entities.size; i++)
            {
                Entity entity = entities.get(i);
                if (entity.debris) continue;
                if (isRegistered(entity.getSystem(), flag)) iteratingSystem.render(deltaSeconds, entity);
            }
            
            if (flag == flagTexture) main.getBatch().end();
            iteratingSystem = null;
        }
        
        /*  Entity removal
        */
        for (int i = 0; i < entityDebris.size; i++)
        {
            Entity debris = entityDebris.get(i);
            int debrisId = debris.getId();
            
            debris.removeFromStores();
            entities.get(entities.size - 1).setId(debrisId);
            entities.removeIndex(debrisId);
        }
        entityDebris.clear();
    }
    
    
    //  Spawn Background
    private void spawnBackground()
    {
        //  Initialize
        Entity entity;
        TextureDrawable drawable;
        
        //  Entity
        int id = entities.size;
        entity = new Entity(
            id,
            flagTexture,
            new Stores[] {
                textureDrawableStore
            }
        );
        
        drawable = new TextureDrawable(main.getAtlas().findRegion("bg"));
        drawable.setScale(Gdx.graphics.getWidth() / (float) drawable.tex.getRegionWidth());
        
        entities.add(entity);
        textureDrawableStore.add(id, drawable);
    }
    
    
    //  Spawn Player
    private void spawnPlayer()
    {
        //  Initialize
        Entity entity;
        TextureDrawable drawable;
        Mobility mobility;
        Player player;
        Collision collision;
        
        //  Entity
        int id = entities.size;
        entity = new Entity(
            id,
            flagPlayer | flagCollision | flagAvatar | flagTexture,
            new Stores[] {
                textureDrawableStore,
                mobilityStore,
                playerStore,
                collisionStore
            }
        );
        
        //  Drawable
        drawable = new TextureDrawable(main.getAtlas().findRegion("wizard"));
        drawable.setScale(4f);
        drawable.position.set(
            (Gdx.graphics.getWidth() - drawable.getTrueWidth()) * 0.5f,
            Gdx.graphics.getHeight() * 0.25f - drawable.getTrueHeight() * 0.5f
        );
        
        //  Mobility
        mobility = new Mobility();
        mobility.screenContained = true;
        mobility.screenBounds = new float[] {
            0f,
            0f,
            Gdx.graphics.getWidth() - drawable.getTrueWidth(),
            Gdx.graphics.getHeight() - drawable.getTrueHeight()
        };
        
        //  Player
        player = new Player();
        
        //  Collision
        collision = new Collision(
            drawable,
            false
        );
        
        //  Finalize
        entities.add(entity);
        textureDrawableStore.add(id, drawable);
        mobilityStore.add(id, mobility);
        playerStore.add(id, player);
        collisionStore.add(id, collision);
    }
    
    
    //  Spawn Spiders
    private void spawnSpiders()
    {
        int no = MathUtils.random(1, 3);
        for (int i = 0; i < no; i++)
        {
            //  Initialize
            Entity entity;
            TextureDrawable drawable;
            Mobility mobility;
            Collision collision;
            Spider spider;
            float posX;
            
            //  Entity
            int id = entities.size;
            entity = new Entity(
                id,
                flagSpider | flagCollision | flagAvatar | flagTexture,
                new Stores[] {
                    textureDrawableStore,
                    mobilityStore,
                    collisionStore,
                    spiderStore
                }
            );
            
            //  Drawable
            drawable = new TextureDrawable(main.getAtlas().findRegion("spider"));
            drawable.setScale(4f);
            switch (i)
            {
                case 0: posX = (Gdx.graphics.getWidth() - drawable.getTrueWidth()) * 0.5f; break;
                
                case 1: posX = Gdx.graphics.getWidth() * 0.25f - drawable.getTrueWidth() * 0.5f; break;
                
                case 2: posX = Gdx.graphics.getWidth() * 0.75f - drawable.getTrueWidth() * 0.5f; break;
            
                default: continue;
            }
            drawable.position.set(posX, Gdx.graphics.getHeight() * 0.75f - drawable.getTrueHeight() * 0.5f);
            
            //  Mobility
            mobility = new Mobility();
            mobility.setSpeed(Spider.randomSpeed());
            
            //  Collision
            collision = new Collision(
                drawable,
                false
            );
            
            //  Spider
            spider = new Spider();
            spider.destination.set(drawable.position);
            
            //  Finalize
            entities.add(entity);
            textureDrawableStore.add(id, drawable);
            mobilityStore.add(id, mobility);
            collisionStore.add(id, collision);
            spiderStore.add(id, spider);
        }
    }
    
    
    //  TODO: javadoc
    public void onPlayerHit(Entity player)
    {
        if (state.lives-- < 1)
        {
            main.getTimer().scheduleTask(
                new Task()
                {
                    @Override public void run()
                    {
                        main.goToMainMenuScreen();
                    }
                },
                1f
            );
            removeEntities(player, playerStore.get(player.getId()).hitWeb);
        }
    }
    
    
    //  TODO: javadoc
    public void onSpiderHit(Entity spider)
    {
        main.getTimer().scheduleTask(
            new Task()
            {
                @Override public void run()
                {
                    removeEntities(spider);
                }
            },
            1f
        );
        removeEntities(spiderStore.get(spider.getId()).hitSpell);
        state.points += GameplayState.POINTS_HIT_SPIDER;
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
        //  Initialize
        Entity entity;
        TextureDrawable drawable;
        Mobility mobility;
        Collision collision;
        
        //  Entity
        int id = entities.size;
        entity = new Entity(
            id,
            flagTexture | flagAvatar | flagCollision,
            new Stores[] {
                textureDrawableStore,
                mobilityStore,
                collisionStore
            }
        );
        
        //  Drawable
        drawable = new TextureDrawable(new TextureRegion(main.getAtlas().findRegion("spell")));
        drawable.setScale(2f);
        drawable.position.set(
            playerDrawable.position.x + (playerDrawable.getTrueWidth() - drawable.getTrueWidth()) * 0.5f,
            playerDrawable.position.y + (playerDrawable.getTrueHeight() - drawable.getTrueHeight()) * 0.5f
        );
        drawable.tex.flip(false, true);
        
        //  Collision
        collision = new Collision(
            drawable,
            false
        );
        
        //  Mobility
        mobility = new Mobility();
        mobility.setSpeed(1000f);
        mobility.setDirection(Vector2.Y.cpy());
        
        //  Finalize
        entities.add(entity);
        textureDrawableStore.add(id, drawable);
        mobilityStore.add(id, mobility);
        collisionStore.add(id, collision);
    }
    
    
    /**
     * Player is out of lives.
    */
    public void requestGameOver(Entity player)
    {
        main.getTimer().scheduleTask(
            new Task()
            {
                @Override public void run()
                {
                    main.goToMainMenuScreen();
                }
            },
            1f
        );
        removeEntities(player);
    }
    
    
    /**
     * A kind of bitmask flag detector.
     * @param system Entity's system
     * @param flag of the target system. Should be a power of 2
     * @return If the passed entity can be put through the system
     * based on its system flags.
     */
    private boolean isRegistered(int system, char flag)
    {
        return (system & flag) != 0b0;
    }
    
    
    /**
     * A system requests that an entity should be removed.
     * <p>
     * Entity removal is deferred.
     * @param targetEntities
     */
    public void removeEntities(Entity... targetEntities)
    {
        for (int i = 0; i < targetEntities.length; i++)
        {
            Entity debris = targetEntities[i];
            debris.debris = true;
            entityDebris.add(debris);
        }
    }
}
