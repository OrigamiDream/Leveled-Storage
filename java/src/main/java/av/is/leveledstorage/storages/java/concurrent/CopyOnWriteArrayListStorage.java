package av.is.leveledstorage.storages.java.concurrent;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.java.ListStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Avis Network on 2018-06-01.
 */
public class CopyOnWriteArrayListStorage<T extends StorageObject> extends ListStorage<T, CopyOnWriteArrayList<T>> {
    
    public CopyOnWriteArrayListStorage(CopyOnWriteArrayList<T> collection) {
        super(collection);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new CopyOnWriteArrayList<>());
        for(int i = 0; i < size; i++) {
            T obj = (T) generic[i].delegate();
            
            obj.read(input);
            
            getValue().add(obj);
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output) throws IOException {
        output.write(getValue().size());
        for(int i = 0; i < getValue().size(); i++) {
            T obj = getValue().get(i);
            obj.write(output);
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new CopyOnWriteArrayListStorage(new CopyOnWriteArrayList<T>(getValue()));
    }
}
