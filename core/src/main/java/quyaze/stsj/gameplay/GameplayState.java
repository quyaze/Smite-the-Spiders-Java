package quyaze.stsj.gameplay;

/** A class that keeps track of overlying data and states. */
public abstract class GameplayState
{
    //  Fields
    private boolean paused;
    public int score;
    public int lives;
    
    final static public int POINTS_SPELL_HIT_SPIDER = 50;
    final static public int POINTS_SPIDER_HIT_PLAYER = -5;
    final static public int POINTS_WEB_HIT_PLAYER = -20;
    
    
    //  Constructor
    public GameplayState()
    {
        paused = true;
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
     * @return Able to change the pause state (current state was not already the desired state)
     */
    public boolean setGamePaused(boolean paused)
    {
        if (this.paused == paused) return false;
        onPausedStateChanged(paused);
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
    
    
    /** Event for when the game switches pause states. */
    public abstract void onPausedStateChanged(boolean paused);
}
