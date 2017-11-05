package av.is.leveledstorage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * LeveledStorage에서 중요하게 작용하는 레벨 당 Storage입니다.
 * 이곳에서 {@link StorageObject}를 상속한 객체를 저장할 수 있습니다.
 */
public class Storage<T extends StorageObject> implements Writable<DataOutputStream> {
    
    public Storage child;
    
    private List<T> objects = new ArrayList<>();
    
    public void setChild(Storage child) {
        this.child = child;
    }
    
    public void transition(List<T> objects) {
        if(child != null) {
            child.transition(this.objects);
            this.objects.clear();
        }
        this.objects.addAll(objects);
    }
    
    public void add(T obj) {
        objects.add(obj);
    }
    
    public List<T> getObjects() {
        return objects;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        getObjects().forEach(obj -> builder.append(obj.toString()).append(", "));
        if(getObjects().size() > 0) {
            builder.setLength(builder.length() - 2);
        }
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        for(T obj : objects) {
            obj.write(output);
        }
    }
}
