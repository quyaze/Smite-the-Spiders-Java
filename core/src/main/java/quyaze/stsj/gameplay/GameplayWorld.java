package quyaze.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.EWDatastore;
import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.gameplay.architecture.Spider;
import quyaze.stsj.gameplay.systems.AvatarSystem;
import quyaze.stsj.gameplay.systems.CollisionSystem;
import quyaze.stsj.screens.GameplayScreen;

public class GameplayWorld extends EntityWorld
{
    //  Fields
    final private GameMaster gameMaster;
    
    final private CharArray entityFlags;
    final private Array<EWDatastore<?>[]> entityDatastores;
    final private IntArray entityDebris;
    final private IntSet isEntityDebris;
    
    final public EWDatastore<Player> playerDatastore;
    final public EWDatastore<Avatar> avatarDatastore;
    final public EWDatastore<Mobility> mobilityDatastore;
    final public EWDatastore<Collision> collisionDatastore;
    final public EWDatastore<Spider> spiderDatastore;
    final public EWDatastore<Projectile> projectileDatastore;
    
    final private AvatarSystem avatarSystem;
    final private CollisionSystem collisionSystem;
    
    final static private char flagPlayer = 1;       //      1 = 1 << 0
    final static private char flagCollision = 2;    //     10 = 1 << 1
    final static private char flagAvatar = 4;       //    100 = 1 << 2
    final static private char flagSpider = 8;       //   1000 = 1 << 3
    final static private char flagTexture = 16;     //  10000 = 1 << 4
    
    final static private int capacity = 64;
    final static private float solveInterval = 1 / 30f;
    private boolean gamePaused;
    private EWSystem iteratingSystem;
    private Task collisionSolveTask;
    private float timeToNextSolve;
    
    
    //  Constructor
    public GameplayWorld(GameplayScreen owner, GameMaster gameMaster)
    {
        super(owner);
        this.gameMaster = gameMaster;
        entityFlags = new CharArray(false, capacity);
        entityDatastores = new Array<>(false, capacity);
        entityDebris = new IntArray(false, capacity);
        isEntityDebris = new IntSet(capacity);
        
        playerDatastore = new EWDatastore<>(capacity);
        avatarDatastore = new EWDatastore<>(capacity);
        mobilityDatastore = new EWDatastore<>(capacity);
        collisionDatastore = new EWDatastore<>(capacity);
        spiderDatastore = new EWDatastore<>(capacity);
        projectileDatastore = new EWDatastore<>(capacity);
        
        avatarSystem = new AvatarSystem(this);
        collisionSystem = new CollisionSystem(this, capacity);
        
        collisionSolveTask = new Task()
        {
            @Override public void run()
            {
                collisionSystem.solve();
            }
        };
    }
    
    
    //  Render
    @Override
    public void render()
    {
        /*  First see if pausing or exiting to the main menu.
            
            Every call to this function, EWSystems each perform a pass in
            order. These systems can interact or manipulate entities for
            gameplay aspects.
            
            Each system pass, all entities are iterated. If they subscribe
            to a system, they are sent through. Subscription is determined
            within the entity's bitmask flag.
            
            Entitiy removal is deferred. Systems can mark enities as
            "debris" and are then removed at the very end of render().
        */
       
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) setGamePaused(!gamePaused);
        if (gamePaused && Gdx.input.isButtonJustPressed(0))
        {
            gameMaster.goToMainMenuScreen();
            return;
        }
        
        if (!gamePaused && Gdx.input.isKeyJustPressed(Input.Keys.Q))
        {}
        
        for (char flag = 1; flag <= flagTexture; flag <<= 1)
        {
            if (gamePaused && flag != flagTexture) continue;
            
            switch (flag)
            {
                case flagPlayer: break;
                
                case flagCollision: iteratingSystem = collisionSystem; break;
                
                case flagAvatar: iteratingSystem = avatarSystem; break;
                
                case flagSpider: break;
                
                case flagTexture:
                    ScreenUtils.clear(Color.BLACK);
                    gameMaster.getViewport().apply();
                    gameMaster.getBatch().setProjectionMatrix(
                        gameMaster.getViewport().getCamera().combined
                    );
                    gameMaster.getBatch().begin();
                    break;
            }
            
            for (int entity = 0; entity < entities; entity++)
            {
                if (isEntityDebris.contains(entity)) continue;
                if ((entityFlags.get(entity) & flag) != 0) iteratingSystem.iterate(entity);
            }
            
            if (flag == flagTexture) gameMaster.getBatch().end();
        }
        iteratingSystem = null;
        
        /*  Deferred entity removal.
        */
        for (int debris = 0; debris < entityDebris.size; debris++) removeEntity(debris);
    }
    
    
    /**
     * Spawn an entity and subscribe it to the specified systems.
     * <p></p>
     * Data is being added to the datastores blindly. Ensure
     * {@code data} corresponds to the {@code datastore} in the
     * correct order.
     */
    private void addEntity(char systems, EWDatastore<Object>[] datastores, Object... data)
    {
        for (int i = 0; i < datastores.length; i++) datastores[i].add(i, data);
        entityDatastores.add(datastores);
        entityFlags.add(systems);
        entities++;
    }
    
    
    /** Remove an entity. */
    private void removeEntity(int entity)
    {
        EWDatastore<?>[] datastores = entityDatastores.get(entity);
        
        entityFlags.removeIndex(entity);
        for (int i = 0 ; i < datastores.length; i++) datastores[i].remove(entity);
        entityDatastores.removeIndex(entity);
        entityDebris.removeIndex(entity);
        isEntityDebris.remove(entity);
        entities--;
    }
    
    
    /** Mark an entity for deferred removal. */
    public void removeEntityRequest(int entity)
    {
        entityDebris.add(entity);
        isEntityDebris.add(entity);
    }
    
    
    /** Set game paused. */
    public void setGamePaused(boolean paused)
    {
        if (gamePaused == paused) return;
        else if (paused)
        {
            timeToNextSolve = collisionSolveTask.getExecuteTimeMillis() * 0.001f;
            collisionSolveTask.cancel();
        }
        else
        {
            gameMaster.getTimer().scheduleTask(
                collisionSolveTask,
                timeToNextSolve,
                solveInterval,
                -1
            );
            timeToNextSolve = 0f;
        }
        gamePaused = paused;
    }
    
    
    /**
     * On {@code GameplayScreen.show()}
     * <p></p>
     * Starts the game.
     */
    public void show()
    {
        gameMaster.getTimer().scheduleTask(
            collisionSolveTask,
            0f,
            solveInterval,
            -1
        );
        spawnBackground();
        spawnPlayer();
        spawnSpiders();
    }
    
    
    /**
     * On {@code GameplayScreen.hide()}
     * <p></p>
     * Exits and clears the game.
     */
    public void hide()
    {
        entities = 0;
        entityFlags.clear();
        entityDebris.clear();
        isEntityDebris.clear(capacity);
        entityDatastores.clear();
        playerDatastore.clear();
        avatarDatastore.clear();
        mobilityDatastore.clear();
        collisionDatastore.clear();
        spiderDatastore.clear();
        projectileDatastore.clear();
        gamePaused = false;
        collisionSolveTask.cancel();
    }
    
    
    /** Create the background. */
    @SuppressWarnings("unchecked")
    private void spawnBackground()
    {
        final Avatar avatar;
        
        final AtlasRegion background = gameMaster.getAtlas().findRegion("bg");
        final int widthScreen = Gdx.graphics.getWidth();
        final int heightScreen = Gdx.graphics.getHeight();
        final int widthBackground = background.getRegionWidth();
        final int heightBackground = background.getRegionHeight();
        
        avatar = new Avatar(
            background,
            widthScreen / heightScreen > widthBackground / heightBackground ?
            widthScreen / (float) widthBackground :
            heightScreen / (float) heightBackground
        );
        
        addEntity(
            flagAvatar,
            new EWDatastore[] {
                avatarDatastore
            },
            avatar
        );
    }
    
    
    /** Create the player. */
    @SuppressWarnings("unchecked")
    private void spawnPlayer()
    {
        final Player player;
        final Avatar avatar;
        final Mobility mobility;
        final Collision collision;
        
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        final Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
        
        player = new Player();
        
        avatar = new Avatar(
            gameMaster.getAtlas().findRegion("wizard"),
            4f
        );
        avatar.position.set(center.sub(avatar.getTrueSize()));
        
        mobility = new Mobility();
        
        collision = new Collision(avatar)
        {
            @Override public void onCollided(int entity, Collision collider)
            {
                final Projectile projectile = projectileDatastore.get(entity);
                final boolean isSpider = spiderDatastore.contains(entity);
                final boolean isWeb = projectile != null && "web".equals(projectile.name);
                
                if (!(isSpider || isWeb)) return;
                if (isWeb) removeEntityRequest(entity);
                onPlayerHit();
            }
        };
        
        addEntity(
            (char) (flagPlayer | flagAvatar | flagCollision | flagTexture),
            new EWDatastore[] {
                playerDatastore,
                avatarDatastore,
                mobilityDatastore,
                collisionDatastore
            },
            player, avatar, mobility, collision
        );
    }
    
    
    /** Create the spiders. */
    @SuppressWarnings("unchecked")
    private void spawnSpiders()
    {
        for (
            int i = 0, n = MathUtils.random(1, 3);
            i < n;
            i++
        )
        {
            Avatar avatar;
            Mobility mobility;
            Collision collision;
            Spider spider;
            
            float posX;
            
            avatar = new Avatar(
                gameMaster.getAtlas().findRegion("spider"),
                4f
            );
            
            mobility = new Mobility(Spider.randomSpeed());
            
            collision = new Collision(avatar)
            {
                @Override public void onCollided(int entity, Collision collider)
                {
                    if (playerDatastore.contains(entity)) onPlayerHit();
                }
            };
            
            spider = new Spider();
            
            addEntity(
                (char) (flagAvatar | flagCollision | flagSpider | flagTexture),
                new EWDatastore[] {
                    avatarDatastore,
                    mobilityDatastore,
                    collisionDatastore,
                    spiderDatastore
                },
                avatar, mobility, collision, spider
            );
        }
    }
    
    
    /** Cast a fireball spell. */
    @SuppressWarnings("unchecked")
    private void spawnFireball()
    {}
    
    
    /** Player is hit by a web or spider. */
    private void onPlayerHit()
    {}
    
    
    /** A spider is hit by the player's spell. */
    private void onSpiderHit()
    {}
}
