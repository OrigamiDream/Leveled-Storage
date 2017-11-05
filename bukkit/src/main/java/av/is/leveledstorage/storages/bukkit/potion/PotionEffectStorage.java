package av.is.leveledstorage.storages.bukkit.potion;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class PotionEffectStorage implements DataStorageObject<PotionEffect> {
    
    private PotionEffect effect;
    
    public PotionEffectStorage(PotionEffect effect) {
        this.effect = effect;
    }
    
    @Override
    public PotionEffect getValue() {
        return effect;
    }
    
    @Override
    public void setValue(PotionEffect value) {
        effect = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new PotionEffectStorage(new PotionEffect(
                effect.getType(),
                effect.getDuration(), effect.getAmplifier(),
                effect.isAmbient(), effect.hasParticles(), effect.getColor()
        ));
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        PotionEffectType type = PotionEffectType.getById(input.readInt());
        int duration = input.readInt();
        int amplifier = input.readInt();
        boolean aimbient = input.readBoolean();
        boolean particles = input.readBoolean();
        int r = input.readInt();
        int g = input.readInt();
        int b = input.readInt();
        Color color = Color.fromRGB(r, g, b);
        
        setValue(new PotionEffect(
                type,
                duration, amplifier,
                aimbient, particles, color
        ));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(effect.getType().getId());
        output.writeInt(effect.getDuration());
        output.writeInt(effect.getAmplifier());
        output.writeBoolean(effect.isAmbient());
        output.writeBoolean(effect.hasParticles());
        output.writeInt(effect.getColor().getRed());
        output.writeInt(effect.getColor().getGreen());
        output.writeInt(effect.getColor().getBlue());
    }
}
