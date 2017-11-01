package av.is.leveledstorage.tags;

import av.is.leveledstorage.ReturnableObject;
import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringStorage implements ReturnableObject<DataInputStream, DataOutputStream, String> {
    
    private String value;
    
    public StringStorage(String value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readUTF();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeUTF(value);
    }
    
    @Override
    public String toString() {
        return getValue();
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public ReturnableObject delegate() {
        return new StringStorage(value);
    }
}
