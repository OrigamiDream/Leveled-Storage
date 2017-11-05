package av.is.leveledstorage.storages.bukkit.inventory;

import av.is.leveledstorage.DataStorageObject;
import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.storages.bukkit.meta.ItemMetaStorage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Avis Network on 2017-11-05.
 */
public class ItemStackStorage implements DataStorageObject<ItemStack> {
    
    private ItemStack itemStack;
    
    public ItemStackStorage(Material material, int amount, short damage, Byte data) {
        this(new ItemStack(material, amount, damage, data));
    }
    
    public ItemStackStorage(Material material, int amount, short damage) {
        this(new ItemStack(material, amount, damage));
    }
    
    public ItemStackStorage(Material material, int amount) {
        this(new ItemStack(material, amount));
    }
    
    public ItemStackStorage(Material material) {
        this(new ItemStack(material));
    }
    
    public ItemStackStorage(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    
    @Override
    public ItemStack getValue() {
        return itemStack;
    }
    
    @Override
    public void setValue(ItemStack value) {
        itemStack = value;
    }
    
    @Override
    public StorageObject delegate() {
        return new ItemStackStorage(getValue().clone());
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        int material = input.readInt();
        int amount = input.readInt();
        short damage = input.readShort();
        
        setValue(new ItemStack(material, amount, damage));
        
        ItemMeta original = getValue().getItemMeta();
        
        Class<ItemMetaStorage> storageType = ItemMetaStorage.class;
        for(Class<? extends ItemMeta> storage : ItemMetaStorage.ITEM_META_REGISTRY.keySet()) {
            if(storage.isInstance(original)) {
                storageType = (Class<ItemMetaStorage>) ItemMetaStorage.ITEM_META_REGISTRY.get(storage);
            }
        }
        
        try {
            ItemMetaStorage storage = storageType.getConstructor(ItemMeta.class).newInstance(original);
            storage.read(input);
            getValue().setItemMeta(storage.getValue());
        } catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(getValue().getTypeId());
        output.writeInt(getValue().getAmount());
        output.writeInt(getValue().getDurability());
    
        ItemMeta original = getValue().getItemMeta();
    
        Class<ItemMetaStorage> storageType = ItemMetaStorage.class;
        for(Class<? extends ItemMeta> storage : ItemMetaStorage.ITEM_META_REGISTRY.keySet()) {
            if(storage.isInstance(original)) {
                storageType = (Class<ItemMetaStorage>) ItemMetaStorage.ITEM_META_REGISTRY.get(storage);
            }
        }
    
        try {
            ItemMetaStorage storage = storageType.getConstructor(ItemMeta.class).newInstance(original);
            storage.write(output);
        } catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
