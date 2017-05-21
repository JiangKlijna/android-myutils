package com.jiangKlijna.java;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/7/14.
 */
public class Beans implements Serializable {
    protected Beans() {
    }

    public enum Type {
        Single, Prototype, Init, Clone, Custom
    }

    private static final Beans bs = new Beans();

    public static Beans getFactory() {
        return bs;
    }

    //存储产品
    private transient final Map<IKey, IProduct> products = new ConcurrentHashMap<>();

//    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
//    private final Lock r = rwl.readLock();
//    private final Lock w = rwl.writeLock();

    //获得产品实例
    public <I, T> I getBean(Class<I> iClass, Class<T> tClass) {
        return (I) getIProduct(iClass, tClass).onGetBean();
    }

    public <T> T getBean(Class<T> tClass) {
        return getBean(tClass, tClass);
    }

    //regist_product parameter
    // @param iClass 产品返回值类型的class
    // @param tClass 产品本身类型的class
    // @param Type 产品的类型
    // @param IProduct 产品

    public void regist_product(CustomProduct product) {
        regist_product(product.getIClass(), product.getTClass(), product);
    }

    public void regist_product(Class tClass) {
        regist_product(tClass, tClass);
    }

    public void regist_product(Class iClass, Class tClass) {
        regist_product(iClass, tClass, Type.Single);
    }

    public void regist_product(Class iClass, Type type) {
        regist_product(iClass, iClass, type);
    }

    public void regist_product(Class iClass, Class tClass, Type type) {
        switch (type) {
            case Single:
                regist_product(iClass, tClass, new SingleProduct(tClass));
                break;
            case Prototype:
                regist_product(iClass, tClass, new PrototypeProduct(tClass));
                break;
            default:
                throw new RuntimeException("type must in [Single, Prototype]");
        }
    }

    public <T> void regist_product(Class<T> tClass, Type type, T product) {
        regist_product(tClass, tClass, type, product);
    }

    public <I, T> void regist_product(Class<I> iClass, Class<T> tClass, Type type, T product) {
        switch (type) {
            case Init:
                regist_product(iClass, tClass, new InitProduct(product));
                break;
            case Clone:
                regist_product(iClass, tClass, new CloneProduct(product));
                break;
            default:
                throw new RuntimeException("type must in [Clone, Init]");
        }
    }

    private <T> void regist_product(Class<T> iClass, IProduct<T> product) {
        regist_product(iClass, iClass, product);
    }

    private <I, T> void regist_product(Class<I> iClass, Class<T> tClass, IProduct<T> product) {
        IKey iKey = new IKey(iClass, tClass);
        if (null == getIProduct(iKey)) {
            products.put(iKey, product);
        } else {
            throw new RuntimeException("Can not repeat the registration of product, Must first unregist_product().");
        }
    }

    //注销产品
    public void unregist_product(Class iClass, Class tClass) {
        IProduct p = getIProduct(iClass, tClass);
        p.onDestory();
        products.remove(p);
    }

    public void unregist_product(Class iClass) {
        unregist_product(iClass, iClass);
    }

    public void destoryAllProduct() {
        for (IProduct p : products.values())
            p.onDestory();
        products.clear();
    }

    //获得产品
    private final IProduct getIProduct(Class iClass) {
        return getIProduct(iClass, iClass);
    }

    private final IProduct getIProduct(Class iClass, Class tClass) {
        return products.get(new IKey(iClass, tClass));
    }

    private final IProduct getIProduct(IKey iKey) {
        return products.get(iKey);
    }

    //存放产品map的key
    private static final class IKey<I, T> implements Comparable<IKey>, Serializable {

        private final Class<I> iClass;
        private final Class<T> tClass;

        private IKey(Class<I> iClass, Class<T> tClass) {
            this.iClass = iClass;
            this.tClass = tClass;
        }

        private void check() {
            if (iClass == null || tClass == null) {
                throw new RuntimeException("iClass not is null || tClass not is null");
            }
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = (result << 5) - 1 + System.identityHashCode(iClass);
            result = (result << 5) - 1 + System.identityHashCode(tClass);
            return result;
        }

        @Override
        public String toString() {
            return "IKey [iClass = " + iClass.toString() + ", tClass = " + tClass.toString() + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof IKey) {
                IKey another = (IKey) o;
                return equals(another.iClass, another.tClass);
            }
            return this == o;
        }

        public boolean equals(Class iClass, Class tClass) {
            return this.iClass == iClass && this.tClass == tClass;
        }

        @Override
        public int compareTo(IKey iKey) {
            return compareTo(iKey.iClass, iKey.tClass);
        }

        public int compareTo(Class iClass, Class tClass) {
            if (equals(iClass, tClass)) return 0;
            int i = this.iClass.getName().compareTo(iClass.getName());
            return i != 0 ? i : i + this.tClass.getName().compareTo(tClass.getName());
        }
    }

    //存放产品map的value
    private interface IProduct<T> extends Serializable {
        T onGetBean();

        void onDestory();

        Type type();
    }

    //单例产品
    private static final class SingleProduct<T> implements IProduct<T> {
        private final Class<T> bClass;
        private T product;

        private SingleProduct(Class<T> bClass) {
            this.bClass = bClass;
        }

        @Override
        public synchronized T onGetBean() {
            if (product == null) {
                try {
                    Constructor<T> c = bClass.getDeclaredConstructor();
                    c.setAccessible(true);
                    product = c.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            return product;
        }

        @Override
        public void onDestory() {
            product = null;
        }

        @Override
        public Type type() {
            return Type.Single;
        }
    }

    //原型产品
    private static final class PrototypeProduct<T> implements IProduct<T> {
        private final Class<T> bClass;

        private PrototypeProduct(Class<T> bClass) {
            this.bClass = bClass;
        }

        @Override
        public synchronized T onGetBean() {
            try {
                Constructor<T> c = bClass.getDeclaredConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDestory() {
        }

        @Override
        public Type type() {
            return Type.Prototype;
        }
    }

    //初始化产品
    private static final class InitProduct<T> implements IProduct<T> {
        private T product;

        private InitProduct(T product) {
            this.product = product;
        }

        @Override
        public synchronized T onGetBean() {
            return product;
        }

        @Override
        public void onDestory() {
            product = null;
        }

        @Override
        public Type type() {
            return Type.Init;
        }
    }

    //克隆产品
    private static final class CloneProduct<T> implements IProduct<T> {
        private T product;

        private CloneProduct(T product) {
            this.product = product;
        }

        @Override
        public T onGetBean() {
            try {
                return Obj.cloneobj(product);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onDestory() {
            product = null;
        }

        @Override
        public Type type() {
            return Type.Clone;
        }
    }

    //自定义产品
    public static abstract class CustomProduct<I, T> implements IProduct<T> {
        @Override
        public final Type type() {
            return Type.Custom;
        }

        protected abstract Class<I> getIClass();

        protected abstract Class<T> getTClass();
    }

}
