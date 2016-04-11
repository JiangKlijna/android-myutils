package com.jiangKlijna.view;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * view的事件,以静态内部类的形式存在
 * Author: jiangKlijna
 */
public class Event {
    private Event() {
    }

    public static class OnTouchListener implements View.OnTouchListener, Serializable {
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

    /**
     * 双击事件
     */
    public static abstract class OnDoubleClickListener implements View.OnClickListener, Serializable {
        private boolean kg;

        @Override
        public synchronized final void onClick(View view) {
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
    public static class OnClickListeners implements View.OnClickListener, Serializable {
        private final LinkedHashSet<View.OnClickListener> clicks = new LinkedHashSet<View.OnClickListener>();

        @Override
        public synchronized final void onClick(View view) {
            for (View.OnClickListener click : clicks) {
                click.onClick(view);
            }
        }

        public synchronized OnClickListeners addClick(View.OnClickListener click) {
            clicks.add(click);
            return this;
        }

        public synchronized OnClickListeners removeClick(View.OnClickListener click) {
            clicks.remove(click);
            return this;
        }

        public synchronized OnClickListeners clear() {
            clicks.clear();
            return this;
        }

        public int size() {
            return clicks.size();
        }

        public synchronized OnClickListeners addAll(OnClickListeners clickListeners) {
            clicks.addAll(clickListeners.clicks);
            return this;
        }

    }
}
