package com.shoujiduoduo.duonavigator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * package: com.shoujiduoduo.duonavigator
 * create by zhilin on 2018/3/31 23:11
 * des: 可监听滑动的ScrollView
 */
public class ObservableScrollView extends ScrollView {

    protected boolean mScrollable = true;

    private OnScrollChangeListener mListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener){
        mListener = listener;
    }

    public void setScrollable(boolean enable){
        mScrollable = enable;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !mScrollable || super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldX, int oldY) {
        super.onScrollChanged(scrollX, scrollY, oldX, oldY);
        if (mListener != null){
            mListener.onScrollChange(scrollX,scrollY,oldX,oldY);
        }
    }

    public interface OnScrollChangeListener{
        void onScrollChange(int scrollX, int scrollY, int oldX, int oldY);
    }
}
