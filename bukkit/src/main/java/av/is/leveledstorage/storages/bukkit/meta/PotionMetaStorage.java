package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.bukkit.potion.PotionDataStorage;
import av.is.leveledstorage.storages.bukkit.potion.PotionEffectStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class PotionMetaStorage extends ItemMetaStorage<PotionMeta> {
    
    public PotionMetaStorage(PotionMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        boolean hasBasePotionData = input.readBoolean();
        if(hasBasePotionData) {
            PotionDataStorage storage = new PotionDataStorage(null);
            storage.read(input);
            getValue().setBasePotionData(storage.getValue());
        }
        
        boolean hasCustomEffets = input.readBoolean();
        if(hasCustomEffets) {
            ArrayListStorage<PotionEffectStorage> storage = new ArrayListStorage<>(null);
            storage.read(input);
            storage.getValue().stream().map(PotionEffectStorage::getValue).forEach(effect -> getValue().addCustomEffect(effect, true));
        }
        
        boolean hasColor = input.readBoolean();
        if(hasColor) {
            getValue().setColor(Color.fromRGB(input.readInt(), input.readInt(), input.readInt()));
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        output.writeBoolean(getValue().getBasePotionData() != null);
        if(getValue().getBasePotionData() != null) {
            PotionDataStorage storage = new PotionDataStorage(getValue().getBasePotionData());
            storage.write(output);
        }
        
        output.writeBoolean(getValue().getCustomEffects() != null);
        if(getValue().getCustomEffects() != null) {
            ArrayListStorage<PotionEffectStorage> storage = new ArrayListStorage<>(getValue().getCustomEffects().stream().map(PotionEffectStorage::new).collect(Collectors.toList()));
            storage.write(output);
        }
        
        output.writeBoolean(getValue().hasColor());
        if(getValue().hasColor()) {
            output.writeInt(getValue().getColor().getRed());
            output.writeInt(getValue().getColor().getGreen());
            output.writeInt(getValue().getColor().getBlue());
        }
    }
}
