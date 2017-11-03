package av.is.leveledstorage.storages.util;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Avis Network on 2017-11-04.
 */
public class ArrayListStorage<T extends StorageObject> extends ListStorage<T, ArrayList<T>> {
    
    public ArrayListStorage(ArrayList<T> collection) {
        super(collection);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new ArrayList<>(size));
        for(int i = 0; i < size; i++) {
            T obj = (T) generic[0].delegate();
            
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
        return new ArrayListStorage(new ArrayList<T>(getValue()));
    }
}
