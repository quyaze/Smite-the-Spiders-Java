/*
//      GameplayState.java
*/


package latech.stsj.gameplay;


//  TODO: javadoc
public class GameplayState
{
    //  Fields
    public int points;
    public int lives;
    
    final static public int POINTS_HIT_SPIDER = 50;
    
    
    //  Constructor
    public GameplayState(int startingLives)
    {
        lives = startingLives;
    }
}
