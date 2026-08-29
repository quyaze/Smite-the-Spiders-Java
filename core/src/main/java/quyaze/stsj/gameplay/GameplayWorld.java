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
 * {@link EntityWorld} for gameplay and a subsystem to
 * {@link GameplayScreen}.
*/
public class GameplayWorld extends EntityWorld<GameplayScreen>
{
    /*  Fields  */
    private SmiteTheSpiders game;
    private GameplayScreen screen;
    
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
    final static private int DEBRIS = 3;
    
    public Event<OnEntityReassigned> onEntityReassigned;
    
    final static public float UNITS_PER_PIXEL = 1f;
    
    
    /*  Constrctor  */
    public GameplayWorld()
    {
        entityFlags = new CharArray(false, CAPACITY);
        entityDatastores = new Array<>(false, CAPACITY);
        entityDebris = new IntArray(false, DEBRIS);
        isEntityDebris = new IntSet(DEBRIS);
        
        playerDatastore = new EWDatastore<>(CAPACITY, Player.class);
        avatarDatastore = new EWDatastore<>(CAPACITY, Avatar.class);
        mobilityDatastore = new EWDatastore<>(CAPACITY, Mobility.class);
        collisionDatastore = new EWDatastore<>(CAPACITY, Collision.class);
        spiderDatastore = new EWDatastore<>(CAPACITY, Spider.class);
        projectileDatastore = new EWDatastore<>(CAPACITY, Projectile.class);
        
        playerSystem = new PlayerSystem();
        avatarSystem = new AvatarSystem();
        collisionSystem = new CollisionSystem((int) (CAPACITY * 0.9f));
        spiderSystem = new SpiderSystem();
        drawSystem = new DrawSystem();
        
        onEntityReassigned = new Event<>();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        game = getGameInstance();
        screen = getScreen();
        
        playerSystem.setWorld(this);
        avatarSystem.setWorld(this);
        collisionSystem.setWorld(this);
        spiderSystem.setWorld(this);
        drawSystem.setWorld(this);
    }
    
    
    /*  Render  */
    @Override
    public void render(float dS)
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
        
        if (!custInput()) return;
        playerSystem.render(dS); 
        spiderSystem.render(dS);
        EWSystem iterating;
        
        for (char flag = 1; flag <= SYSFLAG_DRAW; flag <<= 1)
        {
            if (screen.state.isPaused() && flag != SYSFLAG_DRAW) continue;
            
            switch (flag)
            {
                case SYSFLAG_PLAYER:    iterating = playerSystem;       break;
                case SYSFLAG_AVATAR:    iterating = avatarSystem;       break;
                case SYSFLAG_COLLISION: iterating = collisionSystem;    break;
                case SYSFLAG_SPIDER:    iterating = spiderSystem;       break;
                case SYSFLAG_DRAW:
                    iterating = drawSystem;
                    
                    SpriteBatch batch = game.getBatch();
                    ScreenViewport viewport = game.getViewport();
                    
                    ScreenUtils.clear(Color.BLACK);
                    viewport.apply(true);
                    batch.setProjectionMatrix(
                        viewport.getCamera().combined
                    );
                    batch.begin();
                    
                    break;
                default: throw new IllegalStateException("missing system");
            }
            
            for (int entity = 0; entity < entities; entity++)
            {
                if (isEntityDebris.contains(entity)) continue;
                if ((entityFlags.get(entity) & flag) != 0) iterating.iterate(entity);
            }
            
            if (flag == SYSFLAG_DRAW) game.getBatch().end();
        }
        iterating = null;
        
        /*  Deferred entity removal.
        */
        if (entityDebris.isEmpty()) return;
        entityDebris.sort();
        for (int i = entityDebris.size - 1; i >= 0; i--)
        {
            final int debris = entityDebris.get(i);
            final int last = entities - entityDebris.size + i;
            EWDatastore<?>[] dsDebris = entityDatastores.get(debris);
            EWDatastore<?>[] dsLast = entityDatastores.get(last);
            
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
    
    
    /**
     * Spawn an entity and subscribe it to the specified systems.
     * <p></p>
     * Ensure {@code data} corresponds to the {@code datastore} in the
     * correct order.
     */
    public int addEntity(char systems, EWDatastore<Object>[] datastores, Object... data)
    {
        if (datastores.length != data.length)
            throw new IllegalArgumentException("improper data to datastore assignment");
        
        for (int i = 0; i < datastores.length; i++) datastores[i].put(entities, data[i]);
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
     * Input pre-entity-loop iteration.
     * @return Input logic requires aborting the entity-system iteration
    */
    private boolean custInput()
    {
        var state = screen.state;
        
        final boolean paused = (
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ?
            state.toggleGamePaused() : state.isPaused()
        );
        
        if (paused && Gdx.input.isButtonJustPressed(0))
        {
            game.toMainMenuScreen();
            return false;
        }
        
        // if (!paused && Gdx.input.isKeyJustPressed(Input.Keys.E)) screen.core.spawnSpiders();
        
        return true;
    }
    
    
    /** On {@link GameplayScreen#hide()}. */
    public void hide()
    {
        entities = 0;
        entityFlags.clear();
        entityDatastores.clear();
        entityDebris.clear();
        isEntityDebris.clear();
        playerDatastore.clear();
        avatarDatastore.clear();
        mobilityDatastore.clear();
        collisionDatastore.clear();
        spiderDatastore.clear();
        projectileDatastore.clear();
    }
    
    
    /** On {@code GameplayScreen.resize()}. */
    public void resize(int width, int height)
    {
        collisionSystem.resize(width, height);
    }
}
