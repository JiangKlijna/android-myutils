package com.jiangKlijna.object;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存对象
 * Author: jiangKlijna
 */
public class ObjectKey<T> implements Serializable {
    private static final transient ConcurrentHashMap<Integer, Object> OBJECT_MAP = new ConcurrentHashMap<Integer, Object>();

    public static final void test() {
        ObjectKey<File> key1 = saveObj_map(Environment.getDataDirectory());
        ObjectKey<ObjectKey> key2 = saveObj_file(key1);
        ObjectKey<File> fileKey = key2.popObj();
        File f = fileKey.popObj();
        if (f == Environment.getDataDirectory()) {
            System.out.print(1);
        } else {
            System.out.print(2);
        }
        fileKey.destory();
    }

    public static <ST> ObjectKey<ST> saveObj_map(final ST obj) {
        ObjectKey key = new ObjectKey(obj.getClass());
        OBJECT_MAP.put(key.hashcode, obj);
        return key;
    }

    public static <ST> ObjectKey<ST> saveObj_file(final Serializable obj) {
        ObjectKey key = new ObjectKey(obj.getClass(), true);
        try {
            File file = key.getThisFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return key;
    }

    public static void destoryAll() {
        OBJECT_MAP.clear();
    }

    private final Class<T> clasz;
    private final boolean isFile;
    private final int hashcode;

    private ObjectKey(Class<T> clasz) {
        this(clasz, false);
    }

    private ObjectKey(Class<T> clasz, boolean isFile) {
        this.clasz = clasz;
        this.isFile = isFile;
        this.hashcode = super.hashCode();
    }

    private transient File thisFile;

    private File getThisFile() {
        if (thisFile == null) {
            try {
                thisFile = new File(Environment.getExternalStorageDirectory(), hashCode() + ".sa");
                if (!thisFile.exists()) {
                    thisFile.createNewFile();
                }
            } catch (IOException e) {
            }
        }
        return thisFile;
    }

    //从map中获得对象

    public T getObj() {
        T obj;
        if (isFile) {
            try {
                FileInputStream fis = new FileInputStream(getThisFile());
                ObjectInputStream oos = new ObjectInputStream(fis);
                obj = (T) oos.readObject();
                oos.close();
                fis.close();
            } catch (Exception e) {
                return null;
//                throw new RuntimeException("IOException ClassNotFoundException...", e);
            }
        } else {
            obj = (T) OBJECT_MAP.get(hashcode);
            if (obj == null) {
                return null;
//                throw new RuntimeException("Has been destroyed");
            }
        }
        return obj;
    }

    //从map/file中获得对象,并且删掉map/file中的缓存
    public T popObj() {
        T obj = getObj();
        destory();
        return obj;
    }

    /**
     * 原型模式:以map/file中存放的obj为原型通过反射获得一个属性相同的对象
     * 此类必须有空构造
     */
    public T prototype() throws Exception {
        return ObjUtil.cloneobj(getObj());
    }

    //是否可以进行(获得对象操作)
    public boolean isDestory() {
        return isFile ? getThisFile().exists() : OBJECT_MAP.containsKey(hashcode);
    }

    //销毁
    public void destory() {
        if (isFile) {
            File file = getThisFile();
            if (file.exists()) {
                file.delete();
            }
        } else {
            OBJECT_MAP.remove(hashcode);
        }
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {
        return hashcode == o.hashCode();
    }

    @Override
    public String toString() {
        return "ObjectKey [class = " + clasz.getName() + " , save in " + (isFile ? "file" : "map") + "]";
    }
}
