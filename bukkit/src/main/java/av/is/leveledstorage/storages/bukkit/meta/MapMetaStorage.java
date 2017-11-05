package av.is.leveledstorage.storages.bukkit.meta;

import org.bukkit.Color;
import org.bukkit.inventory.meta.MapMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class MapMetaStorage extends ItemMetaStorage<MapMeta> {
    
    public MapMetaStorage(MapMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        getValue().setScaling(input.readBoolean());
        
        boolean hasLocationName = input.readBoolean();
        if(hasLocationName) {
            getValue().setLocationName(input.readUTF());
        }
        
        boolean hasColor = input.readBoolean();
        if(hasColor) {
            getValue().setColor(Color.fromRGB(input.readInt(), input.readInt(), input.readInt()));
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().isScaling());
        output.writeBoolean(getValue().hasLocationName());
        if(getValue().hasLocationName()) {
            output.writeUTF(getValue().getLocationName());
        }
        output.writeBoolean(getValue().hasColor());
        if(getValue().hasColor()) {
            output.writeInt(getValue().getColor().getRed());
            output.writeInt(getValue().getColor().getGreen());
            output.writeInt(getValue().getColor().getBlue());
        }
    }
}
