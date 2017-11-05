package av.is.leveledstorage;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by Avis Network on 2017-11-04.
 */
public class Storages {
    
    private Storages() {
    }
    
    public static <T> Optional<T> createEmptyObject(Class<T> clazz) {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        Optional<T> optional;
        try {
            Constructor objDef = Object.class.getDeclaredConstructor();
            Constructor constr = factory.newConstructorForSerialization(clazz, objDef);
            optional = Optional.ofNullable(clazz.cast(constr.newInstance()));
        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            optional = Optional.empty();
        }
        return optional;
    }
}
