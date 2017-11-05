package av.is.leveledstorage.storages.guava;

import av.is.leveledstorage.StorageObject;
import com.google.common.collect.ArrayTable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class ArrayTableStorage<R extends StorageObject, C extends StorageObject, V extends StorageObject> extends TableStorage<R, C, V, ArrayTable<R, C, V>> {
    
    public ArrayTableStorage(ArrayTable<R, C, V> table) {
        super(table);
    }
    
    @Override
    public int generics() {
        return 3;
    }
    
    @Override
    public void readGeneric(DataInputStream input, StorageObject... generic) throws IOException {
        int rowSize = input.readInt();
        for(int r = 0; r < rowSize; r++) {
            int colSize = input.readInt();
            R row = (R) generic[0].delegate();
            
            row.read(input);
            
            for(int c = 0; c < colSize; c++) {
                C column = (C) generic[1].delegate();
                V value = (V) generic[2].delegate();
                
                column.read(input);
                value.read(input);
                
                getValue().put(row, column, value);
            }
        }
    }
    
    @Override
    public void writeGeneric(DataOutputStream output) throws IOException {
        output.writeInt(getValue().size());
        for(R row : getValue().rowKeyList()) {
            Map<C, V> rows = getValue().row(row);
            output.writeInt(rows.size());
            
            row.write(output);
            
            for(C column : rows.keySet()) {
                V value = rows.get(column);
                
                column.write(output);
                value.write(output);
            }
        }
    }
    
    @Override
    public StorageObject delegate() {
        return new ArrayTableStorage(ArrayTable.create(getValue()));
    }
}
