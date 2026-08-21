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
import quyaze.stsj.gameplay.systems.DrawSystem;
import quyaze.stsj.gameplay.systems.PlayerSystem;
import quyaze.stsj.gameplay.systems.SpiderSystem;
import quyaze.stsj.screens.GameplayScreen;

final public class GameplayWorld extends EntityWorld
{
    //  Fields
    final private GameMaster gameMaster;
    final private GameplayCore core;
    final private GameplayState state;
    
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
    
    final private PlayerSystem playerSystem;
    final private AvatarSystem avatarSystem;
    final private CollisionSystem collisionSystem;
    final private SpiderSystem spiderSystem;
    final private DrawSystem drawSystem;
    
    final static public char SYSFLAG_PLAYER = 1;     //      1 = 1 << 0
    final static public char SYSFLAG_AVATAR = 2;     //     10 = 1 << 1
    final static public char SYSFLAG_COLLISION = 4;  //    100 = 1 << 2
    final static public char SYSFLAG_SPIDER = 8;     //   1000 = 1 << 3
    final static public char SYSFLAG_DRAW = 16;      //  10000 = 1 << 4
    
    final static private int CAPACITY = 64;
    final static private float SOLVE_INTERVAL = 1 / 24f;
    final private Task collisionSolveTask;
    private SystemForEW iteratingSystem;
    private float timeToNextSolve;
    
    
    //  Constructor
    public GameplayWorld(GameMaster gameMaster, GameplayScreen owner)
    {
        super(gameMaster, owner);
        this.gameMaster = gameMaster;
        this.core = new GameplayCore(gameMaster, this)
        {
            @Override public void requestGameOver() {}
        };
        this.state = new GameplayState()
        {
            @Override public void onPausedStateChanged(boolean paused)
            {
                setSolverEnabled(!paused);
            }
        };
        
        entityFlags = new CharArray(false, CAPACITY);
        entityDatastores = new Array<>(false, CAPACITY);
        entityDebris = new IntArray(false, 2);
        isEntityDebris = new IntSet(2);
        
        playerDatastore = new DatastoreForEW<>(CAPACITY);
        avatarDatastore = new DatastoreForEW<>(CAPACITY);
        mobilityDatastore = new DatastoreForEW<>(CAPACITY);
        collisionDatastore = new DatastoreForEW<>(CAPACITY);
        spiderDatastore = new DatastoreForEW<>(CAPACITY);
        projectileDatastore = new DatastoreForEW<>(CAPACITY);
        
        playerSystem = new PlayerSystem(this);
        avatarSystem = new AvatarSystem(this);
        collisionSystem = new CollisionSystem(this, (int) (CAPACITY * 0.8f));
        spiderSystem = new SpiderSystem(this);
        drawSystem = new DrawSystem(this, gameMaster.getBatch());
        
        collisionSolveTask = new Task()
        {
            @Override public void run()
            {
                collisionSystem.getSolver().solve();
            }
        };
    }
    
    
    //  Render
    @Override
    public void render(float delta)
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
        
        final boolean paused = (
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ?
            state.toggleGamePaused() : state.isPaused()
        );
        
        if (paused && Gdx.input.isButtonJustPressed(0))
        {
            gameMaster.goToMainMenuScreen();
            return;
        }
        
        if (!paused && Gdx.input.isKeyJustPressed(Input.Keys.E)) core.spawnSpiders();
        
        for (char flag = 1; flag <= SYSFLAG_DRAW; flag <<= 1)
        {
            if (paused && flag != SYSFLAG_DRAW) continue;
            
            switch (flag)
            {
                case SYSFLAG_PLAYER: iteratingSystem = playerSystem; break;
                
                case SYSFLAG_AVATAR: iteratingSystem = avatarSystem; break;
                
                case SYSFLAG_COLLISION: iteratingSystem = collisionSystem; break;
                
                case SYSFLAG_SPIDER: iteratingSystem = spiderSystem; break;
                
                case SYSFLAG_DRAW:
                    iteratingSystem = drawSystem;
                    
                    ScreenUtils.clear(Color.BLACK);
                    gameMaster.getViewport().apply(true);
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
            
            if (flag == SYSFLAG_DRAW)
            {
                gameMaster.getBatch().end();
            }
        }
        iteratingSystem = null;
        
        /*  Deferred entity removal.
        */
        if (entityDebris.isEmpty()) return;
        entityDebris.sort();
        for (int i = entityDebris.size - 1; i >= 0; i--)
        {
            final int debris = entityDebris.get(i);
            final int last = entities - entityDebris.size + i;
            final DatastoreForEW<?>[] dsDebris = entityDatastores.get(debris);
            final DatastoreForEW<?>[] dsLast = entityDatastores.get(last);
            
            for (int j = 0; j < dsDebris.length; j++) dsDebris[j].remove(debris);
            if (debris != last) for (int j = 0; j < dsLast.length; j++) dsLast[j].transfer(last, debris);
            entityFlags.removeIndex(debris);
            entityDatastores.removeIndex(debris);
        }
        entities -= entityDebris.size;
        entityDebris.clear();
        isEntityDebris.clear();
    }
    
    
    /**
     * @return Gameplay core object
     */
    public GameplayCore getCore()
    {
        return core;
    }
    
    
    /**
     * @return Gameplay state object
     */
    public GameplayState getState()
    {
        return state;
    }
    
    
    /**
     * Spawn an entity and subscribe it to the specified systems.
     * <p></p>
     * Data is being added to the datastores blindly. Ensure
     * {@code data} corresponds to the {@code datastore} in the
     * correct order.
     */
    @SuppressWarnings("unchecked")
    public void addEntity(char systems, DatastoreForEW<?>[] datastores, Object... data)
    {
        if (datastores.length != data.length) return;
        for (int i = 0; i < datastores.length; i++)
        {
            ((DatastoreForEW<Object>) datastores[i]).put(entities, data[i]);
        }
        entityDatastores.add(datastores);
        entityFlags.add(systems);
        entities++;
    }
    
    
    /** Mark an entity for deferred removal. */
    public void removeEntityRequest(int entity)
    {
        if (isEntityDebris.add(entity)) entityDebris.add(entity);
    }
    
    
    /**
     * On {@code GameplayScreen.show()}
     * <p></p>
     * Starts the game.
     */
    public void show()
    {
        state.setGamePaused(false);
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
        isEntityDebris.clear();
        entityDatastores.clear();
        playerDatastore.clear();
        avatarDatastore.clear();
        mobilityDatastore.clear();
        collisionDatastore.clear();
        spiderDatastore.clear();
        projectileDatastore.clear();
        state.setGamePaused(true);
        setSolverEnabled(false);
    }
    
    
    /**
     * On {@code GameplayScreen.pause()}
     * <p></p>
     * Executes pause functionality.
     */
    public void pause()
    {
        state.setGamePaused(true);
    }
    
    
    /** On {@code GameplayScreen.resize()}. */
    public void resize(int width, int height)
    {
        collisionSystem.resize(width, height);
    }
    
    
    /** Enable to disable the solver. */
    private void setSolverEnabled(boolean enabled)
    {
        /*  Some functionality below makes the solver appear to freeze when
            the player is pausing during gameplay. When unpausing, the
            solver resumes not with the full interval delay, but rather, the
            time after when it was supposed to run next. A small detail
            although it is highly unnoticeable since the solver waits for a
            frame.
        */
        
        if (enabled == collisionSolveTask.isScheduled()) return;
        if (enabled)
        {
            gameMaster.getTimer().scheduleTask(
                collisionSolveTask,
                timeToNextSolve,
                SOLVE_INTERVAL
            );
        }
        else
        {
            final long then = collisionSolveTask.getExecuteTimeMillis();
            final long now = System.nanoTime() / 1000000;
            timeToNextSolve = (then - now) * 0.001f;
            collisionSolveTask.cancel();
        }
    }
}
