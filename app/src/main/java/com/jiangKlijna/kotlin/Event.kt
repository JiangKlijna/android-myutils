package com.jiangKlijna.kotlin

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import java.util.LinkedHashSet

/**
 * view的事件,以静态内部类的形式存在
 * Author: com.jiangKlijna
 */
object Event {

    class OnTouchListener : View.OnTouchListener {
        private var mPosX: Float = 0.toFloat()
        private var mPosY: Float = 0.toFloat()
        private var mCurrentPosX: Float = 0.toFloat()
        private var mCurrentPosY: Float = 0.toFloat()

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mPosX = event.x
                    mPosY = event.y
                    onDown(view)
                }
                MotionEvent.ACTION_MOVE -> {
                    mCurrentPosX = event.x
                    mCurrentPosY = event.y
                    if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                        onLeft_Right(view)
                    } else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 10) {
                        onRight_Left(view)
                    } else if (mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                        onTop_Bottom(view)
                    } else if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                        onBottom_Top(view)
                    }
                }
                MotionEvent.ACTION_UP -> onUp(view)
            }
            return true
        }

        protected fun onLeft_Right(view: View) {}

        protected fun onRight_Left(view: View) {}

        protected fun onBottom_Top(view: View) {}

        protected fun onTop_Bottom(view: View) {}

        protected fun onDown(view: View) {}

        protected fun onUp(view: View) {}
    }

    class OnScrollListener : AbsListView.OnScrollListener {

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            when (scrollState) {
                AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> onStop(view)
                AbsListView.OnScrollListener.SCROLL_STATE_FLING -> onFling(view)
                AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> onStart(view)
            }
        }

        //停止触摸后还在滚动
        fun onFling(view: AbsListView) {}

        //停止滚动
        fun onStop(view: AbsListView) {}

        //开始滚动
        fun onStart(view: AbsListView) {}

        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {}
    }

    /**
     * listview使用的OnTouchListener
     */
    abstract class OnListViewTouchListener(val mListView: ListView) : View.OnTouchListener {
        private var mTouchPosition: Int = 0

        override fun onTouch(view: View, ev: MotionEvent): Boolean {
            val pos = mListView.pointToPosition(ev.x.toInt(), ev.y.toInt())
            if (pos != mTouchPosition) {
                mTouchPosition = pos
                onTouchItem(mListView, pos)
            }
            return false
        }

        protected abstract fun onTouchItem(listView: ListView, position: Int)
    }

    /**
     * 双击事件
     */
    abstract class OnDoubleClickListener : View.OnClickListener {
        private var kg: Boolean = false

        override fun onClick(view: View) {
            if (kg) {
                onDoubleClick(view)
            } else {
                kg = true
                Thread(backR).start()//推荐使用线程池
            }
        }

        private val backR = Runnable {
            SystemClock.sleep(1000)
            kg = false
        }

        protected abstract fun onDoubleClick(view: View)
    }

    /**
     * 添加点击事件s
     */
    class OnClickListeners : View.OnClickListener, Iterable<View.OnClickListener> {
        private val clicks = LinkedHashSet<View.OnClickListener>()

        override fun onClick(view: View) {
            for (click in clicks) click.onClick(view)
        }

        fun addClick(click: View.OnClickListener): OnClickListeners {
            clicks.add(click)
            return this
        }

        fun removeClick(click: View.OnClickListener): OnClickListeners {
            clicks.remove(click)
            return this
        }

        fun clear(): OnClickListeners {
            clicks.clear()
            return this
        }

        fun size(): Int {
            return clicks.size
        }

        fun addAll(clickListeners: OnClickListeners): OnClickListeners {
            clicks.addAll(clickListeners.clicks)
            return this
        }

        override fun iterator(): Iterator<View.OnClickListener> {
            return clicks.iterator()
        }
    }
}
