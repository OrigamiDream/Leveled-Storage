package av.is.leveledstorage.storages.bukkit.util;

import av.is.leveledstorage.StorageObject;
import org.bukkit.util.BlockVector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class BlockVectorStorage extends VectorStorage {
    
    public BlockVectorStorage(BlockVector vector) {
        super(vector);
    }
    
    @Override
    public StorageObject delegate() {
        return new BlockVectorStorage(new BlockVector(vector.getX(), vector.getY(), vector.getZ()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        double x = input.readDouble();
        double y = input.readDouble();
        double z = input.readDouble();
        
        setValue(new BlockVector(x, y, z));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeDouble(getValue().getX());
        output.writeDouble(getValue().getY());
        output.writeDouble(getValue().getZ());
    }
}
