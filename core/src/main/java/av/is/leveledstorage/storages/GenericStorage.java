package av.is.leveledstorage.storages;

import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.Storages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Avis Network on 2017-11-04.
 *
 * 제너릭을 사용하는 클래스들을 편리하게 저장하기 위한 클래스입니다.
 *
 * 기본적으로 {@link #generics()} 에 의거하여 Generic의 개수를 먼저 구하고,
 * 그 만큼 StorageObject로 반환하여 본 클래스의 제너릭으로 캐스트합니다.
 *
 */
public abstract class GenericStorage<V> implements StorageObject<DataInputStream, DataOutputStream, V> {
    
    private StorageObject[] generics;
    
    @Override
    public void read(DataInputStream input) throws IOException {
        int generics = input.readInt();
        Class[] generic = new Class[generics()];
        try {
            for(int i = 0; i < generics; i++) {
                generic[i] = Class.forName(input.readUTF());
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        StorageObject[] objects = new StorageObject[generics];
        for(int i = 0; i < generics; i++) {
            Optional<StorageObject> object = Storages.createEmptyObject(generic[generics]);
            if(!object.isPresent()) {
                throw new IllegalArgumentException("Unknown generic type: " + generic[generics] + " cannot be found.");
            }
            objects[i] = object.get();
        }
        this.generics = objects;
        readGeneric(input, objects);
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(generics());
        for(StorageObject generic : generics) {
            output.writeUTF(generic.getClass().getName());
        }
        writeGeneric(output);
    }
    
    public abstract int generics();
    
    public abstract void readGeneric(DataInputStream input, StorageObject... generic) throws IOException;
    
    public abstract void writeGeneric(DataOutputStream output) throws IOException;
}
