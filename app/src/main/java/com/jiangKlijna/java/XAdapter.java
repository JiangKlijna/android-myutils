package com.jiangKlijna.java;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 万能adapter
 * ViewHolder以静态内部类的形式存在(私有构造),通过getHoder方法来获取ViewHolder对象
 * Author: jiangKlijna
 */
public abstract class XAdapter<T> extends BaseAdapter {

    private final CollectionDataSet<T> dataSet;
    private final Context mContent;
    private Object obj;

    public XAdapter(Context context) {
        this(Collections.EMPTY_LIST, context);
    }

    public XAdapter(T[] array, Context context) {
        this(Arrays.asList(array), context);
    }

    public XAdapter(Collection<T> collection, Context context) {
        this.dataSet = new CollectionDataSet(collection);
        mContent = context;
    }

    public void setData(Collection<T> collection) {
        if (collection != dataSet.collection)
            dataSet.collection = collection;
        dataSet.onNotifyDataSetChanged();
        super.notifyDataSetChanged();
    }

    public void setData(T[] array) {
        setData(Arrays.asList(array));
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

    public Collection<T> getCollection() {
        return dataSet.collection;
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

    public static ViewHolder getHolder(Context context, View convertView, Class<? extends View> clasz) {
        if (convertView == null) {
            try {
                return new ViewHolder(clasz.getConstructor(Context.class).newInstance(context));
            } catch (Exception e) {
                throw new RuntimeException(clasz.getName() + " Construction method error. \n", e);
            }
        }
        return (ViewHolder) convertView.getTag();
    }

    public static ViewHolder getHolder(Context context, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            try {
                return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
            } catch (Exception e) {
                throw new RuntimeException(layoutId + " Layout Inflater error. \n", e);
            }
        }
        return (ViewHolder) convertView.getTag();
    }

    public static class ViewHolder {
        private SparseArray<View> mViews;
        private final View mConvertView;

        private ViewHolder(View view) {
            mConvertView = view;
            mConvertView.setTag(this);
        }

        public final <T extends View> T getConvertView() {
            return (T) mConvertView;
        }

        public final <T extends View> T getView(int viewId) {
            if (mViews == null)
                mViews = new SparseArray(6);
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public final <T extends View> T getView(int viewId, Class<T> cls) {
            return getView(viewId);
        }
    }

//    private interface IDataSet<T> {
//        int getCount();
//        T getItem(int position);
//        void onNotifyDataSetChanged();
//    }

    private static final class CollectionDataSet<T> {

        private Collection<T> collection;
        private Object[] datas;

        private CollectionDataSet(Collection<T> collection) {
            this.collection = collection;
            onNotifyDataSetChanged();
        }

        public void onNotifyDataSetChanged() {
            datas = collection.toArray();
        }

        public int getCount() {
            return datas.length;
        }

        public T getItem(int position) {
            return (T) datas[position];
        }
    }
}