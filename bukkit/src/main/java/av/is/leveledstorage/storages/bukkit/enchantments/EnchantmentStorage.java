package av.is.leveledstorage.storages.bukkit.enchantments;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import org.bukkit.enchantments.Enchantment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class EnchantmentStorage implements DataStorageObject<Enchantment> {
    
    private Enchantment enchantment;
    
    public EnchantmentStorage(Enchantment enchantment) {
        this.enchantment = enchantment;
    }
    
    @Override
    public Enchantment getValue() {
        return enchantment;
    }
    
    @Override
    public void setValue(Enchantment value) {
        enchantment = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new EnchantmentStorage(enchantment);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        setValue(Enchantment.getById(input.readInt()));
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(enchantment.getId());
    }
}
