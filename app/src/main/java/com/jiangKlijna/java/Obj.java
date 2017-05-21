package com.jiangKlijna.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * object Util
 * Author: com.jiangKlijna
 */
public class Obj {

    /**
     * @param src  源对象
     * @param desc 目标对象
     */
    public static <T> void copyobj(final T src, final T desc) throws Exception {
        copyobj(src, desc, src.getClass(), desc.getClass());
    }

    private static <T> void copyobj(T src, T desc, Class<?> srcclass, Class<?> descclass) throws Exception {
        if (srcclass == null || descclass == null) {
            return;
        }
        for (Field f : srcclass.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            Field descf = descclass.getDeclaredField(f.getName());
            descf.setAccessible(true);
            descf.set(desc, f.get(src));
        }
        copyobj(src, desc, srcclass.getSuperclass(), descclass.getSuperclass());
    }

    /**
     * @param src clone一个src对象
     * @param <T> 此类必须有空构造
     */
    public static <T> T cloneobj(final T src) throws Exception {
        Constructor constructor = src.getClass().getDeclaredConstructor();
        constructor.setAccessible(true);
        T desc = (T) constructor.newInstance();
        copyobj(src, desc);
        return desc;
    }
}
