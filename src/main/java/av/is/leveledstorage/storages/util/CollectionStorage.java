package av.is.leveledstorage.storages.util;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.GenericStorage;

import java.util.Collection;

/**
 * Created by Avis Network on 2017-11-04.
 *
 * 제네릭을 하나만 가지고 있는 {@link Collection<T>} 를 읽어들입니다.
 * {@link Collection<T>} 을 상속하는 다른 클래스들을 읽고 저장하기 위한 중심 클래스입니다
 *
 */
public abstract class CollectionStorage<T extends StorageObject, C extends Collection<T>> extends GenericStorage<C> {
    
    private C collection;
    
    public CollectionStorage(C collection) {
        this.collection = collection;
    }
    
    @Override
    public C getValue() {
        return collection;
    }
    
    @Override
    public void setValue(C value) {
        collection = value;
    }
    
    /**
     * @return {@link Collection<T>} 는 한 개(T)의 제네릭만을 가지고 있기 때문에, 1을 반환합니다.
     */
    @Override
    public int generics() {
        return 1;
    }
}
