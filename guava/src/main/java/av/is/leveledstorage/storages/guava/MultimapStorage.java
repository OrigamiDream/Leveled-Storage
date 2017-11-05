package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.GenericStorage;
import com.google.common.collect.Multimap;

/**
 * Created by Avis Network on 2017-11-05.
 */
public abstract class MultimapStorage<K extends StorageObject, V extends StorageObject, M extends Multimap<K, V>> extends GenericStorage<M> {
    
    private M map;
    
    public MultimapStorage(M map) {
        this.map = map;
    }
    
    @Override
    public M getValue() {
        return map;
    }
    
    @Override
    public void setValue(M value) {
        map = value;
    }
    
    @Override
    public int generics() {
        return 2;
    }
}
