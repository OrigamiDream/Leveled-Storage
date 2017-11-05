package av.is.leveledstorage.storages.bukkit.block.banner;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class PatternStorage implements DataStorageObject<Pattern> {
    
    private Pattern pattern;
    
    public PatternStorage(Pattern pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public Pattern getValue() {
        return pattern;
    }
    
    @Override
    public void setValue(Pattern value) {
        pattern = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new PatternStorage(new Pattern(pattern.getColor(), pattern.getPattern()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        DyeColor color = DyeColor.getByDyeData(input.readByte());
        PatternType patternType = PatternType.getByIdentifier(input.readUTF());
        
        setValue(new Pattern(color, patternType));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeByte(pattern.getColor().getDyeData());
        output.writeUTF(pattern.getPattern().getIdentifier());
    }
}
