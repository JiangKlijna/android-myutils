package com.jiangKlijna.kotlin

import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Administrator on 2016/7/14.
 */
class Beans protected constructor() : Serializable {

    enum class Type {
        Single, Prototype, Init, Clone, Custom
    }

    //存储产品
    @Transient private val products = ConcurrentHashMap<IKey<*, *>, IProduct<*>>()

    //    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    //    private final Lock r = rwl.readLock();
    //    private final Lock w = rwl.writeLock();

    //获得产品实例
    fun <I, T> getBean(iClass: Class<I>, tClass: Class<T>): I? {
        return getIProduct(iClass, tClass).onGetBean() as I
    }

    fun <T> getBean(tClass: Class<T>): T? {
        return getBean(tClass, tClass)
    }

    //regist_product parameter
    // @param iClass 产品返回值类型的class
    // @param tClass 产品本身类型的class
    // @param Type 产品的类型
    // @param IProduct 产品

    fun <I, T> regist_product(product: CustomProduct<I, T>) {
        regist_product(product.iClass, product.tClass, product)
    }

    fun regist_product(tClass: Class<*>) {
        regist_product(tClass, tClass, Type.Single)
    }

    fun regist_product(iClass: Class<*>, type: Type) {
        regist_product(iClass, iClass, type)
    }

    fun <T> regist_product(tClass: Class<T>, type: Type, product: T) {
        regist_product(tClass, tClass, type, product)
    }

    @JvmOverloads fun <I, T> regist_product(iClass: Class<I>, tClass: Class<T>, type: Type, product: T? = null) {
        when (type) {
            Beans.Type.Single -> regist_product(iClass, tClass, SingleProduct(tClass))
            Beans.Type.Prototype -> regist_product(iClass, tClass, PrototypeProduct(tClass))
            Beans.Type.Init -> regist_product(iClass, tClass, InitProduct(product))
            Beans.Type.Clone -> regist_product(iClass, tClass, CloneProduct(product))
        }
    }

    private fun <T> regist_product(iClass: Class<T>, product: IProduct<T>) {
        regist_product(iClass, iClass, product)
    }

    private fun <I, T> regist_product(iClass: Class<I>, tClass: Class<T>, product: IProduct<T>) {
        val iKey = IKey(iClass, tClass)
        if (null == getIProduct(iKey)) {
            products.put(iKey, product)
        } else {
            throw RuntimeException("Can not repeat the registration of product, Must first unregist_product().")
        }
    }

    //注销产品
    @JvmOverloads fun unregist_product(iClass: Class<*>, tClass: Class<*> = iClass) {
        getIProduct(iClass, tClass).let {
            it.onDestory()
            products.remove(IKey(iClass, tClass), it)
        }
    }

    fun destoryAllProduct() {
        for (p in products.values)
            p.onDestory()
        products.clear()
    }

    private fun getIProduct(iClass: Class<*>, tClass: Class<*> = iClass): IProduct<*> {
        return products[IKey(iClass, tClass)]!!
    }

    private fun getIProduct(iKey: IKey<*, *>): IProduct<*>? {
        return products[iKey]
    }

    //存放产品map的key
    private class IKey<I, T> constructor(private val iClass: Class<I>, private val tClass: Class<T>) : Comparable<IKey<*, *>>, Serializable {

        override fun hashCode(): Int {
            var result = 1
            result = (result shl 5) - 1 + System.identityHashCode(iClass)
            result = (result shl 5) - 1 + System.identityHashCode(tClass)
            return result
        }

        override fun toString(): String {
            return "IKey [iClass = " + iClass!!.toString() + ", tClass = " + tClass!!.toString() + "]"
        }

        override fun equals(o: Any?): Boolean {
            if (o is IKey<*, *>) {
                val another = o
                return equals(another.iClass, another.tClass)
            }
            return this === o
        }

        fun equals(iClass: Class<*>, tClass: Class<*>): Boolean {
            return this.iClass == iClass && this.tClass == tClass
        }

        override fun compareTo(iKey: IKey<*, *>): Int {
            return compareTo(iKey.iClass, iKey.tClass)
        }

        fun compareTo(iClass: Class<*>, tClass: Class<*>): Int {
            if (equals(iClass, tClass)) return 0
            val i = this.iClass!!.name.compareTo(iClass.name)
            return if (i != 0) i else i + this.tClass!!.name.compareTo(tClass.name)
        }
    }

    //存放产品map的value
    private interface IProduct<T> : Serializable {
        fun onGetBean(): T

        fun onDestory()

        fun type(): Type
    }

    //单例产品
    private class SingleProduct<T> constructor(private val bClass: Class<T>) : IProduct<T> {
        private var product: T? = null

        @Synchronized override fun onGetBean(): T {
            if (product == null) {
                try {
                    bClass.getDeclaredConstructor().let {
                        it.isAccessible = true
                        product = it.newInstance()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw RuntimeException(e)
                }

            }
            return product!!
        }

        override fun onDestory() {
            product = null
        }

        override fun type(): Type {
            return Type.Single
        }
    }

    //原型产品
    private class PrototypeProduct<T> constructor(private val bClass: Class<T>) : IProduct<T> {

        @Synchronized override fun onGetBean(): T {
            try {
                val c = bClass.getDeclaredConstructor()
                c.isAccessible = true
                return c.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException(e)
            }

        }

        override fun onDestory() {}

        override fun type(): Type {
            return Type.Prototype
        }
    }

    //初始化产品
    private class InitProduct<T> constructor(private var product: T?) : IProduct<T> {

        @Synchronized override fun onGetBean(): T {
            return product!!
        }

        override fun onDestory() {
            product = null
        }

        override fun type(): Type {
            return Type.Init
        }
    }

    //克隆产品
    private class CloneProduct<T> constructor(private var product: T?) : IProduct<T> {

        override fun onGetBean(): T {
            try {
                return Obj.cloneobj<T>(product!!)!!
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException(e)
            }

        }

        override fun onDestory() {
            product = null
        }

        override fun type(): Type {
            return Type.Clone
        }
    }

    //自定义产品
    abstract class CustomProduct<I, T> : IProduct<T> {
        override fun type(): Type {
            return Type.Custom
        }

        abstract val iClass: Class<I>

        abstract val tClass: Class<T>
    }

    companion object {

        val factory = Beans()
    }

}//获得产品
