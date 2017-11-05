package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.bukkit.FireworkEffectStorage;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class FireworkEffectMetaStorage extends ItemMetaStorage<FireworkEffectMeta> {
    
    public FireworkEffectMetaStorage(FireworkEffectMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasEffect = input.readBoolean();
        if(hasEffect) {
            FireworkEffectStorage storage = new FireworkEffectStorage(null);
            storage.read(input);
            getValue().setEffect(storage.getValue());
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().hasEffect());
        if(getValue().hasEffect()) {
            FireworkEffectStorage storage = new FireworkEffectStorage(getValue().getEffect());
            storage.write(output);
        }
    }
}
