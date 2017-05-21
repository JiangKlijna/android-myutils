package com.jiangKlijna.kotlin

import java.lang.reflect.Constructor
import java.lang.reflect.Field

/**
 * object Util
 * Author: com.jiangKlijna
 */
object Obj {

    /**
     * @param src  源对象
     * *
     * @param desc 目标对象
     */
    @Throws(Exception::class)
    fun <T> copyobj(src: T, desc: T) {
        copyobj(src, desc, (src as Any).javaClass, (desc as Any).javaClass)
    }

    @Throws(Exception::class)
    private fun <T> copyobj(src: T, desc: T, srcclass: Class<*>?, descclass: Class<*>?) {
        if (srcclass == null || descclass == null) {
            return
        }
        for (f in srcclass.declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(f.modifiers)) {
                continue
            }
            f.isAccessible = true
            val descf = descclass.getDeclaredField(f.name)
            descf.isAccessible = true
            descf.set(desc, f.get(src))
        }
        copyobj(src, desc, srcclass.superclass, descclass.superclass)
    }

    /**
     * @param src clone一个src对象
     * *
     * @param <T> 此类必须有空构造
    </T> */
    @Throws(Exception::class)
    fun <T> cloneobj(src: T): T? {
        (src as Any).javaClass.getConstructor().let {
            it.isAccessible = true
            val desc = it.newInstance()
            copyobj(src, desc)
            return desc as? T?
        }
    }
}
