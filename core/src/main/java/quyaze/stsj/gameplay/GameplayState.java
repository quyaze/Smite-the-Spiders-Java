package quyaze.stsj.gameplay;

import quyaze.stsj.core.Event;
import quyaze.stsj.core.ScreenContext;
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
    private State state;
    
    public Event<Boolean> onPausedStateChanged;
    public Event<State> onGameStateChanged;
    
    final static public int POINTS_SPELL_HIT_SPIDER = 50;
    final static public int POINTS_SPIDER_HIT_PLAYER = -5;
    final static public int POINTS_WEB_HIT_PLAYER = -20;
    
    
    /*  Constructor  */
    public GameplayState()
    {
        onPausedStateChanged = new Event<>();
        onGameStateChanged = new Event<>();
        reset();
    }
    
    
    /*  Create  */
    @Override public void create()
    {
        getScreen().core.onGameOver.bindDeferred(
            () -> {
                reset();
            }
        );
    }
    
    
    /**
     * @return Game {@link State}
     */
    public State getState()
    {
        return state;
    }
    
    
    /** Set the game state, which is controlled. */
    public void setState(State state)
    {
        /*  Intermission can only change into Round
            Round can only change into GameOver
            Cannot set state if GameOver
        */
        switch (this.state) {
            case INTERMISSION: if (state != State.ROUND) return; break;
            case ROUND: if (state != State.GAME_OVER) return; break;
            default: return; 
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
        state = State.INTERMISSION;
        score = 0;
        lives = 3;
    }
    
    
    /** Game State. */
    static public enum State
    {
        INTERMISSION, ROUND, GAME_OVER
    }
}
