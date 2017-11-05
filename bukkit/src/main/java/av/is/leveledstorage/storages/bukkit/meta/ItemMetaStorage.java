package av.is.leveledstorage.storages.bukkit.meta;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.IntStorage;
import av.is.leveledstorage.storages.StringStorage;
import av.is.leveledstorage.storages.bukkit.enchantments.EnchantmentStorage;
import av.is.leveledstorage.storages.java.ArrayListStorage;
import av.is.leveledstorage.storages.java.HashMapStorage;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class ItemMetaStorage<T extends ItemMeta> implements DataStorageObject<T> {
    
    public static final Map<Class<? extends ItemMeta>, Class<? extends ItemMetaStorage>> ITEM_META_REGISTRY = new HashMap<>();
    static {
        ITEM_META_REGISTRY.put(LeatherArmorMeta.class, LeatherArmorMetaStorage.class);
        ITEM_META_REGISTRY.put(MapMeta.class, MapMetaStorage.class);
        ITEM_META_REGISTRY.put(SpawnEggMeta.class, SpawnEggMetaStorage.class);
        ITEM_META_REGISTRY.put(BookMeta.class, BookMetaStorage.class);
        ITEM_META_REGISTRY.put(SkullMeta.class, SkullMetaStorage.class);
        ITEM_META_REGISTRY.put(FireworkMeta.class, FireworkMetaStorage.class);
        ITEM_META_REGISTRY.put(EnchantmentStorageMeta.class, EnchantmentStorageMetaStorage.class);
        ITEM_META_REGISTRY.put(BannerMeta.class, BannerMetaStorage.class);
        ITEM_META_REGISTRY.put(KnowledgeBookMeta.class, KnowledgeBookMetaStorage.class);
    }
    
    private T meta;
    
    public ItemMetaStorage(T meta) {
        this.meta = meta;
    }
    
    @Override
    public T getValue() {
        return meta;
    }
    
    @Override
    public void setValue(T value) {
        meta = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new ItemMetaStorage(meta.clone());
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        
        boolean hasDisplayName = input.readBoolean();
        if(hasDisplayName) {
            getValue().setDisplayName(input.readUTF());
        }
        
        boolean hasLocalizedName = input.readBoolean();
        if(hasLocalizedName) {
            getValue().setLocalizedName(input.readUTF());
        }
        
        ArrayListStorage<StringStorage> loreStorage = new ArrayListStorage<StringStorage>(null);
        loreStorage.read(input);
        getValue().setLore(loreStorage.getValue().stream().map(StringStorage::getValue).collect(Collectors.toList()));
        
        
        HashMapStorage<EnchantmentStorage, IntStorage> enchantStorage = new HashMapStorage<>(null);
        enchantStorage.read(input);
        for(Map.Entry<EnchantmentStorage, IntStorage> entry : enchantStorage.getValue().entrySet()) {
            getValue().addEnchant(entry.getKey().getValue(), entry.getValue().getValue(), true);
        }
        
        int flagsLen = input.readInt();
        for(int i = 0; i < flagsLen; i++) {
            ItemFlag flag = ItemFlag.valueOf(input.readUTF());
    
            getValue().addItemFlags(flag);
        }
    
        getValue().setUnbreakable(input.readBoolean());
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeBoolean(getValue().hasDisplayName());
        if(getValue().hasDisplayName()) {
            output.writeUTF(getValue().getDisplayName());
        }
        
        output.writeBoolean(getValue().hasLocalizedName());
        if(getValue().hasLocalizedName()) {
            output.writeUTF(getValue().getLocalizedName());
        }
        
        ArrayListStorage<StringStorage> loreStorage = new ArrayListStorage<StringStorage>(getValue().hasLore() ? getValue().getLore().stream().map(StringStorage::new).collect(Collectors.toList()) : new ArrayList<>());
        loreStorage.write(output);
        
        output.writeInt(getValue().getEnchants().size());
        
        Map<EnchantmentStorage, IntStorage> storages = new HashMap<>();
        getValue().getEnchants().forEach((enchantment, level) -> storages.put(new EnchantmentStorage(enchantment), new IntStorage(level)));
        HashMapStorage<EnchantmentStorage, IntStorage> enchantStorage = new HashMapStorage<EnchantmentStorage, IntStorage>(storages);
        enchantStorage.write(output);
        
        output.writeInt(getValue().getItemFlags().size());
        for(ItemFlag flag : getValue().getItemFlags()) {
            output.writeUTF(flag.toString());
        }
        
        output.writeBoolean(getValue().isUnbreakable());
    }
}
