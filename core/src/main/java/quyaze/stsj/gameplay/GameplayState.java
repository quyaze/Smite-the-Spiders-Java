package quyaze.stsj.gameplay;

import quyaze.stsj.core.Event;
import quyaze.stsj.gameplay.events.Paused;

final public class GameplayState
{
    //  Fields
    private boolean paused = true;
    
    final public Event<Paused> eventPaused;
    
    
    //  Constructor
    public GameplayState()
    {
        eventPaused = new Event<>(2);
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
     * @return Able to change the pause state (current state was not already the desired state)
     */
    public boolean setGamePaused(boolean paused)
    {
        if (this.paused == paused) return false;
        eventPaused.fire(new Paused(paused));
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
