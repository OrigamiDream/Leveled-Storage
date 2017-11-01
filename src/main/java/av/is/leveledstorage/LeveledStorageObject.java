package av.is.leveledstorage;

import av.is.leveledstorage.pool.ThreadPool;
import av.is.leveledstorage.settings.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 다단계 Storage 저장이 가능케 하는 것으로서 LeveledStorage 안에 또 하나의 LeveledStorage가 존재하게 할 수 있습니다.
 *
 * {@link #read(DataInputStream)} 에서부터 하나의 Storage를 읽어내며,
 * {@link #readEach(int, DataInputStream)}를 이용하여 개별 {@link StorageObject}를 읽어냅니다.
 *
 * {@link #read(DataInputStream)} 와 {@link #readEach(int, DataInputStream)}도 마찬가지입니다.
*/
public abstract class LeveledStorageObject<T extends StorageObject> implements StorageObject<DataInputStream, DataOutputStream> {
    
    protected int layers;
    protected final List<List<T>> elements = new ArrayList<>();
    protected List<File> divisionByFiles = new ArrayList<>();
    
    protected final Configuration configuration;
    protected ThreadPool pool;
    protected File directory;
    
    protected Class<T> storageType;
    protected String storageName;
    
    protected Logger logger;
    
    public LeveledStorageObject(Class<T> storageType) {
        this.logger = Logger.getLogger("Leveled Storage");
        this.configuration = new Configuration();
        this.directory = new File("C:/");
        
        if(storageType != null) {
            this.storageType = storageType;
            this.storageName = storageType.getSimpleName();
        }
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public String getStorageName() {
        return storageName;
    }
    
    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public int getLayers() {
        return layers;
    }
    
    public List<List<T>> getElements() {
        return new ArrayList<List<T>>(elements);
    }
    
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    /**
     * level에 저장된 데이터 하나를 읽어냅니다.
     *
     * @param input 을 이용하여 데이터를 읽어낼 수 있습니다.
     * @return T 는 {@link StorageObject}를 상속한 객체이며,
     *         {@link LeveledStorage} 에서는 Constructor을 호출하지 않고 생성한 객체에 데이터를 삽입합니다.
     */
    public abstract T readEach(int level, DataInputStream input) throws IOException;
    
    /**
     * level에 데이터를 저장합니다.
     *
     * @param obj 에 저장된 데이터를
     * @param output 에 저장합니다.
     */
    public abstract void writeEach(int level, T obj, DataOutputStream output) throws IOException;
    
    /**
     *
     * 객체로 생성될 데이터를 읽어옵니다.
     *
     * 1. 분할저장 여부를 가져옵니다.
     * 2. 전체 레이어 개수를 가져옵니다.
     *
     * 3-1.
     *   분할저장이 되어있는 경우, 레벨과 그에 대응하는 파일을 찾아 읽어옵니다.
     *   하위 파일은 레이어별로 저장이 되어있습니다.
     *
     * 3-2.
     *   분할저장이 되어있지 않은 경우, 해당 파일에 저장된 데이터를 읽어옵니다.
     *
     * @param input 에 저장된 데이터를 객체로 읽어옵니다.
     */
    @Override
    public void read(DataInputStream input) throws IOException {
        try {
            this.storageType = (Class<T>) Class.forName(input.readUTF());
            this.configuration.divideFiles = input.readBoolean();
            this.layers = input.readInt();
            
            this.divisionByFiles = new ArrayList<>(layers);
            for(int i = 0; i < layers; i++) {
                divisionByFiles.add(null);
            }
            
            if(configuration.divideFiles) {
                for(int i = 0; i < layers; i++) {
                    int level = input.readInt();
                    File file = new File(input.readUTF());
                    if(!file.exists()) {
                        throw new FileNotFoundException(input.readUTF() + " cannot be found in layer " + level + ".");
                    }
                    divisionByFiles.set(level, file);
                    
                    DataInputStream divisionInput = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                    int size = divisionInput.readInt();
            
                    elements.add(new ArrayList<>(size));
            
                    for(int j = 0; j < size; j++) {
                        elements.get(level).add(readEach(level, divisionInput));
                    }
                }
            } else {
                for(int i = 0; i < layers; i++) {
                    int level = input.readInt();
                    int size = input.readInt();
            
                    elements.add(new ArrayList<>(size));
            
                    for(int j = 0; j < size; j++) {
                        elements.get(level).add(readEach(level, input));
                    }
                }
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeUTF(storageType.getCanonicalName());
        output.writeBoolean(configuration.divideFiles);
        output.writeInt(layers);
        if(configuration.divideFiles) {
            for(int i = 0; i < layers; i++) {
                File file;
                if(divisionByFiles.contains(i)) {
                    file = divisionByFiles.get(i);
                } else {
                    file = new File(directory, getStorageName() + "-" + i + ".ls");
                }
                output.writeInt(i);
                output.writeUTF(file.getAbsolutePath());
                
                List<T> elements = this.elements.get(i);
                
                DataOutputStream divisionOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                divisionOutput.writeInt(elements.size());
                
                for(int j = 0; j < elements.size(); j++) {
                    writeEach(i, elements.get(j), divisionOutput);
                }
                divisionOutput.flush();
                divisionOutput.close();
            }
        } else {
            for(int i = 0; i < layers; i++) {
                output.writeInt(i);
                
                List<T> elements = this.elements.get(i);
                output.writeInt(elements.size());
        
                for(int j = 0; j < elements.size(); j++) {
                    writeEach(i, elements.get(j), output);
                }
            }
        }
    }
}
