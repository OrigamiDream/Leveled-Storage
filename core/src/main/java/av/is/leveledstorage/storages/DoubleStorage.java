package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleStorage implements StorageObject<DataInputStream, DataOutputStream, Double> {
    
    private double value;
    
    public DoubleStorage(double value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readDouble();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeDouble(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Double getValue() {
        return value;
    }
    
    @Override
    public void setValue(Double value) {
        this.value = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new DoubleStorage(value);
    }
}
