package quyaze.stsj.core;

import com.badlogic.gdx.Screen;

import quyaze.stsj.SmiteTheSpiders;

/**
 * A template class for Screens.
 * <p></p>
 * Contains a reference to the owning game and a constructor.
 */
public abstract class StarterScreen implements Screen
{
    final protected SmiteTheSpiders game;
    public StarterScreen(SmiteTheSpiders game)
    {
        this.game = game;
    }
}
