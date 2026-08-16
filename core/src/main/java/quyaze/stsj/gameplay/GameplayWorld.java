package quyaze.stsj.gameplay;

import com.badlogic.gdx.utils.CharArray;

import quyaze.stsj.GameMaster;
import quyaze.stsj.core.EWDatastore;
import quyaze.stsj.core.EntityWorld;
import quyaze.stsj.gameplay.architecture.Avatar;
import quyaze.stsj.gameplay.architecture.Mobility;
import quyaze.stsj.screens.GameplayScreen;

public class GameplayWorld extends EntityWorld
{
    //  Fields
    final private GameMaster gameMaster;
    
    final private CharArray entityFlags;
    
    final public EWDatastore<Avatar> avatarDatastore;
    final public EWDatastore<Mobility> mobilityDatastore;
    
    final static private char flagPlayer = 1;       //      1 = 1 << 0
    final static private char flagCollision = 2;    //     10 = 1 << 1
    final static private char flagAvatar = 4;       //    100 = 1 << 2
    final static private char flagSpider = 8;       //   1000 = 1 << 3
    final static private char flagTexture = 16;     //  10000 = 1 << 4
    
    public boolean gamePaused;
    
    
    //  Constructor
    public GameplayWorld(GameplayScreen owner, GameMaster gameMaster)
    {
        super(owner);
        this.gameMaster = gameMaster;
        entityFlags = new CharArray(false, 64);
        avatarDatastore = new EWDatastore<>(64);
        mobilityDatastore = new EWDatastore<>(64);
    }
    
    
    //  Render
    @Override
    public void render()
    {
        for (char flag = 1; flag <= flagTexture; flag <<= 1)
        {}
    }
    
    
    //  Add Entity
    @Override
    public int addEntity()
    {
        return entities++;
    }
    
    
    //  Remove Entity
    @Override
    public void removeEntity(int entity)
    {
        entities--;
    }
    
    
    public void ownerShow()
    {
        
    }
    
    
    public void ownerHide()
    {
        entities = 0;
        entityFlags.clear();
        avatarDatastore.clear();
        mobilityDatastore.clear();
    }
}
