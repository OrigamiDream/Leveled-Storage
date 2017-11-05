package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.GenericStorage;
import com.google.common.collect.Table;

/**
 * Created by Avis Network on 2017-11-05.
 */
public abstract class TableStorage<R extends StorageObject, C extends StorageObject, V extends StorageObject, T extends Table<R, C, V>> extends GenericStorage<T> {
    
    private T table;
    
    public TableStorage(T table) {
        this.table = table;
    }
    
    @Override
    public T getValue() {
        return table;
    }
    
    @Override
    public void setValue(T value) {
        table = value;
    }
}
