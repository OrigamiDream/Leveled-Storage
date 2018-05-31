package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;

import java.util.Stack;

/**
 * Created by Avis Network on 2018-06-01.
 */
public class StackStorage<T extends StorageObject> extends VectorStorage<T> {
    
    public StackStorage(Stack<T> collection) {
        super(collection);
    }
}
