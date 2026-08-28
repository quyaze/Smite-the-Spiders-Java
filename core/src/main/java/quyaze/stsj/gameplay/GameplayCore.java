package quyaze.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.EWDatastore;
import quyaze.stsj.core.ScreenContext;
import quyaze.stsj.core.Signal;
import quyaze.stsj.core.Utility;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.gameplay.architecture.Spider;
import quyaze.stsj.screens.GameplayScreen;

/**
 * A subsystem to {@link getOwner}.
 * <p></p>
 * Acts as a database by spawning the player, spiders, projectiles,
 * etc. Holds all game logic.
*/
@SuppressWarnings("unchecked")
public class GameplayCore extends ScreenContext<GameplayScreen>
{
    /*  Fields  */
    private int player;
    
    public Signal onGameOver;
    
    final static public float GAME_OVER_PHASE = 1.8f;
    
    
    /*  Constructor  */
    public GameplayCore()
    {
        onGameOver = new Signal();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        getScreen().world.onEntityReassigned.bindDeferred(
            arg -> {
                if (player == arg.oldEntity) player = arg.newEntity;
            }
        );
        
        getScreen().solver.onCollided.bindDeferred(
            arg -> {
                Projectile projectileA = getScreen().world.projectileDatastore.get(arg.thisEntity);
                
                final boolean webHitPlayer = (
                    (projectileA != null && "web".equals(projectileA.name))
                    &&
                    (player == arg.otherEntity)
                );
                
                final boolean spiderHitPlayer = (
                    getScreen().world.spiderDatastore.contains(arg.thisEntity)
                    &&
                    (player == arg.otherEntity)
                );
                
                final boolean spellHitSpider = (
                    (projectileA != null && "spell".equals(projectileA.name))
                    &&
                    (getScreen().world.spiderDatastore.contains(arg.otherEntity))
                );
                
                if (webHitPlayer) onWebHitPlayer(arg.thisEntity);
                if (spiderHitPlayer) onSpiderHitPlayer(arg.thisEntity);
                if (spellHitSpider) onSpellHitSpider(arg.thisEntity, arg.otherEntity);
            }
        );
    }
    
    
    /** On {@link GameplayScreen#show()}. */
    public void show()
    {
        spawnBackground();
        spawnPlayer();
        spawnSpiders();
    }
    
    
    /** Create the background. */
    public void spawnBackground()
    {
        Avatar avatar;
        
        GameplayWorld world = getScreen().world;
        AtlasRegion background = getGameInstance().getAtlas().findRegion("bg");
        
        avatar = new Avatar(
            background,
            Utility.getAvatarScreenScaled(background, GameplayWorld.UNITS_PER_PIXEL)
        );
        avatar.opacity = 1 / 0.2f;
        
        world.addEntity(
            (char) GameplayWorld.SYSFLAG_DRAW,
            new EWDatastore[] {
                world.avatarDatastore
            },
            avatar
        );
    }
    
    
    /** Create the player. */
    public void spawnPlayer()
    {
        Player player;
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        
        GameplayWorld world = getScreen().world;
        SmiteTheSpiders game = getGameInstance();
        
        player = new Player();
        
        avatar = new Avatar(
            game.getAtlas().findRegion("wizard"),
            4f
        );
        player.setAvatar(avatar, GameplayWorld.UNITS_PER_PIXEL);
        player.spawnPlayer(GameplayWorld.UNITS_PER_PIXEL);
        player.onCastFireball.bindDeferred(
            () -> {
                spawnFireball(avatar);
            }
        );
        
        mobility = new Mobility(0f);
        
        collision = new Collision(avatar);
        
        this.player = world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_PLAYER |
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new EWDatastore[] {
                world.playerDatastore,
                world.avatarDatastore,
                world.mobilityDatastore,
                world.collisionDatastore
            },
            player, avatar, mobility, collision
        );
    }
    
    
    /** Create the spiders. */
    public void spawnSpiders()
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
            
            GameplayWorld world = getScreen().world;
            SmiteTheSpiders game = getGameInstance();
            var viewport = getGameInstance().getViewport();
            float posX = viewport.getWorldWidth();
            
            avatar = new Avatar(
                /*  New copy so the spider Avatar can individually flip/orient to
                    horizontal movement
                */
                new TextureRegion(game.getAtlas().findRegion("spider")),
                4f
            );
            
            switch (i)
            {
                case 0: posX *= 0.5f; break;
                case 1: posX *= 0.25f; break;
                case 2: posX *= 0.75f; break;
            }
            posX -= avatar.getTrueWidth() * 0.5f;
            avatar.position.set(
                posX,
                
                /*  Spider should not spawn-clip off the screen
                */
                viewport.getWorldHeight() * 0.8f
            );
            
            mobility = new Mobility();
            
            collision = new Collision(avatar);
            
            spider = new Spider(
                avatar,
                mobility,
                GameplayWorld.UNITS_PER_PIXEL
            );
            
            world.addEntity(
                (char) (
                    GameplayWorld.SYSFLAG_AVATAR |
                    GameplayWorld.SYSFLAG_COLLISION |
                    GameplayWorld.SYSFLAG_SPIDER |
                    GameplayWorld.SYSFLAG_DRAW
                ),
                new EWDatastore[] {
                    world.avatarDatastore,
                    world.mobilityDatastore,
                    world.collisionDatastore,
                    world.spiderDatastore
                },
                avatar, mobility, collision, spider
            );
        }
    }
    
    
    /** Cast a fireball spell. */
    public void spawnFireball(Avatar playerCharacter)
    {
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        Projectile projectile;
        
        GameplayWorld world = getScreen().world;
        SmiteTheSpiders game = getGameInstance();
        TextureRegion spellTexture = new TextureRegion(game.getAtlas().findRegion("spell"));
        spellTexture.flip(false, true);
        
        avatar = new Avatar(
            spellTexture,
            2f
        );
        avatar.position.set(
            playerCharacter.position.cpy().add(playerCharacter.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(
            1000f,
            Vector2.Y.cpy()
        );
        
        collision = new Collision(avatar);
        
        projectile = new Projectile("spell");
        
        world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new EWDatastore[] {
                world.avatarDatastore,
                world.mobilityDatastore,
                world.collisionDatastore,
                world.projectileDatastore
            },
            avatar, mobility, collision, projectile
        );
    }
    
    
    /** Throw a web at the player. */
    public void spawnWeb(Avatar spiderAvatar, Avatar playerCharacter)
    {
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        Projectile projectile;
        
        GameplayWorld world = getScreen().world;
        
        avatar = new Avatar(
            getGameInstance().getAtlas().findRegion("web"),
            2f
        );
        avatar.position.set(
            spiderAvatar.position.cpy().add(spiderAvatar.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(
            1000f,
            playerCharacter.getCenter().sub(avatar.getCenter())
        );
        
        collision = new Collision(avatar);
        
        projectile = new Projectile("web");
        
        world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new EWDatastore[] {
                world.avatarDatastore,
                world.mobilityDatastore,
                world.collisionDatastore,
                world.projectileDatastore
            },
            avatar, mobility, collision, projectile
        );
    }
    
    
    /** Player is hit by a web. */
    public void onWebHitPlayer(int webEntity)
    {
        GameplayState state = getScreen().state;
        
        state.score += GameplayState.POINTS_WEB_HIT_PLAYER;
        if (--state.lives < 1)
        {
            gameOver();
            return;
        }
        
        getScreen().world.playerDatastore.get(player).spawnPlayer(GameplayWorld.UNITS_PER_PIXEL);
    }
    
    
    /** Player runs into a spider. */
    public void onSpiderHitPlayer(int spiderEntity)
    {
        GameplayState state = getScreen().state;
        
        state.score += GameplayState.POINTS_SPIDER_HIT_PLAYER;
        if (--state.lives < 1)
        {
            gameOver();
            return;
        }
        
        getScreen().world.playerDatastore.get(player).spawnPlayer(GameplayWorld.UNITS_PER_PIXEL);
    }
    
    
    /** A spider is hit by the player's spell. */
    public void onSpellHitSpider(int spellEntity, int spiderEntity)
    {
        GameplayState state = getScreen().state;
        
        state.score += GameplayState.POINTS_SPELL_HIT_SPIDER;
        getScreen().world.removeEntityRequest(spellEntity);
        getScreen().world.removeEntityRequest(spiderEntity);
    }
    
    
    /** Out of lives. */
    private void gameOver()
    {
        Timer.schedule(
            new Task() {
                @Override public void run()
                {
                    getGameInstance().toMainMenuScreen();
                }
            },
            GAME_OVER_PHASE
        );
        getScreen().world.removeEntityRequest(player);
        player = -1;
        onGameOver.fire();
    }
}
