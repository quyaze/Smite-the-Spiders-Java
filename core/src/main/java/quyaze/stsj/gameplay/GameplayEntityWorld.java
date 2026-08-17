package quyaze.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.DatastoreForEW;
import quyaze.stsj.core.SystemForEW;
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

public class GameplayEntityWorld extends EntityWorld
{
    //  Fields
    final private GameMaster gameMaster;
    final private GameplayCore core;
    
    final private CharArray entityFlags;
    final private Array<DatastoreForEW<?>[]> entityDatastores;
    final private IntArray entityDebris;
    final private IntSet isEntityDebris;
    
    final public DatastoreForEW<Player> playerDatastore;
    final public DatastoreForEW<Avatar> avatarDatastore;
    final public DatastoreForEW<Mobility> mobilityDatastore;
    final public DatastoreForEW<Collision> collisionDatastore;
    final public DatastoreForEW<Spider> spiderDatastore;
    final public DatastoreForEW<Projectile> projectileDatastore;
    
    final private AvatarSystem avatarSystem;
    final private CollisionSystem collisionSystem;
    
    final static public char flagPlayer = 1;        //      1 = 1 << 0
    final static public char flagAvatar = 2;        //     10 = 1 << 1
    final static public char flagCollision = 4;     //    100 = 1 << 2
    final static public char flagSpider = 8;        //   1000 = 1 << 3
    final static public char flagTexture = 16;      //  10000 = 1 << 4
    
    final static private int capacity = 64;
    final static private float solveInterval = 1 / 30f;
    private SystemForEW iteratingSystem;
    private Task collisionSolveTask;
    private float timeToNextSolve;
    
    
    //  Constructor
    public GameplayEntityWorld(GameplayScreen owner, GameMaster gameMaster, GameplayCore core)
    {
        super(owner);
        this.gameMaster = gameMaster;
        this.core = core;
        entityFlags = new CharArray(false, capacity);
        entityDatastores = new Array<>(false, capacity);
        entityDebris = new IntArray(false, capacity);
        isEntityDebris = new IntSet(capacity);
        
        playerDatastore = new DatastoreForEW<>(capacity);
        avatarDatastore = new DatastoreForEW<>(capacity);
        mobilityDatastore = new DatastoreForEW<>(capacity);
        collisionDatastore = new DatastoreForEW<>(capacity);
        spiderDatastore = new DatastoreForEW<>(capacity);
        projectileDatastore = new DatastoreForEW<>(capacity);
        
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
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) core.togglePaused();
        
        final boolean paused = core.getPaused();
        if (paused && Gdx.input.isButtonJustPressed(0))
        {
            gameMaster.goToMainMenuScreen();
            return;
        }
        
        if (!paused && Gdx.input.isKeyJustPressed(Input.Keys.Q))
        {}
        
        for (char flag = 1; flag <= flagTexture; flag <<= 1)
        {
            if (paused && flag != flagTexture) continue;
            
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
        for (int debris = 0; debris < entityDebris.size; debris++)
        {
            DatastoreForEW<?>[] datastores = entityDatastores.get(debris);
            
            entityFlags.removeIndex(debris);
            for (int i = 0 ; i < datastores.length; i++) datastores[i].remove(debris);
            entityDatastores.removeIndex(debris);
            entityDebris.removeIndex(debris);
            isEntityDebris.remove(debris);
            entities--;
        }
    }
    
    
    /**
     * Spawn an entity and subscribe it to the specified systems.
     * <p></p>
     * Data is being added to the datastores blindly. Ensure
     * {@code data} corresponds to the {@code datastore} in the
     * correct order.
     */
    public void addEntity(char systems, DatastoreForEW<Object>[] datastores, Object... data)
    {
        for (int i = 0; i < datastores.length; i++) datastores[i].add(i, data);
        entityDatastores.add(datastores);
        entityFlags.add(systems);
        entities++;
    }
    
    
    /** Mark an entity for deferred removal. */
    public void removeEntityRequest(int entity)
    {
        entityDebris.add(entity);
        isEntityDebris.add(entity);
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
        core.spawnBackground();
        core.spawnPlayer();
        core.spawnSpiders();
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
        core.setPaused(false);
        collisionSolveTask.cancel();
    }
}
