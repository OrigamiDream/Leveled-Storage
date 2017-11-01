package av.is.leveledstorage.settings;

import av.is.leveledstorage.ReturnableObject;
import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.tags.BooleanStorage;
import av.is.leveledstorage.tags.IntStorage;
import av.is.leveledstorage.tags.LongStorage;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Leveled Storage에서 사용되는 세부 옵션입니다.
 * 기본값은 {@link #SETTINGS}의 value값이며,
 *
 * {@link Configuration} 에서 설정을 변경할 수 있습니다.
 */
public class SettingRegistry {
    
    public static final Map<String, ReturnableObject> SETTINGS = new HashMap<>();
    static {
        SETTINGS.put("allowSaving", new BooleanStorage(true));
        SETTINGS.put("allowTransition", new BooleanStorage(true));
        SETTINGS.put("asyncSave", new BooleanStorage(true));
        SETTINGS.put("asyncTransition", new BooleanStorage(true));
        SETTINGS.put("renewalWhenQuery", new BooleanStorage(true));
        SETTINGS.put("divideFiles", new BooleanStorage(false));
        SETTINGS.put("useThreadPool", new BooleanStorage(false));
        SETTINGS.put("threadPoolInterval", new LongStorage(50L));
        SETTINGS.put("threadPoolCount", new IntStorage(5));
    }
}
