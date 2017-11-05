package av.is.leveledstorage.storages.bukkit;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class LocationStorage implements DataStorageObject<Location> {
    
    private Location location;
    
    public LocationStorage(Location location) {
        checkArgument(location.getWorld() != null, "World cannot be null.");
        this.location = location;
    }
    
    @Override
    public Location getValue() {
        return location;
    }
    
    @Override
    public void setValue(Location value) {
        location = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new LocationStorage(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        setValue(new Location(Bukkit.getWorld(input.readUTF()), input.readDouble(), input.readDouble(), input.readDouble(), input.readFloat(), input.readFloat()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeUTF(getValue().getWorld().getName());
        output.writeDouble(getValue().getX());
        output.writeDouble(getValue().getY());
        output.writeDouble(getValue().getZ());
        output.writeFloat(getValue().getYaw());
        output.writeFloat(getValue().getPitch());
    }
}
