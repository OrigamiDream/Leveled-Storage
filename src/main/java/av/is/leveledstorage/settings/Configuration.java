package av.is.leveledstorage.settings;

import av.is.leveledstorage.EmptyStorageObject;
import av.is.leveledstorage.Readable;
import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.Writable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 스토리지 설정
 *
 * 언제든지 내용이 추가될 수 있는 설정 사항이기 때문에,
 * 호환성을 고려하여, 이 클래스는 모든 Storage가 읽어진 마지막에 리드됩니다.
 *
 * 이 클래스의 필드들은 전부 리플렉션으로 읽어들어지며, 그 이름이 저장됩니다.
 * 그렇기에, 설정 파일에 읽어지는 것 외의 목적으로 필드를 추가하는것은 비권장됩니다.
 *
 */
public class Configuration implements EmptyStorageObject<DataInputStream, DataOutputStream> {
    
    /**
     * 데이터를 파일로 저장하는 기능을 활성화합니다.
     */
    public boolean allowSaving = true;
    
    /**
     * 데이터의 레벨의 변동 가능 여부를 결정합니다.
     */
    public boolean allowTransition = true;
    
    /**
     * 비동기로 저장하여 다른 스레드가 멈추는것을 막습니다.
     */
    public boolean asyncSave = true;
    
    /**
     * 데이터의 레벨이 상승될 때 비동기로 이행합니다.
     */
    public boolean asyncTransition = true;
    
    /**
     * contain(), get() 과 같은 쿼리 메소드를 사용할 시에 해당 데이터들을 갱신합니다.
     */
    public boolean renewalWhenQuery = true;
    
    /**
     * 파일을 저장할 때, 분할하여 저장할지, 단일로 저장할지 설정합니다.
     */
    public boolean divideFiles = false;
    
    /**
     * 스레드 풀을 사용하여, 저장/이동할 때 마다 스레드를 생성하는 것보다 적은 리소스를 사용합니다.
     * 큰 데이터를 다룰 시에는 지연으로 인해 비권장합니다.
     */
    public boolean useThreadPool = false;
    
    /**
     * 스레드 풀을 사용할 시에, 스레드 풀의 반복 주기를 설정합니다.
     */
    public long threadPoolInterval = 50L;
    
    /**
     * 스레드 풀을 사용할 때, 생성할 스레드들의 개수를 설정합니다.
     */
    public int threadPoolCount = 5;
    
    
    @Override
    public void read(DataInputStream input) throws IOException {
        for(int i = 0; i < SettingRegistry.SETTINGS.size(); i++) {
            String setting = input.readUTF();
            if(SettingRegistry.SETTINGS.containsKey(setting)) {
                try {
                    Field field = Configuration.class.getDeclaredField(setting);
                    field.setAccessible(true);
                    
                    StorageObject storageObject = SettingRegistry.SETTINGS.get(setting).delegate();
                    storageObject.read(input);
                    field.set(this, storageObject.getValue());
                } catch(NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        for(Field field : Configuration.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String setting = field.getName();
                if(!SettingRegistry.SETTINGS.containsKey(setting)) {
                    continue;
                }
                output.writeUTF(setting);
                
                StorageObject storageObject = SettingRegistry.SETTINGS.get(setting).delegate();
                storageObject.setValue(field.get(this));
                storageObject.write(output);
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public Configuration delegate() {
        Configuration configuration = new Configuration();
        for(Field field : Configuration.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String setting = field.getName();
                if(!SettingRegistry.SETTINGS.containsKey(setting)) {
                    continue;
                }
                Object original = field.get(this);
                field.set(configuration, original);
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return configuration;
    }
}
