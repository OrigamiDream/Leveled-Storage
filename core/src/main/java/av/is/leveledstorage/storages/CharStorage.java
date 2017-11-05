package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CharStorage implements StorageObject<DataInputStream, DataOutputStream, Character> {
    
    private char value;
    
    public CharStorage(char value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readChar();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeChar(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Character getValue() {
        return value;
    }
    
    @Override
    public void setValue(Character value) {
        this.value = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new CharStorage(value);
    }
}
