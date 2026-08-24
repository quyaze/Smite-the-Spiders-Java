package quyaze.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.EWDatastore;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.gameplay.architecture.Spider;
import quyaze.stsj.screens.GameplayScreen;

/**
 * A static subsystem to {@link GameplayScreen}.
 * <p></p>
 * Acts as a database by spawning the player, spiders, projectiles,
 * etc.
*/
public class GameplayCore
{
    /*  Fields  */
    private int player;
    
    
    /*  Constructor  */
    public GameplayCore()
    {
        GameplayScreen.world.onEntityReassigned.bindDeferred(
            arg -> {
                if (player == arg.oldEntity)
                player = arg.newEntity;
            }
        );
    }
    
    
    /** Create the background. */
    public void spawnBackground()
    {
        Avatar avatar;
        
        GameplayWorld world = GameplayScreen.world;
        AtlasRegion background = SmiteTheSpiders.getAtlas().findRegion("bg");
        
        avatar = new Avatar(
            background,
            SmiteTheSpiders.getUtility().getAvatarScreenScaled(background)
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
        
        GameplayWorld world = GameplayScreen.world;
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
        
        player = new Player();
        
        avatar = new Avatar(
            SmiteTheSpiders.getAtlas().findRegion("wizard"),
            4f
        );
        center.sub(avatar.getTrueSize().scl(0.5f));
        avatar.position.set(center);
        player.setAvatar(avatar);
        
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
            
            GameplayWorld world = GameplayScreen.world;
            float posX = Gdx.graphics.getWidth();
            
            avatar = new Avatar(
                /*  New copy so the spider Avatar can individually flip/orient to
                    horizontal movement
                */
                new TextureRegion(SmiteTheSpiders.getAtlas().findRegion("spider")),
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
                Gdx.graphics.getHeight() * 0.8f
            );
            
            mobility = new Mobility();
            
            collision = new Collision(avatar);
            
            spider = new Spider(avatar, mobility);
            
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
        
        GameplayWorld world = GameplayScreen.world;
        TextureRegion spellTexture = new TextureRegion(SmiteTheSpiders.getAtlas().findRegion("spell"));
        spellTexture.flip(false, true);
        
        avatar = new Avatar(
            spellTexture,
            2f
        );
        avatar.position.set(
            playerCharacter.position.cpy().add(playerCharacter.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(1000f, Vector2.Y.cpy());
        
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
        
        GameplayWorld world = GameplayScreen.world;
        
        avatar = new Avatar(
            SmiteTheSpiders.getAtlas().findRegion("web"),
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
    public void onWebHitPlayer(int webEntity, int playerEntity)
    {
        final GameplayState state = GameplayScreen.state;
        
        state.score += GameplayState.POINTS_WEB_HIT_PLAYER;
        if (state.lives-- < 0) GameplayScreen.state.onGameOver.fire();
    }
    
    
    /** Player is hit by a spider. */
    public void onSpiderHitPlayer(int spiderEntity, int playerEntity)
    {
        final GameplayState state = GameplayScreen.state;
        
        state.score += GameplayState.POINTS_SPIDER_HIT_PLAYER;
        if (state.lives-- < 0) GameplayScreen.state.onGameOver.fire();
    }
    
    
    /** A spider is hit by the player's spell. */
    public void onSpellHitSpider(int spellEntity, int spiderEntity)
    {
        final GameplayState state = GameplayScreen.state;
        
        state.score += GameplayState.POINTS_SPELL_HIT_SPIDER;
        GameplayScreen.world.removeEntityRequest(spellEntity);
        GameplayScreen.world.removeEntityRequest(spiderEntity);
    }
}
