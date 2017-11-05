package av.is.leveledstorage.storages.bukkit.util;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.util.EulerAngle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class EulerAngleStorage implements DataStorageObject<EulerAngle> {
    
    private EulerAngle angle;
    
    public EulerAngleStorage(EulerAngle angle) {
        this.angle = angle;
    }
    
    @Override
    public EulerAngle getValue() {
        return angle;
    }
    
    @Override
    public void setValue(EulerAngle value) {
        angle = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new EulerAngleStorage(new EulerAngle(angle.getX(), angle.getY(), angle.getZ()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        double x = input.readDouble();
        double y = input.readDouble();
        double z = input.readDouble();
    
        setValue(new EulerAngle(x, y, z));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeDouble(getValue().getX());
        output.writeDouble(getValue().getY());
        output.writeDouble(getValue().getZ());
    }
}
