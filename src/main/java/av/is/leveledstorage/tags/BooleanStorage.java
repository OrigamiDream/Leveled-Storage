package av.is.leveledstorage.tags;

import av.is.leveledstorage.ReturnableObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class BooleanStorage implements ReturnableObject<DataInputStream, DataOutputStream, Boolean> {
    
    private boolean value;
    
    public BooleanStorage(boolean value) {
        this.value = value;
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        value = input.readBoolean();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeBoolean(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public Boolean getValue() {
        return value;
    }
    
    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
    
    @Override
    public ReturnableObject delegate() {
        return new BooleanStorage(value);
    }
}
