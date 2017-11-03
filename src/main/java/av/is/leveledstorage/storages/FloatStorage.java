package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatStorage implements StorageObject<DataInputStream, DataOutputStream, Float> {
    
    private float value;
    
    public FloatStorage(float value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readFloat();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeFloat(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Float getValue() {
        return value;
    }
    
    @Override
    public void setValue(Float value) {
        this.value = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new FloatStorage(value);
    }
}
