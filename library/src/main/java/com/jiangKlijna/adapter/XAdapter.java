package com.jiangKlijna.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 万能adapter
 * ViewHolder以静态内部类的形式存在(私有构造),通过getHoder方法来获取ViewHolder对象
 * Author: jiangKlijna
 */
public abstract class XAdapter<T> extends BaseAdapter {
    public static final String TAG = XAdapter.class.getName();
    private static final ArrayList D = new ArrayList();

    public enum MODE {ARRAY, LIST, MAP, SET, JSON}

    private MODE mode;
    private List<T> list;
    private T[] array;
    private Map<?, T> map;
    private Set<T> set;
    private JSONArray ja;
    private Object obj;
    private final Context mContent;

    public XAdapter(Context con) {
        this(D, con);
    }

    public XAdapter(List<T> list, Context con) {
        this.mContent = con;
        this.list = list;
        this.mode = MODE.LIST;
    }

    public XAdapter(T[] array, Context con) {
        this.mContent = con;
        this.array = array;
        this.mode = MODE.ARRAY;
    }

    public XAdapter(Map<?, T> map, Context con) {
        this.mContent = con;
        this.map = map;
        this.mode = MODE.MAP;
    }

    public XAdapter(Set<T> set, Context con) {
        this.mContent = con;
        this.set = set;
        this.mode = MODE.SET;
    }

    public XAdapter(JSONArray ja, Context con) {
        this.mContent = con;
        this.ja = ja;
        this.mode = MODE.JSON;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public void setList(List<T> list) {
        this.list = list;
        this.mode = MODE.LIST;
        notifyDataSetChanged();
    }

    public void setArray(T[] array) {
        this.array = array;
        this.mode = MODE.ARRAY;
        notifyDataSetChanged();
    }

    public void setMap(Map<?, T> map) {
        this.map = map;
        this.mode = MODE.MAP;
        notifyDataSetChanged();
    }

    public void setSet(Set<T> set) {
        this.set = set;
        this.mode = MODE.SET;
        notifyDataSetChanged();
    }

    public void setJSON(JSONArray ja) {
        this.ja = ja;
        this.mode = MODE.JSON;
        notifyDataSetChanged();
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    public Object getObject() {
        return this.obj;
    }

    protected List<T> getList() {
        return this.list;
    }

    protected T[] getArray() {
        return this.array;
    }

    protected Map<?, T> getMap() {
        return this.map;
    }

    protected Set<T> getSet() {
        return this.set;
    }

    protected JSONArray getJSON() {
        return this.ja;
    }

    protected Context getContext() {
        return this.mContent;
    }

    @Override
    public int getCount() {
        if (mode == MODE.LIST) {
            return list.size();
        } else if (mode == MODE.ARRAY) {
            return array.length;
        } else if (mode == MODE.MAP) {
            return map.size();
        } else if (mode == MODE.SET) {
            return set.size();
        } else if (mode == MODE.JSON) {
            return ja.length();
        }
        return 0;
    }

    @Deprecated
    @Override
    public T getItem(int position) {
        if (mode == MODE.LIST) {
            return this.list.get(position);
        } else if (mode == MODE.ARRAY) {
            return this.array[position];
        } else if (mode == MODE.MAP) {
            return (T) this.map.values().toArray()[position];
        } else if (mode == MODE.SET) {
            return (T) this.set.toArray()[position];
        } else if (mode == MODE.JSON) {
            try {
                return (T) this.ja.get(position);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        return initData(position, convertView, parent);
    }

    protected abstract View initData(int position, View convertView, ViewGroup parent);

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

        public final View getConvertView() {
            return mConvertView;
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
}
