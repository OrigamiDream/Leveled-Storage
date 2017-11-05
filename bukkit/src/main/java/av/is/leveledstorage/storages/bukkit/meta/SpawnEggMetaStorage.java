package av.is.leveledstorage.storages.bukkit.meta;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class SpawnEggMetaStorage extends ItemMetaStorage<SpawnEggMeta> {
    
    public SpawnEggMetaStorage(SpawnEggMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        getValue().setSpawnedType(EntityType.valueOf(input.readUTF()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeUTF(getValue().getSpawnedType().toString());
    }
}
