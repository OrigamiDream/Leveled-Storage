package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.storages.IntStorage;
import av.is.leveledstorage.storages.bukkit.enchantments.EnchantmentStorage;
import av.is.leveledstorage.storages.java.HashMapStorage;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class EnchantmentStorageMetaStorage extends ItemMetaStorage<EnchantmentStorageMeta> {
    
    public EnchantmentStorageMetaStorage(EnchantmentStorageMeta meta) {
        super(meta);
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        
        HashMapStorage<EnchantmentStorage, IntStorage> enchantStorage = new HashMapStorage<>(null);
        enchantStorage.read(input);
        for(Map.Entry<EnchantmentStorage, IntStorage> entry : enchantStorage.getValue().entrySet()) {
            getValue().addStoredEnchant(entry.getKey().getValue(), entry.getValue().getValue(), true);
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        super.write(output);
        
        Map<EnchantmentStorage, IntStorage> storages = new HashMap<>();
        getValue().getStoredEnchants().forEach((enchantment, level) -> storages.put(new EnchantmentStorage(enchantment), new IntStorage(level)));
        HashMapStorage<EnchantmentStorage, IntStorage> enchantStorage = new HashMapStorage<EnchantmentStorage, IntStorage>(storages);
        enchantStorage.write(output);
    }
}
