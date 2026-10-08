package quyaze.stsj.gameplay;

import quyaze.stsj.core.template.ScreenContext;
import quyaze.stsj.core.utility.Event;
import quyaze.stsj.screens.GameplayScreen;

/**
 * A subsystem to {@link GameplayScreen}.
 * <p></p>
 * Tracks any data or states, acting as a kind of game state
 * object.
*/
public class GameplayState extends ScreenContext<GameplayScreen>
{
    /*  Fields  */
    private boolean paused = true;
    public int score;
    public int lives;
    private GState state;
    
    public Event<Boolean> onPausedStateChanged;
    public Event<GState> onGameStateChanged;
    
    
    /*  Constructor  */
    public GameplayState()
    {
        onPausedStateChanged = new Event<>(Boolean.class);
        onGameStateChanged = new Event<>(GState.class);
        reset();
    }
    
    
    /*  Create  */
    @Override public void create() {}
    
    
    /**
     * @return Game {@link State}
    */
    public GState getState()
    {
        return state;
    }
    
    
    /** Set the game state, which is controlled. */
    public void setState(GState state)
    {
        /*  Intermission can only change into Round
            Round can only change into GameOver
            Cannot set state if GameOver
      */
        switch (this.state) {
            case INTERMISSION: if (state != GState.ROUND) return;
            case ROUND: if (state != GState.GAME_OVER) return;
            default: break;
        }
        onGameStateChanged.fire(state);
        this.state = state;
    }
    
    
    /**
     * @return Paused state
    */
    public boolean isPaused()
    {
        return paused;
    }
    
    
    /**
     * Set the game paused.
     * @return Was able to change the pause state (current state was not already the desired state)
    */
    public boolean setGamePaused(boolean paused)
    {
        if (this.paused == paused) return false;
        onPausedStateChanged.fire(paused);
        this.paused = paused;
        return true;
    }
    
    
    /**
     * Toggle the paused state.
     * @return New paused state
    */
    public boolean toggleGamePaused()
    {
        setGamePaused(!paused);
        return paused;
    }
    
    
    /*  Reset game data.  */
    public void reset()
    {
        state = GState.INTERMISSION;
        score = 0;
        lives = 3;
    }
    
    
    /** On {@link GameplayScreen#hide()}. */
    public void hide()
    {
        reset();
    }
    
    
    /** Game State. */
    static public enum GState
    {
        INTERMISSION, ROUND, GAME_OVER
    }
}
