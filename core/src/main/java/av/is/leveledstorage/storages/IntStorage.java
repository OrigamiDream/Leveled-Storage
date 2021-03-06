package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntStorage implements StorageObject<DataInputStream, DataOutputStream, Integer> {
    
    private int value;
    
    public IntStorage(int value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readInt();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Integer getValue() {
        return value;
    }
    
    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new IntStorage(value);
    }
}
