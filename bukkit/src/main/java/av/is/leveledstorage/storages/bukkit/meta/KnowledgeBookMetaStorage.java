package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.bukkit.NamespacedKeyStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class KnowledgeBookMetaStorage extends ItemMetaStorage<KnowledgeBookMeta> {
    
    public KnowledgeBookMetaStorage(KnowledgeBookMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasRecipes = input.readBoolean();
        if(hasRecipes) {
            ArrayListStorage<NamespacedKeyStorage> storage = new ArrayListStorage<>(null);
            storage.read(input);
            getValue().setRecipes(storage.getValue().stream().map(NamespacedKeyStorage::getValue).collect(Collectors.toList()));
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().hasRecipes());
        if(getValue().hasRecipes()) {
            ArrayListStorage<NamespacedKeyStorage> storage = new ArrayListStorage<>(getValue().getRecipes().stream().map(NamespacedKeyStorage::new).collect(Collectors.toList()));
            storage.write(output);
        }
    }
}
