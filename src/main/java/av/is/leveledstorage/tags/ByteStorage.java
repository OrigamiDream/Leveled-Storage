package av.is.leveledstorage.tags;

import av.is.leveledstorage.ReturnableObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class ByteStorage implements ReturnableObject<DataInputStream, DataOutputStream, Byte> {
    
    private byte value;
    
    public ByteStorage(byte value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readByte();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeByte(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Byte getValue() {
        return value;
    }
    
    @Override
    public void setValue(Byte value) {
        this.value = value;
    }
    
    @Override
    public ReturnableObject delegate() {
        return new ByteStorage(value);
    }
}
