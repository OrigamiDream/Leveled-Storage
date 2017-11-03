package av.is.leveledstorage.storages.util;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Avis Network on 2017-11-04.
 */
public class HashSetStorage<T extends StorageObject> extends SetStorage<T, HashSet<T>> {
    
    public HashSetStorage(HashSet<T> collection) {
        super(collection);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new HashSet<>(size));
        for(int i = 0; i < size; i++) {
            T obj = (T) generic[0].delegate();
            obj.read(input);
            getValue().add(obj);
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output, StorageObject... generic) throws IOException {
        output.write(getValue().size());
        for(T obj : getValue()) {
            obj.write(output);
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new HashSetStorage(new HashSet<T>(getValue()));
    }
}
