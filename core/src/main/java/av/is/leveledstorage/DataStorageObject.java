package av.is.leveledstorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by Avis Network on 2017-11-05.
 */
public interface DataStorageObject<V> extends StorageObject<DataInputStream, DataOutputStream, V> {
}
