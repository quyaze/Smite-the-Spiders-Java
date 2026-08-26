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
    
    public Event<Boolean> onPausedStateChanged;
    
    final static public int POINTS_SPELL_HIT_SPIDER = 50;
    final static public int POINTS_SPIDER_HIT_PLAYER = -5;
    final static public int POINTS_WEB_HIT_PLAYER = -20;
    
    
    /*  Constructor  */
    public GameplayState()
    {
        onPausedStateChanged = new Event<>();
        reset();
    }
    
    
    /*  Create  */
    @Override public void create() {}
    
    
    public void reset()
    {
        score = 0;
        lives = 3;
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
}
