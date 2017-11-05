package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Avis Network on 2017-11-04.
 */
public class HashMapStorage<K extends StorageObject, V extends StorageObject> extends MapStorage<K, V, Map<K, V>> {
    
    public HashMapStorage(Map<K, V> map) {
        super(map);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(new HashMap<>(size));
        for(int i = 0; i < size; i++) {
            K key = (K) generic[0].delegate();
            V value = (V) generic[1].delegate();
            
            key.read(input);
            value.read(input);
            
            getValue().put(key, value);
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output) throws IOException {
        output.writeInt(getValue().size());
        for(Map.Entry<K, V> entry : getValue().entrySet()) {
            K key = (K) entry.getKey().delegate();
            V value = (V) entry.getValue().delegate();
            
            key.write(output);
            value.write(output);
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new HashMapStorage(new HashMap<K, V>(getValue()));
    }
}
