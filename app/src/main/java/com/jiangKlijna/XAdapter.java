package com.jiangKlijna;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.Collections;

/**
 * 万能adapter
 * ViewHolder以静态内部类的形式存在(私有构造),通过getHoder方法来获取ViewHolder对象
 * Author: com.jiangKlijna
 */
public abstract class XAdapter<T> extends BaseAdapter {

    private final Context mContent;
    private IDataSet<T> dataSet;
    private Object obj;

    public XAdapter(Context con) {
        this(Collections.EMPTY_LIST, con);
    }

    public XAdapter(T[] array, Context context) {
        this(new ArrayDataSet(array), context);
    }

    public XAdapter(Collection<T> collection, Context context) {
        this(new CollectionDataSet(collection), context);
    }

    public XAdapter(IDataSet dataSet, Context context) {
        this.dataSet = dataSet;
        mContent = context;
    }

    public void setData(Collection<T> collection) {
        if (dataSet instanceof CollectionDataSet) {
            CollectionDataSet cds = (CollectionDataSet) dataSet;
            if (collection != cds.collection) {
                cds.collection = collection;
            }
            cds.onNotifyDataSetChanged();
        } else {
            dataSet = new CollectionDataSet(collection);
        }
        super.notifyDataSetChanged();
    }

    public void setData(T[] array) {
        if (dataSet instanceof ArrayDataSet) {
            ArrayDataSet ads = (ArrayDataSet) dataSet;
            if (array != ads.array) {
                ads.array = array;
            }
            ads.onNotifyDataSetChanged();
        } else {
            dataSet = new ArrayDataSet(array);
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        dataSet.onNotifyDataSetChanged();
        super.notifyDataSetChanged();
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    public Object getObject() {
        return this.obj;
    }

    public void clear() {
        setData(Collections.EMPTY_LIST);
    }

    protected Collection<T> getCollection() {
        if (dataSet instanceof CollectionDataSet)
            return ((CollectionDataSet) dataSet).collection;
        return null;
    }

    protected T[] getArray() {
        if (dataSet instanceof ArrayDataSet)
            return ((ArrayDataSet<T>) dataSet).array;
        return null;
    }

    protected Context getContext() {
        return this.mContent;
    }

    @Override
    public int getCount() {
        return dataSet.getCount();
    }

    @Override
    public T getItem(int position) {
        return dataSet.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public static ViewHolder getHolder(Context context, View convertView, Class<? extends View> clasz, int position) {
        if (convertView == null) {
            try {
                return new ViewHolder(clasz.getConstructor(Context.class).newInstance(context), position);
            } catch (Exception e) {
                throw new RuntimeException(clasz.getName() + " Construction method error. \n", e);
            }
        }
        return (ViewHolder) convertView.getTag();
    }

    public static ViewHolder getHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            try {
                return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false), position);
            } catch (Exception e) {
                throw new RuntimeException(layoutId + " Layout Inflater error. \n", e);
            }
        }
        return (ViewHolder) convertView.getTag();
    }

    public static class ViewHolder {
        private final SparseArray<View> mViews;
        private final View mConvertView;
        private final int mPosition;

        private ViewHolder(View view, int position) {
            mPosition = position;
            mViews = new SparseArray<View>();
            mConvertView = view;
            mConvertView.setTag(this);
        }

        public final <T extends View> T getConvertView() {
            return (T) mConvertView;
        }

        public final int getPosition() {
            return mPosition;
        }

        public final <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }

    public interface IDataSet<T> {
        int getCount();

        T getItem(int position);

        void onNotifyDataSetChanged();
    }

    private static final class CollectionDataSet<T> implements IDataSet {

        private Collection<T> collection;
        private Object[] datas;

        private CollectionDataSet(Collection<T> collection) {
            this.collection = collection;
            onNotifyDataSetChanged();
        }

        @Override
        public void onNotifyDataSetChanged() {
            datas = collection.toArray();
        }

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public T getItem(int position) {
            return (T) datas[position];
        }
    }

    private static final class ArrayDataSet<T> implements IDataSet {

        private T[] array;

        private ArrayDataSet(T[] array) {
            this.array = array;
        }

        @Override
        public void onNotifyDataSetChanged() {
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public T getItem(int position) {
            return array[position];
        }
    }
}
