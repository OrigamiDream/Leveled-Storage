package av.is.leveledstorage;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class LeveledStorageAdapter extends Thread {
    
    private LeveledStorage storage;
    
    public LeveledStorageAdapter(LeveledStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void run() {
        storage.processSave();
    }
}
