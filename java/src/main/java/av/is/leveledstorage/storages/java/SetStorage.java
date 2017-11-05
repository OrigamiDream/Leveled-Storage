package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;

import java.util.Set;

/**
 * Created by Avis Network on 2017-11-04.
 */
public abstract class SetStorage<T extends StorageObject, C extends Set<T>> extends CollectionStorage<T, C> {
    
    public SetStorage(C collection) {
        super(collection);
    }
}
