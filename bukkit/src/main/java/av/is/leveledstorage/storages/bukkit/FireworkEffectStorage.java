package av.is.leveledstorage.storages.bukkit;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class FireworkEffectStorage implements DataStorageObject<FireworkEffect> {
    
    private FireworkEffect fireworkEffect;
    
    public FireworkEffectStorage(FireworkEffect fireworkEffect) {
        this.fireworkEffect = fireworkEffect;
    }
    
    @Override
    public FireworkEffect getValue() {
        return fireworkEffect;
    }
    
    @Override
    public void setValue(FireworkEffect value) {
        fireworkEffect = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new FireworkEffectStorage(
                FireworkEffect.builder()
                        .flicker(fireworkEffect.hasFlicker())
                        .trail(fireworkEffect.hasTrail())
                        .withColor(fireworkEffect.getColors())
                        .withFade(fireworkEffect.getFadeColors())
                        .with(fireworkEffect.getType())
                        .build()
        );
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        
        builder.flicker(input.readBoolean());
        builder.trail(input.readBoolean());
        
        int len = input.readInt();
        for(int i = 0; i < len; i++) {
            builder.withColor(Color.fromRGB(input.readInt(), input.readInt(), input.readInt()));
        }
        
        len = input.readInt();
        for(int i = 0; i < len; i++) {
            builder.withFade(Color.fromRGB(input.readInt(), input.readInt(), input.readInt()));
        }
        
        builder.with(FireworkEffect.Type.valueOf(input.readUTF()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeBoolean(fireworkEffect.hasFlicker());
        output.writeBoolean(fireworkEffect.hasTrail());
        output.writeInt(fireworkEffect.getColors().size());
        for(Color color : fireworkEffect.getColors()) {
            output.writeInt(color.getRed());
            output.writeInt(color.getGreen());
            output.writeInt(color.getBlue());
        }
        output.writeInt(fireworkEffect.getFadeColors().size());
        for(Color color : fireworkEffect.getFadeColors()) {
            output.writeInt(color.getRed());
            output.writeInt(color.getGreen());
            output.writeInt(color.getBlue());
        }
        output.writeUTF(fireworkEffect.getType().toString());
    }
}
