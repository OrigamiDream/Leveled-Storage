package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.bukkit.block.banner.PatternStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.BannerMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class BannerMetaStorage extends ItemMetaStorage<BannerMeta> {
    
    public BannerMetaStorage(BannerMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasBaseColor = input.readBoolean();
        if(hasBaseColor) {
            getValue().setBaseColor(DyeColor.getByDyeData(input.readByte()));
        }
        
        ArrayListStorage<PatternStorage> patternStorage = new ArrayListStorage<>(null);
        patternStorage.read(input);
        
        getValue().setPatterns(patternStorage.getValue().stream().map(PatternStorage::getValue).collect(Collectors.toList()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().getBaseColor() != null);
        if(getValue().getBaseColor() != null) {
            output.writeByte(getValue().getBaseColor().getDyeData());
        }
        
        ArrayListStorage<PatternStorage> patternStorage = new ArrayListStorage<>(getValue().getPatterns().stream().map(PatternStorage::new).collect(Collectors.toList()));
        patternStorage.write(output);
    }
}
