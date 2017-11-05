package av.is.leveledstorage;

import java.io.IOException;
import java.io.OutputStream;

public interface Writable<E extends OutputStream> {
    
    void write(final E output) throws IOException;
    
}
