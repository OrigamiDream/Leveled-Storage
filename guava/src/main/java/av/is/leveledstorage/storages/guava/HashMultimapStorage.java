package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import com.google.common.collect.HashMultimap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class HashMultimapStorage<K extends StorageObject, V extends StorageObject> extends SetMultimapStorage<K, V, HashMultimap<K, V>> {
    
    public HashMultimapStorage(HashMultimap<K, V> map) {
        super(map);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(HashMultimap.create());
        for(int i = 0; i < size; i++) {
            int len = input.readInt();
            K key = (K) generic[0].delegate();
            
            key.read(input);
    
            for(int j = 0; j < len; j++) {
                V value = (V) generic[1].delegate();
        
                value.read(input);
        
                getValue().put(key, value);
            }
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output) throws IOException {
        output.writeInt(getValue().size());
        for(K key : getValue().keySet()) {
            Set<V> values = getValue().get(key);
            output.writeInt(values.size());
            
            key.write(output);
            
            for(V value : values) {
                value.delegate().write(output);
            }
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new HashMultimapStorage(HashMultimap.create(getValue()));
    }
}
