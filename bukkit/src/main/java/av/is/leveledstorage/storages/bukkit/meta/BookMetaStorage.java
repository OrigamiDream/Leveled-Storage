package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.StringStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import org.bukkit.inventory.meta.BookMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class BookMetaStorage extends ItemMetaStorage<BookMeta> {
    
    public BookMetaStorage(BookMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasTitle = input.readBoolean();
        if(hasTitle) {
            getValue().setTitle(input.readUTF());
        }
        
        boolean hasAuthor = input.readBoolean();
        if(hasAuthor) {
            getValue().setAuthor(input.readUTF());
        }
        
        boolean hasGeneration = input.readBoolean();
        if(hasGeneration) {
            getValue().setGeneration(BookMeta.Generation.valueOf(input.readUTF()));
        }
        
        boolean hasPages = input.readBoolean();
        if(hasPages) {
            ArrayListStorage<StringStorage> pages = new ArrayListStorage<>(null);
            pages.read(input);
            
            getValue().setPages(pages.getValue().stream().map(StringStorage::getValue).collect(Collectors.toList()));
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().hasTitle());
        if(getValue().hasTitle()) {
            output.writeUTF(getValue().getTitle());
        }
        
        output.writeBoolean(getValue().hasAuthor());
        if(getValue().hasAuthor()) {
            output.writeUTF(getValue().getAuthor());
        }
        
        output.writeBoolean(getValue().hasGeneration());
        if(getValue().hasGeneration()) {
            output.writeUTF(getValue().getGeneration().toString());
        }
        
        output.writeBoolean(getValue().hasPages());
        if(getValue().hasPages()) {
            ArrayListStorage<StringStorage> pages = new ArrayListStorage<>(getValue().getPages().stream().map(StringStorage::new).collect(Collectors.toList()));
            pages.write(output);
        }
    }
}
