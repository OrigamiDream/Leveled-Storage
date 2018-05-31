package av.is.leveledstorage.storages.java.concurrent;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.java.SetStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Avis Network on 2018-06-01.
 */
public class CopyOnWriteArraySetStorage<T extends StorageObject> extends SetStorage<T, CopyOnWriteArraySet<T>> {
    
    public CopyOnWriteArraySetStorage(CopyOnWriteArraySet<T> collection) {
        super(collection);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new CopyOnWriteArraySet<>());
        for(int i = 0; i < size; i++) {
            T obj = (T) generic[0].delegate();
            obj.read(input);
            getValue().add(obj);
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output) throws IOException {
        output.write(getValue().size());
        for(T obj : getValue()) {
            obj.write(output);
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new CopyOnWriteArraySetStorage(new CopyOnWriteArraySet<T>(getValue()));
    }
}
