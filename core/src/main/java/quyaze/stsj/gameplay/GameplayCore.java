package quyaze.stsj.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.DatastoreForEW;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Collision;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.gameplay.architecture.Player;
import quyaze.stsj.gameplay.architecture.Projectile;
import quyaze.stsj.gameplay.architecture.Spider;

public abstract class GameplayCore
{
    //  Fields
    final private GameMaster gameMaster;
    final private GameplayWorld world;
    
    
    //  Constructor
    
    public GameplayCore(GameMaster gameMaster, GameplayWorld world)
    {
        this.gameMaster = gameMaster;
        this.world = world;
    }
    
    
    /** Create the background. */
    public void spawnBackground()
    {
        final Avatar avatar;
        
        final AtlasRegion background = gameMaster.getAtlas().findRegion("bg");
        final int widthScreen = Gdx.graphics.getWidth();
        final int heightScreen = Gdx.graphics.getHeight();
        final int widthBackground = background.getRegionWidth();
        final int heightBackground = background.getRegionHeight();
        
        avatar = new Avatar(
            background,
            widthScreen / (float) heightScreen < widthBackground / (float) heightBackground ?
            heightScreen / (float) heightBackground :
            widthScreen / (float) widthBackground
        );
        
        world.addEntity(
            (char) GameplayWorld.SYSFLAG_DRAW,
            new DatastoreForEW[] {
                world.avatarDatastore
            },
            avatar
        );
    }
    
    
    /** Create the player. */
    public void spawnPlayer()
    {
        final Player player;
        final Avatar avatar;
        final Mobility mobility;
        final Collision collision;
        
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        final Vector2 center = new Vector2(width * 0.5f, height * 0.5f);
        
        player = new Player()
        {
            @Override public void onCastFireball(Avatar playerCharacter)
            {
                spawnFireball(playerCharacter);
            }
        };
        
        avatar = new Avatar(
            gameMaster.getAtlas().findRegion("wizard"),
            4f
        );
        center.sub(avatar.getTrueSize().scl(0.5f));
        avatar.position.set(center);
        player.setAvatar(avatar);
        
        mobility = new Mobility(0f);
        
        collision = new Collision(avatar)
        {
            @Override public void onCollided(int thisEntity, int otherEntity, Collision thisCollision, Collision otherCollision) {}
        };
        
        world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_PLAYER |
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new DatastoreForEW[] {
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
            final Avatar avatar;
            final Mobility mobility;
            final Collision collision;
            final Spider spider;
            
            float posX = Gdx.graphics.getWidth();
            
            avatar = new Avatar(
                gameMaster.getAtlas().findRegion("spider"),
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
            
            collision = new Collision(avatar)
            {
                @Override public void onCollided(int thisEntity, int otherEntity, Collision thisCollision, Collision otherCollision)
                {
                    if (world.playerDatastore.contains(otherEntity)) onSpiderHitPlayer(thisEntity, otherEntity);
                }
            };
            
            spider = new Spider();
            spider.newPath(avatar, mobility);
            
            world.addEntity(
                (char) (
                    GameplayWorld.SYSFLAG_AVATAR |
                    GameplayWorld.SYSFLAG_COLLISION |
                    GameplayWorld.SYSFLAG_SPIDER |
                    GameplayWorld.SYSFLAG_DRAW
                ),
                new DatastoreForEW[] {
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
        final Avatar avatar;
        final Mobility mobility;
        final Collision collision;
        final Projectile projectile;
        
        final TextureRegion spellTexture = new TextureRegion(gameMaster.getAtlas().findRegion("spell"));
        spellTexture.flip(false, true);
        
        avatar = new Avatar(
            spellTexture,
            2f
        );
        avatar.position.set(
            playerCharacter.position.cpy().add(playerCharacter.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(1000f, Vector2.Y.cpy());
        
        collision = new Collision(avatar)
        {
            @Override public void onCollided(int thisEntity, int otherEntity, Collision thisCollision, Collision otherCollision)
            {
                if (world.spiderDatastore.contains(otherEntity)) onSpellHitSpider(thisEntity, otherEntity);
            }
        };
        
        projectile = new Projectile("spell");
        
        world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new DatastoreForEW[] {
                world.avatarDatastore,
                world.mobilityDatastore,
                world.collisionDatastore,
                world.projectileDatastore
            },
            avatar, mobility, collision, projectile
        );
    }
    
    
    /** Throw a web at the player. */
    public void spawnWeb(Avatar spiderAvatar)
    {
        final Avatar avatar;
        final Mobility mobility;
        final Collision collision;
        final Projectile projectile;
        
        avatar = new Avatar(
            gameMaster.getAtlas().findRegion("web"),
            2f
        );
        avatar.position.set(
            spiderAvatar.position.cpy().add(spiderAvatar.getTrueSize().sub(avatar.getTrueSize()).scl(0.5f))
        );
        
        mobility = new Mobility(1000f, Vector2.Y.cpy());
        
        collision = new Collision(avatar)
        {
            @Override public void onCollided(int thisEntity, int otherEntity, Collision thisCollision, Collision otherCollision)
            {
                if (world.playerDatastore.contains(otherEntity)) onWebHitPlayer(thisEntity, otherEntity);
            }
        };
        
        projectile = new Projectile("spell");
        
        world.addEntity(
            (char) (
                GameplayWorld.SYSFLAG_AVATAR |
                GameplayWorld.SYSFLAG_COLLISION |
                GameplayWorld.SYSFLAG_DRAW
            ),
            new DatastoreForEW[] {
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
        final GameplayState state = world.getState();
        
        state.score += GameplayState.POINTS_WEB_HIT_PLAYER;
        if (state.lives-- < 0) requestGameOver();
    }
    
    
    /** Player is hit by a spider. */
    public void onSpiderHitPlayer(int spiderEntity, int playerEntity)
    {
        final GameplayState state = world.getState();
        
        state.score += GameplayState.POINTS_SPIDER_HIT_PLAYER;
        if (state.lives-- < 0) requestGameOver();
    }
    
    
    /** A spider is hit by the player's spell. */
    public void onSpellHitSpider(int spellEntity, int spiderEntity)
    {
        final GameplayState state = world.getState();
        
        state.score += GameplayState.POINTS_SPELL_HIT_SPIDER;
        world.removeEntityRequest(spellEntity);
        world.removeEntityRequest(spiderEntity);
    }
    
    
    /** Event for when the player is out of lives. */
    public abstract void requestGameOver();
}
