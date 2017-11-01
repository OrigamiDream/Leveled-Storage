package av.is.leveledstorage;

import av.is.leveledstorage.tags.IntStorage;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class LeveledStorageTest {
    
    public static void main(String args[]) {
        LeveledStorage<IntStorage> storage = LeveledStorage.createEmptyStorage(IntStorage.class, 5);
        storage.getConfiguration().asyncTransition = false;
        
        storage.add(new IntStorage(5));
        storage.add(new IntStorage(4));
        storage.add(new IntStorage(3));
        storage.add(new IntStorage(2));
        
        storage.processTransition();
    
        storage.add(new IntStorage(6));
        storage.add(new IntStorage(7));
        storage.add(new IntStorage(8));
        
        storage.processTransition();
        storage.processTransition();
        
        storage.add(new IntStorage(9));
        storage.add(new IntStorage(10));
        storage.add(new IntStorage(11));
        storage.add(new IntStorage(12));
        storage.add(new IntStorage(13));
        
        storage.debug();
        
        System.out.println("-----------------------");
        
        storage.processTransition();
        
        storage.debug();
    
        System.out.println("-----------------------");
    
        storage.processTransition();
    
        storage.debug();
    }
}
