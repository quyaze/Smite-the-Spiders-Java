package quyaze.stsj.gameplay.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import quyaze.stsj.SmiteTheSpiders;
import quyaze.stsj.core.EWSystem;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.screens.GameplayScreen;

/** System that draws and renders {@link Avatar}s to the screen. */
public class DrawSystem implements EWSystem
{
    /*  Iterate  */
    @Override
    public void iterate(int entity)
    {
        Avatar avatar = GameplayScreen.world.avatarDatastore.get(entity);
        
        SpriteBatch batch = SmiteTheSpiders.getBatch();
        final float opacity = MathUtils.clamp(
            GameplayScreen.state.isPaused() ? avatar.opacity * 0.2f : avatar.opacity,
            0,
            1f
        );
        
        batch.setColor(1f, 1f, 1f, opacity);
        batch.draw(
            avatar.texture,
            avatar.position.x,
            avatar.position.y,
            avatar.getTrueWidth(),
            avatar.getTrueHeight()
        );
        batch.setColor(Color.WHITE);
    }
}
