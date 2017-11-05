package av.is.leveledstorage.storages.bukkit.meta;

import org.bukkit.inventory.meta.SkullMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class SkullMetaStorage extends ItemMetaStorage<SkullMeta> {
    
    public SkullMetaStorage(SkullMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasOwner = input.readBoolean();
        if(hasOwner) {
            getValue().setOwner(input.readUTF());
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().hasOwner());
        if(getValue().hasOwner()) {
            output.writeUTF(getValue().getOwner());
        }
    }
}
