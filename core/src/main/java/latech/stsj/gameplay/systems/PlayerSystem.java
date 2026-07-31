/*
//      PlayerSystem.java
*/


package latech.stsj.gameplay.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import latech.stsj.gameplay.GameplayWorld;
import latech.stsj.gameplay.structure.Collision;
import latech.stsj.gameplay.structure.Mobility;
import latech.stsj.gameplay.structure.Player;
import latech.stsj.gameplay.structure.TextureDrawable;
import latech.stsj.templates.Entity;
import latech.stsj.templates.System;


/**
 * System for player input.
 */
public class PlayerSystem implements System
{
    //  Fields
    private GameplayWorld world;
    
    
    //  Constructor
    public PlayerSystem(GameplayWorld world)
    {
        this.world = world;
    }
    
    
    //  Render
    @Override
    public void render(float deltaSeconds, Entity entity)
    {
        int id = entity.getId();
        
        TextureDrawable drawable = world.textureDrawableStore.get(id);
        Mobility mobility = world.mobilityStore.get(id);
        Player player = world.playerStore.get(id);
        Collision collision = world.collisionStore.get(id);
        
        Vector2 movementInput2D = player.movementInput2D;
        movementInput2D.setZero();
        
        /*  Input
        */
        final boolean goUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8);
        final boolean goRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6);
        final boolean goDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2);
        final boolean goLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4);
        final boolean castSpell = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        
        //  TODO: event deferred
        if (castSpell) world.spawnFireball(drawable);
        
        /*  Logic
        */
        if (goUp) movementInput2D.y += 1f;
        if (goRight) movementInput2D.x += 1f;
        if (goDown) movementInput2D.y -= 1f;
        if (goLeft) movementInput2D.x -= 1f;
        
        //  Game controllers coming soon
        float inputStrength = movementInput2D.len2();
        float speed = 0f;
        if (inputStrength > 0.04f)
        {
            float magnitude = (float) Math.sqrt(inputStrength);
            movementInput2D.scl(1f / magnitude);
            speed = player.maxSpeed * Math.min(magnitude, 1f);
        }
        mobility.setSpeed(speed);
        mobility.setDirection(movementInput2D);
        
        /*
        //  Is hit by a spider's web
        if (collision.collidingEntities.notEmpty())
        {
            world.onPlayerHit(entity);
        }
        */
    }
}
