package av.is.leveledstorage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 강화된 스토리지 설정을 위한 인터페이스입니다.
 *
 * 값을 반환하고, 설정할 수 있습니다.
 *
 * 기본적으로 주어지는 스토리지만이 아닌, 커스터마이징된 스토리지도 설정으로서 저장/로드가 가능합니다.
 */
public interface ReturnableObject<I extends InputStream, O extends OutputStream, V> extends StorageObject<I, O> {
    
    V getValue();
    
    void setValue(V value);
    
    ReturnableObject delegate();
    
}
