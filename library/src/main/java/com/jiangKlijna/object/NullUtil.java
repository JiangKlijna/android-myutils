package com.jiangKlijna.object;

import java.util.Collection;
import java.util.Map;

/**
 * NullUtil
 * Author: jiangKlijna
 */
public class NullUtil {

    private NullUtil() {
    }

    /**
     * @return isEmpty or isNull
     */
    public static final boolean em(String str) {
        return null == str || str.isEmpty();
    }

    /**
     * @return isEmpty or isNull
     */
    public static final boolean em(Collection c) {
        return null == c || c.isEmpty();
    }

    /**
     * @return isEmpty or isNull
     */
    public static final boolean em(Map m) {
        return null == m || m.isEmpty();
    }

    /**
     * @return equals
     */
    public static final boolean eq(Object obj1, Object obj2) {
        return obj1 == obj2 || obj1.equals(obj2);
    }

    /**
     * @return isNull
     */
    public static final boolean isN(Object obj) {
        return obj == null;
    }

}
