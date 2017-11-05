package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import com.google.common.collect.SetMultimap;

/**
 * Created by Avis Network on 2017-11-05.
 */
public abstract class SetMultimapStorage<K extends StorageObject, V extends StorageObject, M extends SetMultimap<K, V>> extends MultimapStorage<K, V, M> {
    
    public SetMultimapStorage(M map) {
        super(map);
    }
}
