package quyaze.stsj.core.template;

import com.badlogic.gdx.utils.Array;

import quyaze.stsj.core.utility.Signal;

/**
 * A plug-in for {@link World}.
 * <p></p>
 * Acts as a timer that functions by ticking. Unlike
 * {@link com.badlogic.gdx.utils.Timer libGDX's Timer}, it belongs
 * to the {@link World} and is ticked instead of scheduled.
*/
public class WorldTimer
{
    /*  Fields  */
    private Array<Ticker> tickers;
    
    
    /*  Constructor  */
    public WorldTimer()
    {
        tickers = new Array<>(false, 8);
    }
    
    
    public void tick(float dS)
    {
        for (int i = tickers.size; i > 0;)
        {
            Ticker node = tickers.items[--i];
            if (node.stopped) continue;
            if ((node.elapsed += dS) > node.duration)
            {
                node.onFinish.fire();
                node.owner = null;
                tickers.items[tickers.size - 1].i = i;
                tickers.removeIndex(i);
            }
        }
    }
    
    
    public Ticker spawn()
    {
        Ticker node = new Ticker();
        node.owner = this;
        node.i = tickers.size;
        node.onResume = new Signal(1);
        node.onPause = new Signal(1);
        node.onFinish = new Signal(1);
        tickers.add(node);
        return node;
    }
    
    
    public Ticker spawn(float duration)
    {
        if (duration <= 0f || Float.isNaN(duration)) throw new IllegalArgumentException("duration is not > 0 seconds");
        Ticker node = new Ticker();
        node.owner = this;
        node.i = tickers.size;
        node.duration = duration;
        node.onResume = new Signal(1);
        node.onPause = new Signal(1);
        node.onFinish = new Signal(1);
        tickers.add(node);
        return node;
    }
    
    
    final static public class Ticker
    {
        /*  Fields  */
        private WorldTimer owner;
        private int i;
        private float duration = 1f, elapsed;
        private boolean stopped;
        public Signal onResume, onPause, onFinish;
        private Ticker() {}
        
        
        public void cancel()
        {
            if (owner == null)
                throw new IllegalStateException("cannot cancel a finished timer");
            owner.tickers.removeIndex(i);
        }
        
        
        public void pause()
        {
            if (owner == null)
                throw new IllegalStateException("cannot pause a finished timer");
            stopped = true;
        }
        
        
        public void unpause()
        {
            if (owner == null)
                throw new IllegalStateException("cannot unpause a finished timer");
            stopped = false;
        }
    }
}
