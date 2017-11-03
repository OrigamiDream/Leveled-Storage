package av.is.leveledstorage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Avis Network on 2017-11-04.
 */
public interface EmptyStorageObject<I extends InputStream, O extends OutputStream> extends StorageObject<I, O, Object> {
    
    @Override
    default Object getValue() {
        return null;
    }
    
    @Override
    default void setValue(Object value) {
    
    }
    
}
