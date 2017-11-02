package av.is.leveledstorage;

import av.is.leveledstorage.pool.ThreadPool;
import sun.reflect.ReflectionFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Leveled Storage
 *
 * 개발자: OrigamiDream
 *
 * 데이터를 갱신/사용함으로서 쌓이는 데이터를
 * 갱신된 순, 자주 사용되는 데이터 순으로 순차적으로 저장하는 스토리지입니다.
 *
 * 갱신된 데이터는 최상위 레이어로 이동하며, 최상위 레이어인만큼 로드 속도가 빠릅니다.
 * {@link #processSave()} 를 이용하여 데이터를 파일로 저장할 수 있으며,
 * {@link #processTransition()} 를 이용하여 모든 데이터의 레이어를 한 단계씩 내릴 수 있습니다.
 *
 */
public class LeveledStorage<T extends StorageObject> extends LeveledStorageObject<T> {
    
    private static final Object SAVING_LOCK = new Object();
    private static final Object LEVELS_LOCK = new Object();
    
    private boolean saving = false;
    
    private boolean transiting = false;
    
    private final List<List<T>> savingDeque = new ArrayList<>();
    private final List<Storage> levels = new ArrayList<>();
    
    public LeveledStorage(Class<T> storageType, int levels) {
        this(storageType);
        
        createStorages(levels);
        setupShutdown();
    }
    
    public LeveledStorage(Class<T> storageType) {
        super(storageType);
        
        setupShutdown();
    }
    
    public void setupShutdown() {
        Runtime.getRuntime().addShutdownHook(new LeveledStorageAdapter(this));
    }
    
    /**
     *
     * 가장 최근 레이어에 T 를 저장합니다.
     *
     * 만약 데이터가 이동중이거나, 저장중이라면
     * savingDeque에 넣어서 저장중인 데이터들이 변경되지 않도록 합니다.
     *
     */
    public void add(T obj) {
        add(obj, 0);
    }
    
    /**
     *
     * 데이터를 지정된 레이어에 저장합니다.
     *
     * 만약 데이터가 이동중이거나, 저장중이라면
     * savingDeque에 넣어서 저장중인 데이터들이 변경되지 않도록 합니다.
     *
     */
    public void add(T obj, int level) {
        if(transiting || saving) {
            synchronized(SAVING_LOCK) {
                savingDeque.get(level).add(obj);
            }
        } else {
            synchronized(LEVELS_LOCK) {
                levels.get(level).add(obj);
            }
        }
    }
    
    /**
     * 가장 최근 갱신된 데이터부터 차례대로 검색하여 데이터를 찾습니다.
     *
     * renewalWhenQuery 가 true일 경우, 이 메소드는 찾은 데이터와
     * 찾는 데이터가 일치할 시에 해당 데이터를 갱신합니다.
     */
    public boolean contains(T obj) {
        for(int i = 0; i < levels.size(); i++) {
            Storage storage = levels.get(i);
            if(storage.getObjects().contains(obj)) {
                if(configuration.renewalWhenQuery) {
                    outdate(storage, obj);
                }
                return true;
            }
        }
        return false;
    }
    
    private void outdate(Storage storage, T obj) {
        storage.getObjects().remove(obj);
        add(obj);
    }
    
    /**
     * 데이터들의 레벨 이동을 시작합니다.
     */
    public void processTransition() {
        transitionAll();
    }
    
    /**
     * 데이터들을 저장합니다.
     *
     * {@link #setDirectory(File)} 가 먼저 작동되어, 경로를 설정해야만 저장이 가능합니다.
     */
    public void processSave() {
        saveAll();
    }
    
    /**
     * 특정 레이어에 들어있는 데이터를 저장합니다.
     *
     * {@link #setDirectory(File)} 가 먼저 작동되어 경로가 지정되어있고 분할 저장이 활성화되어 있어야 저장이 가능합니다.
     *
     * @param layer
     */
    public void processSave(int layer) {
        saveLayer(layer);
    }
    
    public void createStorages(int levels) {
        if(pool == null) {
            pool = new ThreadPool(configuration);
        }
        this.layers = levels;
        for(int i = 0; i < levels; i++) {
            this.levels.add(new Storage());
            this.savingDeque.add(new ArrayList<>());
            this.elements.add(new ArrayList<>());
        }
        for(int i = 0; i < levels - 1; i++) {
            this.levels.get(i).setChild(this.levels.get(i + 1));
        }
    }
    
    private void transitionAll() {
        if(!configuration.allowTransition) {
            return;
        }
        transiting = true;
        pool.execute(configuration.asyncTransition, this::transition);
    }
    
    private void saveAll() {
        if(!configuration.allowSaving) {
            return;
        }
        saving = true;
        pool.execute(configuration.asyncSave, this::save);
    }
    
    private void saveLayer(int layer) {
        if(!configuration.allowSaving || !configuration.divideFiles) {
            return;
        }
        saving = true;
        pool.execute(configuration.asyncSave, () -> save(layer));
    }
    
    private void save() {
        if(directory == null) {
            logger.warning("Directory cannot be null. Saving storage is rejected.");
            return;
        }
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(directory, getStorageName() + ".ls"))));
            write(dos);
            dos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        saving = false;
        proceedDeque();
    }
    
    private void save(int layer) {
        if(directory == null) {
            logger.warning("Directory cannot be null. Saving storage is rejected.");
            return;
        }
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(directory, getStorageName() + ".ls"))));
            write(dos, layer);
            dos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        saving = false;
        proceedDeque();
    }
    
    /**
     *
     * 네트워크상으로 스토리지를 전송하기 위한 메소드입니다.
     * 분할저장은 불가능합니다.
     *
     * @param outputStream 에 스토리지가 저장됩니다.
     */
    public DataOutputStream writeStorage(DataOutputStream outputStream) throws IOException {
        boolean divide = configuration.divideFiles;
        
        configuration.divideFiles = false;
        write(outputStream);
        configuration.divideFiles = divide;
        
        return outputStream;
    }
    
    private void transition() {
        levels.get(0).transition(Collections.emptyList());
        transiting = false;
        proceedDeque();
    }
    
    private void proceedDeque() {
        synchronized(SAVING_LOCK) {
            for(int i = 0; i < savingDeque.size(); i++) {
                List<T> queue = savingDeque.get(i);
                
                while(!queue.isEmpty()) {
                    add(queue.remove(0), i);
                }
            }
        }
    }
    
    @Override
    public void read(DataInputStream input) throws IOException {
        super.read(input);
        configuration.read(input);
        createStorages(getLayers());
        
        List<List<T>> objects = getElements();
        for(int i = 0; i < objects.size(); i++) {
            List<T> storage = objects.get(i);
            
            int level = i;
            storage.forEach(db -> add(db, level));
        }
    }
    
    @Override
    public void write(DataOutputStream output) throws IOException {
        for(int i = 0; i < levels.size(); i++) {
            Storage storage = levels.get(i);
            
            elements.get(i).clear();
            elements.get(i).addAll(storage.getObjects());
        }
        super.write(output);
        configuration.write(output);
    }
    
    @Override
    public T readEach(int level, DataInputStream input) throws IOException {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        Optional<T> optional = Optional.empty();
        try {
            Constructor objDef = Object.class.getDeclaredConstructor();
            Constructor constr = factory.newConstructorForSerialization(storageType, objDef);
            optional = Optional.ofNullable(storageType.cast(constr.newInstance()));
        } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if(!optional.isPresent()) {
            throw new IllegalArgumentException("Failed to load storage '" + storageType.getSimpleName() + ".ls' from " + directory.toPath());
        }
        T storage = optional.get();
        storage.read(input);
        return storage;
    }
    
    @Override
    public void writeEach(int level, T obj, DataOutputStream output) throws IOException {
        obj.write(output);
    }
    
    /**
     * 아무것도 들어있지 않은 스토리지를 새로 만듭니다.
     *
     * @param classType 에 의해서 스토리지가 저장하는 오브젝트가 설정되며,
     * @param levels 로 스토리지의 총 레벨을 결정합니다.
     *
     * @return 새로운 스토리지를 반환합니다.
     */
    public static <T extends StorageObject> LeveledStorage<T> createEmptyStorage(Class<T> classType, int levels) {
        return new LeveledStorage<>(classType, levels);
    }
    
    /**
     * 지정된 경로에 저장된 스토리지를 불러옵니다.
     *
     * 불러온 파일이 분할 저장이 되어있을 시에는, 해당 파일들을 스토리지로서 불러옵니다.
     *
     * @param file 에 의해서 데이터가 저장된 파일을 불러옵니다.
     * @return 파일을 읽어들여, 저장된 스토리지를 반환합니다.
     */
    public static <T extends StorageObject> LeveledStorage<T> loadStorage(File file) {
        LeveledStorage storage = new LeveledStorage(null);
        try {
            storage.read(new DataInputStream(new BufferedInputStream(new FileInputStream(file))));
            return storage;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 받은 스트림에 저장된 스토리지 데이터를 불러옵니다.
     *
     * 분할 저장하도록 설정되어있는 데이터는 FileNotFoundException을 유발할 것입니다.
     *
     * @param inputStream 을 이용하여 서버나, 소켓 등을 이용하여도 쉽게 데이터를 주고받을 수 있습니다.
     * @return 스트림에 저장된 스토리지를 반환합니다.
     */
    public static <T extends StorageObject> LeveledStorage<T> loadStorage(InputStream inputStream) {
        LeveledStorage storage = new LeveledStorage(null);
        try {
            storage.read(new DataInputStream(inputStream));
            return storage;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void debug() {
        for(int i = 0; i < levels.size(); i++) {
            Storage storage = levels.get(i);
            
            System.out.println("Lv." + (i + 1) + storage.toString());
            System.out.println();
        }
    }
    
}
