package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongStorage implements StorageObject<DataInputStream, DataOutputStream, Long> {
    
    private long value;
    
    public LongStorage(long value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readLong();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeLong(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Long getValue() {
        return value;
    }
    
    @Override
    public void setValue(Long value) {
        this.value = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new LongStorage(value);
    }
}
