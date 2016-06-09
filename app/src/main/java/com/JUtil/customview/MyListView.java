package com.JUtil.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by leil7 on 2016/6/8.
 */
public class MyListView extends ListView {

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        l("onAttachedToWindow");
        super.onAttachedToWindow();
    }

    //当View中所有的子控件均被映射成xml后触发
    @Override
    protected void onFinishInflate() {
        l("onFinishInflate");
        super.onFinishInflate();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        l("onVisibilityChanged", changedView, visibility);
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        l("onMeasure", widthMeasureSpec, heightMeasureSpec, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l("onLayout", changed, l, t, r, b);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        l("onDraw", canvas);
        super.onDraw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        l("onFocusChanged", gainFocus, direction, previouslyFocusedRect);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        l("onWindowFocusChanged", hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onDetachedFromWindow() {
        l("onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        l("onSizeChanged", w, h, oldw, oldh);
        super.onSizeChanged(w, h, oldw, oldh);
    }

//    @Override
//    public boolean onHoverEvent(MotionEvent event) {
//        l("onHoverEvent", event);
//        return super.onHoverEvent(event);
//    }
//
//    @Override
//    public boolean onInterceptHoverEvent(MotionEvent event) {
//        l("onInterceptHoverEvent", event);
//        return super.onInterceptHoverEvent(event);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        l("onTouchEvent", ev);
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        l("onInterceptTouchEvent", ev);
        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public void scrollBy(int x, int y) {
//        l("scrollBy", x, y);
//        super.scrollBy(x, y);
//    }
//
//    @Override
//    public void scrollTo(int x, int y) {
//        l("scrollTo", x, y);
//        super.scrollTo(x, y);
//    }
//
//    @Override
//    public void scrollListBy(int y) {
//        l("scrollListBy", y);
//        super.scrollListBy(y);
//    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        l("onOverScrolled", scrollX, scrollY, clampedX, clampedY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        l("onScrollChanged", l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public static final void l(Object... os) {
        StringBuilder sb = new StringBuilder();
        for (Object o : os)
            sb.append(o).append('\t');
        Log.v("MyListView", sb.toString());
    }

}
