package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;

import java.util.List;

/**
 * Created by Avis Network on 2017-11-04.
 */
public abstract class ListStorage<T extends StorageObject, C extends List<T>> extends CollectionStorage<T, C> {
    
    public ListStorage(C collection) {
        super(collection);
    }
}
