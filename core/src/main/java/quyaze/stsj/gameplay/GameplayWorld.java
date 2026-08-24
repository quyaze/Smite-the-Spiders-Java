package quyaze.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.EWDatastore;
import quyaze.stsj.core.EWSystem;
import quyaze.stsj.core.EntityWorld;
import quyaze.stsj.core.Event;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.gameplay.architecture.Spider;
import quyaze.stsj.gameplay.eventDefs.OnEntityReassigned;
import quyaze.stsj.gameplay.systems.AvatarSystem;
import quyaze.stsj.gameplay.systems.CollisionSystem;
import quyaze.stsj.gameplay.systems.DrawSystem;
import quyaze.stsj.gameplay.systems.PlayerSystem;
import quyaze.stsj.gameplay.systems.SpiderSystem;
import quyaze.stsj.screens.GameplayScreen;

/**
 * {@link EntityWorld} for gameplay and a static subsystem to
 * {@link GameplayScreen}.
*/
public class GameplayWorld extends EntityWorld
{
    /*  Fields  */
    private CharArray entityFlags;
    private Array<EWDatastore<?>[]> entityDatastores;
    private IntArray entityDebris;
    private IntSet isEntityDebris;
    
    public EWDatastore<Player> playerDatastore;
    public EWDatastore<Avatar> avatarDatastore;
    public EWDatastore<Mobility> mobilityDatastore;
    public EWDatastore<Collision> collisionDatastore;
    public EWDatastore<Spider> spiderDatastore;
    public EWDatastore<Projectile> projectileDatastore;
    
    private PlayerSystem playerSystem;
    private AvatarSystem avatarSystem;
    private CollisionSystem collisionSystem;
    private SpiderSystem spiderSystem;
    private DrawSystem drawSystem;
    
    final static public char SYSFLAG_PLAYER = 1;        //      1 = 1 << 0
    final static public char SYSFLAG_AVATAR = 2;        //     10 = 1 << 1
    final static public char SYSFLAG_COLLISION = 4;     //    100 = 1 << 2
    final static public char SYSFLAG_SPIDER = 8;        //   1000 = 1 << 3
    final static public char SYSFLAG_DRAW = 16;         //  10000 = 1 << 4
    
    final static private int CAPACITY = 64;
    final static private float SOLVE_INTERVAL = 1 / 24f;
    private Task collisionSolveTask;
    private EWSystem iteratingSystem;
    private float timeToNextSolve;
    
    public Event<OnEntityReassigned> onEntityReassigned;
    
    
    /*  Constructor  */
    public GameplayWorld()
    {
        entityFlags = new CharArray(false, CAPACITY);
        entityDatastores = new Array<>(false, CAPACITY);
        entityDebris = new IntArray(false, 2);
        isEntityDebris = new IntSet(2);
        
        playerDatastore = new EWDatastore<>(CAPACITY);
        avatarDatastore = new EWDatastore<>(CAPACITY);
        mobilityDatastore = new EWDatastore<>(CAPACITY);
        collisionDatastore = new EWDatastore<>(CAPACITY);
        spiderDatastore = new EWDatastore<>(CAPACITY);
        projectileDatastore = new EWDatastore<>(CAPACITY);
        
        playerSystem = new PlayerSystem();
        avatarSystem = new AvatarSystem();
        collisionSystem = new CollisionSystem((int) (CAPACITY * 0.9f));
        spiderSystem = new SpiderSystem();
        drawSystem = new DrawSystem();
        
        collisionSolveTask = new Task()
        {
            @Override public void run()
            {
                GameplayScreen.solver.solve();
            }
        };
        
        onEntityReassigned = new Event<>();
    }
    
    
    /*  Render  */
    @Override
    public void render(float delta)
    {
        GameplayCore core = GameplayScreen.core;
        GameplayState state = GameplayScreen.state;
        
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
            SmiteTheSpiders.gameInstance().goToMainMenuScreen();
            return;
        }
        
        if (!paused && Gdx.input.isKeyJustPressed(Input.Keys.E)) core.spawnSpiders();
        
        for (char flag = 1; flag <= SYSFLAG_DRAW; flag <<= 1)
        {
            if (paused && flag != SYSFLAG_DRAW) continue;
            
            switch (flag)
            {
                case SYSFLAG_PLAYER:    iteratingSystem = playerSystem;     break;
                case SYSFLAG_AVATAR:    iteratingSystem = avatarSystem;     break;
                case SYSFLAG_COLLISION: iteratingSystem = collisionSystem;  break;
                case SYSFLAG_SPIDER:    iteratingSystem = spiderSystem;     break;
                case SYSFLAG_DRAW:
                    iteratingSystem = drawSystem;
                    
                    SpriteBatch batch = SmiteTheSpiders.getBatch();
                    ScreenViewport viewport = SmiteTheSpiders.getViewport();
                    
                    ScreenUtils.clear(Color.BLACK);
                    viewport.apply(true);
                    batch.setProjectionMatrix(
                        SmiteTheSpiders.getViewport().getCamera().combined
                    );
                    batch.begin();
                    
                    break;
            }
            
            for (int entity = 0; entity < entities; entity++)
            {
                if (isEntityDebris.contains(entity)) continue;
                if ((entityFlags.get(entity) & flag) != 0) iteratingSystem.iterate(entity);
            }
            
            if (flag == SYSFLAG_DRAW)
            {
                SmiteTheSpiders.getBatch().end();
                // if (paused) pauseOverlay();
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
            final EWDatastore<?>[] dsDebris = entityDatastores.get(debris);
            final EWDatastore<?>[] dsLast = entityDatastores.get(last);
            
            for (int j = 0; j < dsDebris.length; j++) dsDebris[j].remove(debris);
            if (debris != last) for (int j = 0; j < dsLast.length; j++) dsLast[j].transfer(last, debris);
            entityFlags.removeIndex(debris);
            entityDatastores.removeIndex(debris);
            onEntityReassigned.fire(
                new OnEntityReassigned(last, debris)
            );
        }
        entities -= entityDebris.size;
        entityDebris.clear();
        isEntityDebris.clear();
    }
    
    
    /** Post-construct. */
    public void postConstruct()
    {
        collisionSystem.postConstruct();
    }
    
    
    /**
     * Spawn an entity and subscribe it to the specified systems.
     * <p></p>
     * Data is being added to the datastores blindly. Ensure
     * {@code data} corresponds to the {@code datastore} in the
     * correct order.
     */
    @SuppressWarnings("unchecked")
    public int addEntity(char systems, EWDatastore<?>[] datastores, Object... data)
    {
        if (datastores.length != data.length) throw new IllegalArgumentException("improper data to datastore assignment");
        for (int i = 0; i < datastores.length; i++)
        {
            ((EWDatastore<Object>) datastores[i]).put(entities, data[i]);
        }
        entityDatastores.add(datastores);
        entityFlags.add(systems);
        return entities++;
    }
    
    
    /** Mark an entity for deferred removal. */
    public void removeEntityRequest(int entity)
    {
        if (isEntityDebris.add(entity)) entityDebris.add(entity);
    }
    
    
    /**
     * On {@code GameplayScreen.show()}
     * <p></p>
     * Starts the SmiteTheSpiders.
     */
    public void show()
    {
        GameplayScreen.state.setGamePaused(false);
        GameplayScreen.core.spawnBackground();
        GameplayScreen.core.spawnPlayer();
        GameplayScreen.core.spawnSpiders();
    }
    
    
    /** On {@link GameplayScreen#hide()}. */
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
        GameplayScreen.state.setGamePaused(true);
        setSolverEnabled(false);
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
            although it is highly unnoticeable since the solver is frame-
            scheduled.
        */
        
        if (enabled == collisionSolveTask.isScheduled()) return;
        if (enabled)
        {
            SmiteTheSpiders.getTimer().scheduleTask(
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
