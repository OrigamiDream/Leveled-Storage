package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.bukkit.FireworkEffectStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class FireworkMetaStorage extends ItemMetaStorage<FireworkMeta> {
    
    public FireworkMetaStorage(FireworkMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasEffects = input.readBoolean();
        if(hasEffects) {
            ArrayListStorage<FireworkEffectStorage> storage = new ArrayListStorage<>(null);
            storage.read(input);
            getValue().addEffects(storage.getValue().stream().map(FireworkEffectStorage::getValue).collect(Collectors.toList()));
        }
        getValue().setPower(input.readInt());
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().hasEffects());
        if(getValue().hasEffects()) {
            ArrayListStorage<FireworkEffectStorage> storage = new ArrayListStorage<FireworkEffectStorage>(getValue().getEffects().stream().map(FireworkEffectStorage::new).collect(Collectors.toList()));
            storage.write(output);
        }
    }
}
