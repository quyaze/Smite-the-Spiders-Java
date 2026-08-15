package quyaze.stsj.screens;

import com.badlogic.gdx.Screen;

import quyaze.stsj.GameMaster;

/**
 * A template class for Screens.
 * <p></p>
 * Contains a reference to the owning GameMaster and a constructor.
 */
public abstract class StarterScreen implements Screen
{
    final protected GameMaster gameMaster;
    public StarterScreen(GameMaster gameMaster)
    {
        this.gameMaster = gameMaster;
    }
}
