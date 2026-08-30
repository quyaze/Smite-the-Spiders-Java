package quyaze.stsj.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.architecture.Avatar;
import quyaze.stsj.core.architecture.Collision;
import quyaze.stsj.core.architecture.Mobility;
import quyaze.stsj.core.architecture.Player;
import quyaze.stsj.core.architecture.Projectile;
import quyaze.stsj.core.architecture.Spider;
import quyaze.stsj.core.template.EWDatastore;
import quyaze.stsj.core.template.ScreenContext;
import quyaze.stsj.core.utility.Signal;
import quyaze.stsj.core.utility.Utility;
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
    private SmiteTheSpiders game;
    private GameplayScreen screen;
    
    private int player;
    private boolean flagFinalSpider;
    
    public Signal onPlayerHit;
    public Signal onGameOver;
    
    final static public float GAME_OVER_PHASE = 1.8f;
    
    final static public int POINTS_SPELL_HIT_SPIDER = 50;
    final static public int POINTS_SPIDER_HIT_PLAYER = -5;
    final static public int POINTS_WEB_HIT_PLAYER = -20;
    
    final static public float PLAYER_HIT_FX_PHASE = 2f;
    final static public float PLAYER_HIT_FX_FADE = 0.05f;
    final static public float PLAYER_HIT_FX_STEP = (
        (1f - PLAYER_HIT_FX_FADE) / PLAYER_HIT_FX_PHASE * 6f
    );
    
    
    /*  Constructor  */
    public GameplayCore()
    {
        onPlayerHit = new Signal();
        onGameOver = new Signal();
    }
    
    
    /*  Create  */
    @Override
    public void create()
    {
        game = getGameInstance();
        screen = getScreen();
        
        screen.world.onEntityReassigned.bindDeferred(
            arg -> {
                if (player == arg.oldEntity) player = arg.newEntity;
            }
        );
        
        screen.solver.onCollided.bindDeferred(
            arg -> {
                Projectile projectileA = screen.world.projectileDatastore.get(arg.thisEntity);
                
                final boolean webHitPlayer = (
                    (projectileA != null && "web".equals(projectileA.name))
                    &&
                    (player == arg.otherEntity)
                );
                
                final boolean spiderHitPlayer = (
                    screen.world.spiderDatastore.contains(arg.thisEntity)
                    &&
                    (player == arg.otherEntity)
                );
                
                final boolean spellHitSpider = (
                    (projectileA != null && "spell".equals(projectileA.name))
                    &&
                    (screen.world.spiderDatastore.contains(arg.otherEntity))
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
    
    
    /** On {@link GameplayScreen#render(float).} */
    public void render(final float dS)
    {
        if (flagFinalSpider) return;
        if (screen.world.spiderDatastore.size() <= 1)
        {
            flagFinalSpider = true;
            Timer.schedule(
                    new Task()
                    {
                        @Override public void run()
                        {
                            flagFinalSpider = false;
                            spawnSpiders();
                        }
                    },
                    MathUtils.random(0.2f, 1.8f)
                );
        }
    }
    
    
    /** Create the background. */
    private void spawnBackground()
    {
        Avatar avatar;
        
        GameplayWorld world = screen.world;
        AtlasRegion background = game.getAtlas().findRegion("bg");
        
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
    private void spawnPlayer()
    {
        Player player;
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        
        GameplayWorld world = screen.world;
        
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
            
            GameplayWorld world = screen.world;
            var viewport = game.getViewport();
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
            spider.onThrowWeb.bindDeferred(
                () -> {
                    spawnWeb(
                        avatar,
                        world.avatarDatastore.get(player)
                    );
                }
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
    private void spawnFireball(Avatar playerCharacter)
    {
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        Projectile projectile;
        
        GameplayWorld world = screen.world;
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
    private void spawnWeb(Avatar spiderAvatar, Avatar playerCharacter)
    {
        if (player == -1) return;
        
        Avatar avatar;
        Mobility mobility;
        Collision collision;
        Projectile projectile;
        
        GameplayWorld world = screen.world;
        
        avatar = new Avatar(
            game.getAtlas().findRegion("web"),
            2f
        );
        avatar.position.set(
            spiderAvatar.position.cpy().add(spiderAvatar.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(
            MathUtils.random(600, 800),
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
    private void onWebHitPlayer(int webEntity)
    {
        GameplayState state = screen.state;
        
        state.score += POINTS_WEB_HIT_PLAYER;
        screen.world.removeEntityRequest(webEntity);
        playerHit();
    }
    
    
    /** Player runs into a spider. */
    private void onSpiderHitPlayer(int spiderEntity)
    {
        GameplayState state = screen.state;
        
        state.score += POINTS_SPIDER_HIT_PLAYER;
        playerHit();
    }
    
    
    /** A spider is hit by the player's spell. */
    private void onSpellHitSpider(int spellEntity, int spiderEntity)
    {
        GameplayWorld world = screen.world;
        GameplayState state = screen.state;
        
        state.score += POINTS_SPELL_HIT_SPIDER;
        world.removeEntityRequest(spellEntity);
        world.removeEntityRequest(spiderEntity);
    }
    
    
    /** Player is hit. */
    private void playerHit()
    {
        if (--screen.state.lives < 1)
        {
            gameOver();
            return;
        }
        
        GameplayWorld world = screen.world;
        Collision playerCollision = world.collisionDatastore.get(player);
        
        Timer.schedule(
            new Task()
            {
                @Override public void run()
                {
                    playerCollision.skipSolving = false;
                }
            },
            PLAYER_HIT_FX_PHASE
        );
        
        world.playerDatastore.get(player).spawnPlayer(GameplayWorld.UNITS_PER_PIXEL);
        playerCollision.skipSolving = true;
        onPlayerHit.fire();
    }
    
    
    /** Out of lives. */
    private void gameOver()
    {
        Timer.schedule(
            new Task() {
                @Override public void run()
                {
                    game.toMainMenuScreen();
                }
            },
            GAME_OVER_PHASE
        );
        screen.world.removeEntityRequest(player);
        player = -1;
        onGameOver.fire();
    }
}
