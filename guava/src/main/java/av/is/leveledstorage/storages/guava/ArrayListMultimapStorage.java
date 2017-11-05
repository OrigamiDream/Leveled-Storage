package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import com.google.common.collect.ArrayListMultimap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class ArrayListMultimapStorage<K extends StorageObject, V extends StorageObject> extends ListMultimapStorage<K, V, ArrayListMultimap<K, V>> {
    
    public ArrayListMultimapStorage(ArrayListMultimap<K, V> map) {
        super(map);
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int size = input.readInt();
        setValue(ArrayListMultimap.create());
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
            List<V> values = getValue().get(key);
            output.writeInt(values.size());
            
            key.write(output);
            
            for(int j = 0; j < values.size(); j++) {
                V value = (V) values.get(j).delegate();
                
                value.write(output);
            }
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new ArrayListMultimapStorage(ArrayListMultimap.create(getValue()));
    }
}
