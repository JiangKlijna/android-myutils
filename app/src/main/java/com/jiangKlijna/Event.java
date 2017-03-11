package com.jiangKlijna;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * view的事件,以静态内部类的形式存在
 * Author: com.jiangKlijna
 */
public class Event {
    private Event() {
    }

    public static class OnTouchListener implements View.OnTouchListener {
        private float mPosX;
        private float mPosY;
        private float mCurrentPosX;
        private float mCurrentPosY;

        @Override
        public final boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPosX = event.getX();
                    mPosY = event.getY();
                    onDown(view);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentPosX = event.getX();
                    mCurrentPosY = event.getY();
                    if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                        onLeft_Right(view);
                    } else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                        onRight_Left(view);
                    } else if (mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                        onTop_Bottom(view);
                    } else if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                        onBottom_Top(view);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onUp(view);
                    break;
            }
            return true;
        }

        protected void onLeft_Right(View view) {
        }

        protected void onRight_Left(View view) {
        }

        protected void onBottom_Top(View view) {
        }

        protected void onTop_Bottom(View view) {
        }

        protected void onDown(View view) {
        }

        protected void onUp(View view) {
        }
    }

    public static class OnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public final void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    onStop(view);
                    break;
                case OnScrollListener.SCROLL_STATE_FLING:
                    onFling(view);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    onStart(view);
                    break;
            }
        }

        //停止触摸后还在滚动
        public void onFling(AbsListView view) {
        }

        //停止滚动
        public void onStop(AbsListView view) {
        }

        //开始滚动
        public void onStart(AbsListView view) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /**
     * listview使用的OnTouchListener
     */
    public static abstract class OnListViewTouchListener implements View.OnTouchListener {
        public final ListView mListView;
        private int mTouchPosition;

        public OnListViewTouchListener(final ListView listView) {
            mListView = listView;
        }

        @Override
        public boolean onTouch(View view, MotionEvent ev) {
            int pos = mListView.pointToPosition((int) ev.getX(), (int) ev.getY());
            if (pos != mTouchPosition) {
                onTouchItem(mListView, mTouchPosition = pos);
            }
            return false;
        }

        protected abstract void onTouchItem(ListView listView, int position);
    }

    /**
     * 双击事件
     */
    public static abstract class OnDoubleClickListener implements View.OnClickListener {
        private boolean kg;

        @Override
        public final void onClick(View view) {
            if (kg) {
                onDoubleClick(view);
            } else {
                kg = true;
                new Thread(backR).start();//推荐使用线程池
            }
        }

        private final Runnable backR = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                kg = false;
            }
        };

        protected abstract void onDoubleClick(View view);
    }

    /**
     * 添加点击事件s
     */
    public static class OnClickListeners implements View.OnClickListener, Iterable {
        private final LinkedHashSet<View.OnClickListener> clicks = new LinkedHashSet<View.OnClickListener>();

        @Override
        public final void onClick(View view) {
            for (View.OnClickListener click : clicks) click.onClick(view);
        }

        public OnClickListeners addClick(View.OnClickListener click) {
            clicks.add(click);
            return this;
        }

        public OnClickListeners removeClick(View.OnClickListener click) {
            clicks.remove(click);
            return this;
        }

        public OnClickListeners clear() {
            clicks.clear();
            return this;
        }

        public int size() {
            return clicks.size();
        }

        public OnClickListeners addAll(OnClickListeners clickListeners) {
            clicks.addAll(clickListeners.clicks);
            return this;
        }

        @Override
        public Iterator iterator() {
            return clicks.iterator();
        }
    }
}
