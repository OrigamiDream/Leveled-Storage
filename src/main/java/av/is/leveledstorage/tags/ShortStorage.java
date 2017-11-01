package av.is.leveledstorage.tags;

import av.is.leveledstorage.ReturnableObject;
import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShortStorage implements ReturnableObject<DataInputStream, DataOutputStream, Short> {
    
    private short value;
    
    public ShortStorage(short value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readShort();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeShort(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Short getValue() {
        return value;
    }
    
    @Override
    public void setValue(Short value) {
        this.value = value;
    }
    
    @Override
    public ReturnableObject delegate() {
        return new ShortStorage(value);
    }
}
