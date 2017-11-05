package av.is.leveledstorage.storages.java;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.GenericStorage;

import java.util.Map;

/**
 * Created by Avis Network on 2017-11-04.
 *
 * 제네릭을 두 개 가진 {@link Map<K, V>}를 읽어들입니다.
 * {@link Map<K, V>} 을 상속하는 다른 클래스들을 읽고 저장하기 위한 중심 클래스입니다
 */
public abstract class MapStorage<K extends StorageObject, V extends StorageObject, M extends Map<K, V>> extends GenericStorage<M> {
    
    private M map;
    
    public MapStorage(M map) {
        this.map = map;
    }
    
    @Override
    public M getValue() {
        return map;
    }
    
    @Override
    public void setValue(M value) {
        map = value;
    }
    
    /**
     * @return {@link Map<K, V>} 는 두 개(K, V)의 제네릭만을 가지고 있기 때문에, 2를 반환합니다.
     */
    @Override
    public int generics() {
        return 2;
    }
}
