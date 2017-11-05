package av.is.leveledstorage.storages.bukkit;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.NamespacedKey;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class NamespacedKeyStorage implements DataStorageObject<NamespacedKey> {
    
    private NamespacedKey key;
    
    public NamespacedKeyStorage(NamespacedKey key) {
        this.key = key;
    }
    
    @Override
    public NamespacedKey getValue() {
        return key;
    }
    
    @Override
    public void setValue(NamespacedKey value) {
        key = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new NamespacedKeyStorage(new NamespacedKey(key.getNamespace(), key.getKey()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        setValue(new NamespacedKey(input.readUTF(), input.readUTF()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeUTF(getValue().getNamespace());
        output.writeUTF(getValue().getKey());
    }
}
