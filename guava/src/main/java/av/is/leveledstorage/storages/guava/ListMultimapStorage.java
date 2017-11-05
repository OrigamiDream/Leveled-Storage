package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import com.google.common.collect.ListMultimap;

/**
 * Created by Avis Network on 2017-11-05.
 */
public abstract class ListMultimapStorage<K extends StorageObject, V extends StorageObject, M extends ListMultimap<K, V>> extends MultimapStorage<K, V, M> {
    
    public ListMultimapStorage(M map) {
        super(map);
    }
}
