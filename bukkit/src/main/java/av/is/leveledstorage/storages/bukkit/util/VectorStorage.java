package av.is.leveledstorage.storages.bukkit.util;

import av.is.leveledstorage.StorageObject;
import org.bukkit.util.Vector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class VectorStorage implements StorageObject<DataInputStream,DataOutputStream,Vector> {
    
    protected Vector vector;
    
    public VectorStorage(Vector vector) {
        this.vector = vector;
    }
    
    @Override
    public Vector getValue() {
        return vector;
    }
    
    @Override
    public void setValue(Vector value) {
        vector = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new VectorStorage(new Vector(vector.getX(), vector.getY(), vector.getZ()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        double x = input.readDouble();
        double y = input.readDouble();
        double z = input.readDouble();
        
        setValue(new Vector(x, y, z));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeDouble(getValue().getX());
        output.writeDouble(getValue().getY());
        output.writeDouble(getValue().getZ());
    }
}
