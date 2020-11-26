package am.bdgexame.bancapplication.util;

import java.util.Collection;
import java.util.Map;

public class Validator {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
