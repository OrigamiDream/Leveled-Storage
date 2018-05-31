package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Avis Network on 2018-06-01.
 */
public class VectorStorage<T extends StorageObject> extends ListStorage<T, Vector<T>> {
    
    public VectorStorage(Vector<T> collection) {
        super(collection);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new Vector<>(size));
        for(int i = 0; i < size; i++) {
            T obj = (T) generic[i].delegate();
            
            obj.read(input);
            getValue().addElement(obj);
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
        return new VectorStorage(new Vector<T>(getValue()));
    }
}
