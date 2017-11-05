package av.is.leveledstorage.storages.bukkit.potion;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class PotionDataStorage implements DataStorageObject<PotionData> {
    
    private PotionData potionData;
    
    public PotionDataStorage(PotionData potionData) {
        this.potionData = potionData;
    }
    
    @Override
    public PotionData getValue() {
        return potionData;
    }
    
    @Override
    public void setValue(PotionData value) {
        potionData = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new PotionDataStorage(new PotionData(potionData.getType(), potionData.isExtended(), potionData.isUpgraded()));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        PotionType type = PotionType.valueOf(input.readUTF());
        boolean extended = input.readBoolean();
        boolean upgraded = input.readBoolean();
        
        setValue(new PotionData(type, extended, upgraded));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeUTF(getValue().getType().toString());
        output.writeBoolean(getValue().isExtended());
        output.writeBoolean(getValue().isUpgraded());
    }
}
