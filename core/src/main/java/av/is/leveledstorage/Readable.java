package av.is.leveledstorage;

import java.io.IOException;
import java.io.InputStream;

public interface Readable<E extends InputStream> {
    
    void read(final E input) throws IOException;
    
}
